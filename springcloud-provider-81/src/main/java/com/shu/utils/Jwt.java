package com.shu.utils;


import com.alibaba.fastjson.JSONArray;
import com.shu.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jwt{
    public static void setSecret(String secret) {
        Jwt.secret = secret;
    }

    public static void setExpiration(int expiration) {
        Jwt.expiration = expiration;
    }

    private static String secret="file_platform_nb";
    private static long expiration= 1000 * 24 * 60 * 60 * 7;
//    private static final String SUBJECT="file_platform";
    private static final String CLAIM_KEY_USERNAME="username";
    private static final String CLAIM_KEY_CREATED="created";
    private static final String CLAIM_KEY_ROLES="roles";

    //获得的token中得到用户
    public static String getUsernameFromToken(String token){
        String username;
        try {
            username=getClaimsFromToken(token).get("username").toString();
        }catch (Exception e){
            username=null;
        }
        return username;
    }

    //获得token中的用户权限
    public static List<SimpleGrantedAuthority> getUserRole(String token){
        Claims claims=getClaimsFromToken(token);
        List roles=(List)claims.get("roles");
        String json= JSONArray.toJSONString(roles);
        List<SimpleGrantedAuthority> grantedAuthorityList=JSONArray.parseArray(json,SimpleGrantedAuthority.class);
        return grantedAuthorityList;
    }

    //获得token中的时间
    public static Date getCreatedDateFromToken(String token){
        Date date;
        try {
            final Claims claims=getClaimsFromToken(token);
            date=new Date((Long)claims.get(CLAIM_KEY_CREATED));
        }catch (Exception e)
        {
            date=null;
        }
        return date;
    }

    //从token中获得过期时间
    public static Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    //解析token
    private static Claims getClaimsFromToken(String token){
        try {
            final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//            System.out.println(claims.get("username").toString());
            return claims;
        } catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }

    //生成过期时间
    private static Date generateExpirationDate(){
        //过期事件

        return new Date(System.currentTimeMillis()+ expiration);
    }

    //userDetails中的信息加入到token并调用generateToken生产token
    public static String generateToken(User userDetails){
        Map<String,Object> claims=new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        claims.put(CLAIM_KEY_ROLES,userDetails.getAuthorities());
        return generateToken(claims,userDetails.getUsername());
    }

    //生产token
    public static String generateToken(Map<String,Object> claims,String username){
        return Jwts.builder()
//                .claim("xx","xxx")
                .setSubject(username)
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    //判断是否在是否在过期时间之前
    private static Boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //判断是否可以刷下
    public static Boolean canTokenBeRefreshed(String token){
        return !isTokenExpired(token);
    }

    //刷新token把CLAIM_KEY_CREATED刷新了
    public static String refreshToken(String token){
        String refreshedToken;
        try{
            final Claims claims=getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED,new Date());
            refreshedToken=generateToken(claims,claims.get("username").toString());
        }catch(Exception e){
            refreshedToken=null;
        }
        return refreshedToken;
    }

    //判断token是否过期，是否可用
    public static Boolean validateToken(String token, User userDetails){
        User user=(User) userDetails;
        final String username=getUsernameFromToken(token);
        final Date created=getCreatedDateFromToken(token);
        return username.equals(user.getUsername())&&isTokenExpired(token);
    }

}