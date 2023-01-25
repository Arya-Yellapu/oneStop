package com.test.OneStop.Dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



@Repository
public class LoginDaoImpl {
	
	@Autowired
	JdbcTemplate temp;

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
		Iterator<RestaurantEntity> resIterator = resList.iterator();
		RestaurantEntity re = null;
		while(resIterator.hasNext())
		{
			re=resIterator.next();
			int flag=0;
			List<Slots> slotsList = re.getSlotsList();
			int slotsListSize=slotsList.size();
			for(Slots s:slotsList)
			{
				if(s.getSlotsnumber()==0)
				{
					flag++;
				}
			}
			if(flag==slotsListSize)
			{
				resIterator.remove();
			}
		}
	    return resList;
	}
	
	public List<Slots> getRestaurantSlots(String id)
	{
		RestaurantEntity re = em.find(RestaurantEntity.class, Integer.valueOf(id));
		
		List<Slots> slotsList = re.getSlotsList();
		List<Slots> finalSlotsList = new LinkedList<>();
		slotsList.stream().filter(n->n.getSlotsnumber()!=0).forEach(n->{
			finalSlotsList.add(n);
		});
		
		return finalSlotsList;
	}
	
	public void updateSlot(String reservationId, String date, String time, int resId, String mailId)
	{
		RestaurantEntity re = em.find(RestaurantEntity.class, resId);
		Slots se = new Slots();
		for(Slots s : re.getSlotsList())
		{
			if(s.getDate().equals(Date.valueOf(date)) && s.getSlot().equalsIgnoreCase(time))
			{
				se=s;
			}
		}
		LoginEntity le = findUserByEmail(mailId);
		
		ReservationEntity rse = new ReservationEntity();
		rse.setLoginEntity(le);
		rse.setReservationid(reservationId);
		rse.setSlotEntity(se);
		rse.setSlotsstatus("Booked");
		
		se.setSlotsnumber(se.getSlotsnumber()-1);
		em.persist(se);
		em.persist(rse);
		
	}
	
	public List<FinalSlots> getSlots(String mail)
	{
		List<FinalSlots> list = new LinkedList<>();
		
		String sql = "select * from reservation where mailid=?";
		String sql1 = "select * from slots where id=?";
		String sql2 = "select * from restaurants where id=?";
		List<ReservationJDBC> res = temp.query(sql,new Object[] {mail}, new ReservationJDBCRowMapper());
		if(res!=null)
		{
		for(ReservationJDBC r : res)
		{
		    FinalSlots f = new FinalSlots();
		    f.setReservationId(r.getReservationid());
		    f.setSlotId(r.getSlotid());
		    f.setSlotsstatus(r.getSlotsstatus());
		    
			SlotsJDBC slot = temp.queryForObject(sql1, new Object[] {r.getSlotid()}, new SlotsJDBCRowMapper());
			if(slot!=null)
			{
				f.setDate_field(slot.getDate_field());
				f.setMaxnumber(slot.getMaxnumber());
				f.setSlot(slot.getSlot());
				f.setSlotId(slot.getId());
				f.setSlotsnumber(slot.getSlotsnumber());
				
				RestaurantsJDBC rest = temp.queryForObject(sql2,new Object[] {slot.getRes_id()}, new RestaurantsJDBCRowMapper());
				
				f.setRestaurantId(slot.getRes_id());
				f.setRestaurantName(rest.getRestaurant_name());
				f.setLocation(rest.getLocation());
				
				list.add(f);
			}
		}
		}
		return list;
	}
	
	public void cancelSlot(String reservationId, String slotId)
	{
		String sql1="update reservation set slotsstatus = 'Cancelled' where reservationid = ?";
		temp.update(sql1, reservationId);
		
		String sql2="update slots set slotsnumber = (select slotsnumber from slots where id = ?)+1 where id = ?;";
		temp.update(sql2, Integer.valueOf(slotId), Integer.valueOf(slotId));
	}
}

