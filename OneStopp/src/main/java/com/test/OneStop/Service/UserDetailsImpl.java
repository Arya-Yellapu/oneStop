package com.test.OneStop.Service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.test.OneStop.Dao.LoginEntity;



public class UserDetailsImpl implements UserDetails {
	
	private LoginEntity le;
	
	

	public UserDetailsImpl(LoginEntity le) {
		super();
		this.le = le;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(le.getRole()));
	}

	@Override
	public String getPassword() {
		return le.getPassword();
	}

	@Override
	public String getUsername() {
		return le.getEmail();
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

