package com.onestop.restaurantSide.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;





@Repository
public class DaoImpl {

	@PersistenceContext
	EntityManager em;
	
	public LoginEntity getLoginData(String email)
	{
		LoginEntity le = em.find(LoginEntity.class, email);
		return le;
	}
	
	public OTPEntity fetchOTP(String email)
	{
		OTPEntity e=em.find(OTPEntity.class, email);
		return e;
	}
	
	public void insertUser(String firstName, String lastName, String mobileNumber, String email,String password)
	{
		LoginEntity le = new LoginEntity();
		le.setEmail(email);
		le.setFirstName(firstName);
		le.setLastName(lastName);
		le.setMobile("+91"+mobileNumber);
		//le.setPassword(new BCryptPasswordEncoder().encode(password));
		le.setPassword(password);
		le.setRole("RESTAURANTUSER");

		em.persist(le);
	}
}
