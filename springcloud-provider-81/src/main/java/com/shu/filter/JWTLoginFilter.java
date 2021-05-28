package com.shu.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.dao.UserDAO;
import com.shu.model.User;
import com.shu.utils.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;


public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private static String url="/toLogin";

    @Autowired
    private UserDAO userDAO;

    public static void setUrl(String url) {
        JWTLoginFilter.url = url;
    }

    public JWTLoginFilter(AuthenticationManager authenticationManager){
        this.authenticationManager=authenticationManager;
        //设置放行接口做登录
        super.setFilterProcessesUrl(url);

    }

    //登录时调用的方法
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        //调用我们MemberUserDetailsService 账号密码登录

//            User user=new ObjectMapper()
//                    .readValue(request.getInputStream(),User.class);
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password,
                            new ArrayList<>()
                    )
            );

    }

    /*
     * @Author: jameszhang
     * @Description:账号和密码验证成功
     * @param request :
     * @param response :
     * @param chain :
     * @param authResult :
     * @return: void
     **/
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //账号和密码验证成功，在响应头中响应jwt
        User user = (User)authResult.getPrincipal();
        String s = Jwt.generateToken(user);
        System.out.println("token-->"+s);
        response.addHeader("Access-Control-Expose-Headers", "token");
        response.addHeader("Access-Control-Expose-Headers", "username");
        response.addHeader("token",s);
        response.addHeader("username",user.getUsername());



    }

    /*
     * @Author: jameszhang
     * @Description:账号和密码验证失败
     * @param request :
     * @param response :
     * @param failed :
     * @return: void
     **/
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
        response.setStatus(401);
    }
}
