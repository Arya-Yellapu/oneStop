package com.test.OneStop.Service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.test.OneStop.Dao.LoginDaoImpl;
import com.test.OneStop.Dao.LoginEntity;



@Service
public class UserDetailsSeviceImpl implements UserDetailsService{
	
	@Autowired
	LoginDaoImpl daoImpl;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginEntity le = daoImpl.findUserByEmail(username);
		
		if(le==null)
		{
			throw new UsernameNotFoundException("User "+username+" not found");
		}
		return new UserDetailsImpl(le);
	}

}
