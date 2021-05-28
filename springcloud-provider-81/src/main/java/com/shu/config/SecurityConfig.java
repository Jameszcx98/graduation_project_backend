package com.shu.config;

import com.shu.dao.PermissionDAO;
import com.shu.filter.JWTLoginFilter;
import com.shu.filter.JWTValidationFilter;
import com.shu.service.impl.UserServiceImpl;
import com.shu.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImpl userService;


    /*
     * @Author: jameszhang
     * @Description: 授权
     * @param http :
     * @return: void
     **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * 首页所有人可以访问，功能页只有对应有权限的人才能去访问
         * 采用链式变成法则，antMatchers匹配相应的url，hasrole是可以访问的用户身份
         **/
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST,"/email").permitAll()
                .antMatchers(HttpMethod.GET,"/activate").permitAll()
                .antMatchers(HttpMethod.POST,"/").permitAll()
                .antMatchers(HttpMethod.POST,"/toLogin").permitAll()
                .anyRequest().fullyAuthenticated()
//                .antMatchers("/**").hasRole("customer")
                .and()
                .addFilter(new JWTValidationFilter(authenticationManager()))
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors();


        /*
         * 没有权限就默认转跳到登录页面，springsecurity自带的
         **/
//        http.formLogin().usernameParameter("username").passwordParameter("password").loginPage("/toLogin");
//
//        http.logout();
    }

    /*
     * @Author: jameszhang
     * @Description:认证
     * @param auth :
     * @return: void
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
        * 如果用户是zcx，密码是123456，赋予其vip2，vip3的权限，让其进入之后的授权阶段。数据从数据库中读取
        * 需要passwordEncoder，明文密码不让用
        * */
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("zcx").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
//                .and()
//                .withUser("root").password(new BCryptPasswordEncoder().encode("123456")).roles("vip1","vip2","vip3");



        auth.userDetailsService(userService).passwordEncoder(new PasswordEncoder() {
            /*
             * @Author: jameszhang
             * @Description:对用户输入的密码进行MD5加密
             * @param charSequence :
             * @return: java.lang.String
             **/
            @Override
            public String encode(CharSequence charSequence) {
                return MD5Util.getMD5((String) charSequence);
            }

            /*
             * @Author: jameszhang
             * @Description:判断输入的密码是否正确
             * @param charSequence :用户输入的密码
             * @param s :数据库中的密码
             * @return: boolean
             **/
            @Override
            public boolean matches(CharSequence charSequence, String s) {
                String rawPass= MD5Util.getMD5((String) charSequence);
                boolean res=rawPass.equals(s);
                return res;
            }
        });


    }
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}

