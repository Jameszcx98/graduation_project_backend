package com.shu.filter;

import com.mysql.cj.util.StringUtils;
import com.shu.utils.Jwt;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JWTValidationFilter extends BasicAuthenticationFilter {
    public JWTValidationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    /*
     * @Author: jameszhang
     * @Description:拦截请求
     * @param request :
     * @param response :
     * @param chain :
     * @return: void
     **/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        //获取token的权限列表
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(request.getHeader("token")));
        //放行
        super.doFilterInternal(request,response,chain);

    }
    private UsernamePasswordAuthenticationToken getAuthentication(String token){
        String username= Jwt.getUsernameFromToken(token);
        if(!StringUtils.isEmptyOrWhitespaceOnly(username)){
            //解析权限列表
            List<SimpleGrantedAuthority> userRoleList = Jwt.getUserRole(token);
            return new UsernamePasswordAuthenticationToken(username,null,userRoleList);
        }
        return null;


    }
}
