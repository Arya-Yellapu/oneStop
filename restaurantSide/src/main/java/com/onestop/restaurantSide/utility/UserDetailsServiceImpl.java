package com.onestop.restaurantSide.utility;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onestop.restaurantSide.dao.DaoImpl;
import com.onestop.restaurantSide.dao.LoginEntity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	DaoImpl daoImpl;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginEntity le = daoImpl.getLoginData(username);
		if(le==null)
		{
			throw new UsernameNotFoundException("User "+username+" not found");
		}
		return new UserPrincipal(le);
	}

}
