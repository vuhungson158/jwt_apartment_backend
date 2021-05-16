package com.hung91hn.apartment.controller;

import com.hung91hn.apartment.Util;
import com.hung91hn.apartment.model.*;
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
import java.util.Optional;
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
    private RedisTemplate inActive;
    @Autowired
    private Util util;

    @PostMapping("/register")
    public Response register(@RequestBody User user) {
        final String valid = validateLogin(user);
        if (valid != null) return new Response(valid);

        if (inActive.opsForValue().get(user.phone) != null || repository.findByPhone(user.phone) != null)
            return new Response("Tên đăng nhập hoặc số điện thoại đã tồn tại");

        user.password = encoder.encode(user.password);

        final int otp = ThreadLocalRandom.current().nextInt(0, 1000000);
        util.log("otp: " + otp);
//        sendSMS(user.phone, otp);

        final int timeOut = 10;
        inActive.opsForValue().set(user.phone, new UserRegister(otp, user), timeOut, TimeUnit.MINUTES);
        return new Response(200, String.format("OTP đã được gửi đến số điện thoại: %s\nVui lòng kích hoạt trong %d phút!", user.phone, timeOut));
    }

    private void sendSMS(String phone, int otp) {
        Message.creator(
                new PhoneNumber(phone.startsWith("0") ? phone.replaceFirst("0", "+84") : phone),
                new PhoneNumber("+12059386033"), String.format("Mã kích hoạt của bạn là: %06d", otp)).create();
    }

    @PostMapping("/active")
    public Response active(@RequestBody UserActive userActive) {
        final String key = userActive.phone;
        final UserRegister _userRegister = (UserRegister) inActive.opsForValue().get(key);
        if (_userRegister == null || userActive.otp != _userRegister.otp) return new Response("Sai OTP");

        final User user = _userRegister.user;
        user.roles = User.USER;
        user.state = User.AVAILABLE;
        inActive.delete(key);

        return responseLogin(repository.save(user));
    }

    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        final User _user = repository.findByPhone(user.phone);
        return _user != null && encoder.matches(user.password, _user.password) ?
                responseLogin(_user) : new Response("Thông tin không chính xác");
    }

    private Response responseLogin(User user) {
        final UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.phone = user.phone;
        userPrincipal.roles = user.roles;

        user.password = null;
        return new ResponseT<>(new UserLogin(jwtUtil.generateToken(userPrincipal), user));
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

    @PostMapping("/getUser")
    public Response get(@RequestBody long id) {
        final Optional<User> optional = repository.findById(id);
        if (!optional.isPresent()) return new Response("User không tồn tại");

        final User user = optional.get();
        user.password = null;
        return new ResponseT<>(user);
    }
}
