package com.kob.backend.utils;

import io.jsonwebtoken.Claims;

public class JWTToUserId {
    public static Integer jwt2UserId(String jwtToken){
        int userId = -1;
        try {
            Claims claims = JwtUtil.parseJWT(jwtToken);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}
