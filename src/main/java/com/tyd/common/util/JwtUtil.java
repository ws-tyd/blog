package com.tyd.common.util;

import com.tyd.module.pojo.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author 谭越东
 * @date 2022/09/27
 */
@Component
public class JwtUtil {

    private static long time = 24 * 60 * 60 * 1000;

    //加密建
    private static String jwtKey = "admin";

    //过期时间
    private static int expirationTime=1;

    @Value("${jwt.signKey}")
    private String autoJwt;

    @Value("${jwt.expirationTime}")
    private int expiration;

    @PostConstruct
    public void initJwt() {
        jwtKey = this.autoJwt;
        expirationTime=this.expiration;
        System.out.println(expirationTime+"----"+this.autoJwt);
    }


    public static String buildToken(User user){
        JwtBuilder builder = Jwts.builder();
        return builder.setHeaderParam("alg","HS256")
                .setHeaderParam("blog","jwt")
                .claim("username",user.getUsername())
                .claim("role",user.getRole())
                .claim("user",user)
                .setExpiration(new Date(new Date().getTime()+(expirationTime*time)))
                .setId(BaseUtils.noUnderscoreUUID())
                .signWith(SignatureAlgorithm.HS256,jwtKey)
                .compact();
    }

    public static String getUserName(String token){
        JwtParser parser = Jwts.parser();
        Jwt parse = parser.setSigningKey(jwtKey).parse(token);
        Map body = (Map) parse.getBody();
        return body.get("username").toString();
    }

    public static String getPassword(String token){
        JwtParser parser = Jwts.parser();
        Jwt parse = parser.setSigningKey(jwtKey).parse(token);
        Map body = (Map) parse.getBody();
        return body.get("password").toString();
    }
    public static Map getUser(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(jwtKey).parseClaimsJws(token);
        return claimsJws.getBody().get("user",HashMap.class);
    }
    public static String getExpiration(String token){
        JwtParser parser = Jwts.parser();
        Jwt parse = parser.setSigningKey(jwtKey).parse(token);
        Map body = (Map) parse.getBody();
        return  body.get("exp").toString();
    }
    public static Map getBody(String token){
        JwtParser parser = Jwts.parser();
        Jwt parse = parser.setSigningKey(jwtKey).parse(token);
        return  (Map) parse.getBody();
    }
    public static void main(String[] args) {
        System.out.println(getExpiration("eyJhbGciOiJIUzI1NiIsImJsb2ciOiJqd3QifQ.eyJ1c2VybmFtZSI6IuWwj-WwjyIsInJvbGUiOiJ1c2VyIiwidXNlciI6eyJpZCI6NiwidXNlcklkIjo3MDUwNzM0MDIzODI3NjU2NzA0LCJ1c2VybmFtZSI6IuWwj-WwjyIsInBhc3N3b3JkIjoiMTIzNDU2IiwiZW1haWwiOm51bGwsInBob25lIjpudWxsLCJzZXgiOm51bGwsInJvbGUiOiJ1c2VyIiwiY3JlYXRlVGltZSI6MTY4MjY5MDI3OTAwMCwiYXZhdGFySWQiOjI3LCJhdmF0YXJVcmwiOiJodHRwOi8vNDcuMTAyLjE0OC4xNDo5MDAwL2F2YXRhci8yMDIzLTA0LzI4L2MyOWZhZjdjYTBkODRjNzM4NjViYmM0MmUyNTg5MGFjLmpwZyIsImluZm8iOiLmtYvor5XnroDku4sifSwiZXhwIjoxNjgzNzgyMjU1LCJqdGkiOiJhOTY2OWNkZmU5ZTg0ZTI0OWFkMzBkZjk2YjkzNDc5YyJ9.O6F4_eEA8HIgyEgWOK4LNr6gjS0xp7NplasTbVo2TE8"));
    }
}