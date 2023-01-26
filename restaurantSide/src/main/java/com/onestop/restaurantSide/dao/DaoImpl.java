package com.onestop.restaurantSide.dao;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;





@Repository
public class DaoImpl {
	
	@Autowired
	JdbcTemplate temp;

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
	
	public void insertData(String mail, String resName, String location, String from, String to, String slots, String numberOfSlots, String maxNumber)
	{
		LoginEntity le = getLoginData(mail);
		
		List<SlotsEntity> slotsList = new LinkedList<>();
		String[] slotsArr=slots.split(",");
		LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		Stream<LocalDate> datesRange=fromDate.datesUntil(toDate);
		datesRange.forEach(n->{
			for(String s:slotsArr)
			{
				SlotsEntity se = new SlotsEntity();
				se.setDate(Date.valueOf(n));
				se.setSlot(s);
				se.setSlotsnumber(Integer.valueOf(numberOfSlots));
				se.setMaxNumber(Integer.valueOf(maxNumber));
				slotsList.add(se);
			}
		});
		Date toDa = Date.valueOf(to);
		for(String s:slotsArr)
		{
			SlotsEntity se = new SlotsEntity();
			se.setDate(toDa);
			se.setSlot(s);
			se.setSlotsnumber(Integer.valueOf(numberOfSlots));
			se.setMaxNumber(Integer.valueOf(maxNumber));
			slotsList.add(se);
		}
		
		RestaurantEntity re= new RestaurantEntity();
		re.setRestaurantName(resName);
		re.setLocation(location);
		re.setLoginEntity(le);
		re.setSlotsList(slotsList);
		
		em.persist(re);
	}
	
	public List<RestaurantsJDBC> getRestaurantsForUser(String mail)
	{
		String sql = "select * from restaurants where email = ?";
		List<RestaurantsJDBC> list = temp.query(sql, new Object[]{mail}, new RestaurantsJDBCRowMapper());
		return list;
	}
	
	public List<FinalSlots> getBookingsForRestaurant(String resId)
	{
		List<FinalSlots> finalList = new LinkedList<>();
		
		String sql = "select * from slots where res_id = ?";
		List<SlotsJDBC> slotsList = temp.query(sql, new Object[] {Integer.valueOf(resId)}, new SlotsJDBCRowMapper());
		
		List<List<ReservationJDBC>> resList = new LinkedList<>();
		
		for(SlotsJDBC s: slotsList)
		{
			
			String sql1 = "select * from reservation where slotid = ?";
			List<ReservationJDBC> ls = temp.query(sql1, new Object[]{s.getId()}, new ReservationJDBCRowMapper());
			if(!ls.isEmpty())
			{
				
				for(ReservationJDBC res : ls)
				{
					FinalSlots f = new FinalSlots();
					f.setDate_field(s.getDate_field());
					f.setSlot(s.getSlot());
					f.setSlotsnumber(s.getSlotsnumber());
					f.setMaxnumber(s.getMaxnumber());
					
					f.setReservationId(res.getReservationid());
					f.setSlotsstatus(res.getSlotsstatus());
					f.setMailId(res.getMailid());
					
					finalList.add(f);
				}
			}
		}
		return finalList;
	}
}
