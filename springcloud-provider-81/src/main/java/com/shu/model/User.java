package com.shu.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 */
@Data
public class User implements Serializable, UserDetails {
    /**
     *
     */
    private Long userId;


    /**
     *
     */
    private String username;
    /**
     *
     */
    private String userEmail;
    /**
     *
     */
    private String userMobile;
    /**
     *
     */
    private BigDecimal userMoney;
    /**
     *
     */
    private String password;
    /**
     *
     */
    private Date createDate;
    private Date lastLoginTime;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    /**
     *
     */
    private String wechatM1;
    /**
     *
     */
    private String wechatM2;

    private String status;

    private List<GrantedAuthority> authorities=new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }


}