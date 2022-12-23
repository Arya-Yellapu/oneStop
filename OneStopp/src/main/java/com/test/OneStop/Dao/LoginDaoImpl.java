package com.test.OneStop.Dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;



@Repository
public class LoginDaoImpl {

	@PersistenceContext
	EntityManager em;
	
	Logger logger =LoggerFactory.getLogger(this.getClass());
	
	public LoginEntity findUserByEmail(String email)
	{
		LoginEntity le = em.find(LoginEntity.class, email);
		if(le==null)
		{
			logger.error("User "+email+" not found");
		}
		return le;
	}
	
	public void insertUser(String firstName, String lastName, String mobileNumber, String email,String password)
	{
		LoginEntity le = new LoginEntity();
		le.setEmail(email);
		le.setFirstname(firstName);
		le.setLastname(lastName);
		le.setMobile("+91"+mobileNumber);
		//le.setPassword(new BCryptPasswordEncoder().encode(password));
		le.setPassword(password);
		le.setRole("USER");

		em.persist(le);
	}
	
	public OTPEntity fetchOTP(String email)
	{
		OTPEntity e=em.find(OTPEntity.class, email);
		return e;
	}
	
	public List<RestaurantEntity> getRestaurantsByLocation(String location)
	{
		Query query = em.createQuery("select re from RestaurantEntity re where re.location =?1");
		query.setParameter(1, location);
		
		List<RestaurantEntity> resList = query.getResultList();
	    return resList;
	}
	
	public List<Slots> getRestaurantSlots(String id)
	{
		RestaurantEntity re = em.find(RestaurantEntity.class, Integer.valueOf(id));
		
		List<Slots> slotsList = re.getSlotsList();
		List<Slots> finalSlotsList = new LinkedList<>();
		slotsList.stream().filter(n->n.getStatus().equalsIgnoreCase("Available")).forEach(n->{
			finalSlotsList.add(n);
		});
		
		return finalSlotsList;
	}
}

