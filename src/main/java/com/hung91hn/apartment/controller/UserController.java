package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.helper.Util;
import com.hung91hn.apartment.model.*;
import com.hung91hn.apartment.repository.UserRepository;
import com.hung91hn.apartment.security.JwtUtil;
import com.hung91hn.apartment.security.UserPrincipal;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@RestController
public class UserController {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisTemplate redis;
    @Autowired
    private Util util;

    @PostMapping("/register")
    public Response register(@RequestBody UserPrincipal user) {
        final String valid = validateLogin(user);
        if (valid != null) return new Response(valid);

        if (redis.opsForValue().get(user.phone) != null || repository.findByPhone(user.phone) != null)
            return new Response("Tên đăng nhập hoặc số điện thoại đã tồn tại");

        user.password = encoder.encode(user.password);

        final int otp = ThreadLocalRandom.current().nextInt(0, 1000000);
        util.print("otp: " + otp);
//        sendSMS(user.phone, otp);

        final int timeOut = 10;
        redis.opsForValue().set(user.phone, new UserRegister(otp, user), timeOut, TimeUnit.MINUTES);
        return new Response(Response.SUCCESS, String.format("OTP đã được gửi đến số điện thoại: %s\nVui lòng kích hoạt trong %d phút!", user.phone, timeOut));
    }

    private void sendSMS(String phone, int otp) {
        Message.creator(
                new PhoneNumber(phone.startsWith("0") ? phone.replaceFirst("0", "+84") : phone),
                new PhoneNumber("+12059386033"), String.format("Mã kích hoạt của bạn là: %06d", otp)).create();
    }

    @PostMapping("/active")
    public Response active(@RequestBody UserActive userActive) {
        final String key = userActive.phone;
        final UserRegister _userRegister = (UserRegister) redis.opsForValue().get(key);
        if (_userRegister == null || userActive.otp != _userRegister.otp) return new Response("Sai OTP");

        final UserPrincipal user = _userRegister.user;
        user.roles = User.USER;
        user.state = User.State.Activate;
        redis.delete(key);

        return responseLogin(repository.save(user));
    }

    @PostMapping("/login")
    public Response login(@RequestBody UserPrincipal userPrincipal) {
        final User user = repository.findByPhone(userPrincipal.phone);
        if (user != null && encoder.matches(userPrincipal.password, user.password)) {
            if (user.state == User.State.Activate) {
                userPrincipal.id = user.id;
                userPrincipal.roles = user.roles;
                userPrincipal.displayName = user.displayName;
                userPrincipal.idCard = user.idCard;
                return responseLogin(userPrincipal);
            } else return new Response("Tài khoản bị vô hiệu");
        } else return new Response("Thông tin không chính xác");
    }

    private Response responseLogin(UserPrincipal user) {
        user.password = null;
        return new ResponseT<>(new UserLogin(jwtUtil.generateToken(user), user));
    }

    private String validateLogin(User user) {
        final Map<String, String> map = new HashMap<>();
        map.put("số điện thoại", user.phone);
        map.put("mật khẩu", user.password);
        final String empty = checkEmpty(map);

        if (empty == null) {
            final int phoneMin = 10;
            final int passMin = 8;
            if (user.phone.length() < phoneMin) {
                return String.format("Số điện thoại tối thiểu %d ký tự!", phoneMin);
            } else if (user.password.length() < passMin) return String.format("Mật khẩu tối thiểu %d ký tự!", passMin);
            return null;
        } else return empty;
    }

    private String checkEmpty(Map<String, String> params) {
        final StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet())
            if (!StringUtils.hasLength(entry.getValue()))
                builder.append(entry.getKey()).append(", ");
        return builder.length() > 0 ? "Không được để trống: " + builder : null;
    }

    /*@PostMapping("/getUser")
    public Response get(@RequestBody long id) {
        final Optional<User> optional = repository.findById(id);
        if (!optional.isPresent()) return new Response("User không tồn tại");

        final User user = optional.get();
        user.password = null;
        return new ResponseT<>(user);
    }*/
}
