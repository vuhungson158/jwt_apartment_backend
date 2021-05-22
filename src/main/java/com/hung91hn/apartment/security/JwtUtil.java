package com.hung91hn.apartment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hung91hn.apartment.helper.Util;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;

@Service
public class JwtUtil {
    private final String USER = "user";
    private final String SECRET = "12345678123456781234567812345678";
    @Autowired
    private Util util;
    @Autowired
    private RedisTemplate redis;
    @Autowired
    private ObjectMapper mapper;

    public String generateToken(UserPrincipal user) {
        try {
            final JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USER, user);
            final SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), builder.build());
            signedJWT.sign(new MACSigner(SECRET.getBytes()));
            final String token = signedJWT.serialize();
            redis.opsForValue().set(user.phone, token);
            return token;
        } catch (Exception e) {
            util.print(e.toString());
            return null;
        }
    }

    private JWTClaimsSet getClaims(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (ParseException | JOSEException e) {
            util.print(e.toString());
        }
        return claims;
    }

    public UserPrincipal getUser(String jwt) {
        final JWTClaimsSet claims = getClaims(jwt);
        if (claims != null) try {
            final JSONObject jsonObject = (JSONObject) claims.getClaim(USER);
            return mapper.readValue(jsonObject.toJSONString(), UserPrincipal.class);
        } catch (Exception e) {
            util.print(e.toString());
        }
        return null;
    }

    public UserPrincipal validate(String token) {
        if (StringUtils.hasText(token)) {
            final UserPrincipal user = getUser(token);
            if (user != null && token.equals(redis.opsForValue().get(user.phone))) return user;
        }

        return null;
    }
}
