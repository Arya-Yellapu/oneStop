package com.onestop.restaurantSide.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.onestop.restaurantSide.dao.DaoImpl;
import com.onestop.restaurantSide.dao.FinalSlots;
import com.onestop.restaurantSide.dao.LoginEntity;
import com.onestop.restaurantSide.dao.OTPEntity;
import com.onestop.restaurantSide.dao.RestaurantsJDBC;



@Controller
public class HomeControl {

	
	@Value("${spring.mail.username")
	private String fromAddress;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	JavaMailSender sender;
	
	@Autowired
	DaoImpl daoImpl;
	
	public void sendOTP(String mail)
	{
		int otp = (int) ((Math.random()*9000)+1000);
		LocalDateTime current = LocalDateTime.now();
		
		String finalOTP = otp+"="+current.toString();
		
		OTPEntity o = daoImpl.fetchOTP(mail);
		
		if(o==null)
		{
			jdbc.execute("insert into otp(email,otp) values('"+mail+"','"+finalOTP+"')");
			SimpleMailMessage message = new SimpleMailMessage();
			
			message.setFrom(fromAddress);
			message.setTo(mail);
			message.setSubject("OTP For Registration to oneStop");
			message.setText("Dear User"
					+ "\n"
					+"Thank you for using oneStop"+"\n"
					+"Your otp to sucessfully regsiter is "+otp+"\n"
					+"Thanks,"+"\n"
					+"oneStop"
					);
			
			sender.send(message);
		}
		else if(o!=null)
		{
			jdbc.execute("update otp set otp = '"+finalOTP+"' where email = '"+mail+"'");
			
            SimpleMailMessage message = new SimpleMailMessage();
			
			message.setFrom(fromAddress);
			message.setTo(mail);
			message.setSubject("OTP For Registration to oneStop");
			message.setText("Dear User"
					+ "\n"
					+"Thank you for using oneStop"+"\n"
					+"Your otp to sucessfully regsiter is "+otp+"\n"
					+"Thanks,"+"\n"
					+"oneStop"
					);
			
			sender.send(message);
		}
	}

	@RequestMapping("/login")
	public ModelAndView loginPage()
	{
		ModelAndView m1 = new ModelAndView("login");
		return m1;
	}
	
	@RequestMapping("/")
	public ModelAndView home()
	{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		LocalDateTime current = LocalDateTime.now();
		String currentDateTime = current.toString();
		
		String name= "";
		String currentUserMail="";
		if(auth.toString().startsWith("UsernamePasswordAuthenticationToken"))
		{
			String email = auth.getName();
			currentUserMail=email;
			jdbc.execute("update restaurantlogin set lastlogin = '"+currentDateTime+"' where email = '"+email+"'");
			
			LoginEntity le = daoImpl.getLoginData(email);
			name=le.getFirstName();
		}
		else if(auth.toString().startsWith("OAuth2AuthenticationToken"))
		{
			String s = auth.getPrincipal().toString();
			String[] arr = s.split(",");
//			for(String sa : arr)
//			{
//				System.out.println(sa);
//				System.out.println("====");
//			}
		
			String firstName=arr[15].substring(6,10);
			String lastName=arr[17].substring(13);
			String email=arr[19].substring(7,30);
			currentUserMail=email;
			
			name=firstName;
			
			LoginEntity le = daoImpl.getLoginData(email);
			
			if(le==null)
			{
			jdbc.execute("insert into restaurantlogin(email,role,firstName,lastName,lastlogin) values('"+email+"','RESTAURANTUSER','"+firstName+"','"+lastName+"','"+currentDateTime+"')");
			}
			else
			{
				jdbc.execute("update restaurantlogin set lastlogin = '"+currentDateTime+"' where email = '"+email+"'");
			}
		}
		ModelAndView m1 = new ModelAndView("index");
		m1.addObject("mail", currentUserMail);
		m1.addObject("name", name);
		return m1;
	}
	
	@RequestMapping("/addSlots")
	public ModelAndView addSlots(@RequestParam String mail)
	{
		ModelAndView m1 = new ModelAndView("addSlots");
		m1.addObject("mail", mail);
		return m1;
	}
	
	@RequestMapping("/addMoreSlots")
	public ModelAndView addMoreSlots(@RequestParam String mail)
	{
		return addSlots(mail);
	}
	
	@RequestMapping("/saveSlots")
	@Transactional
	public ModelAndView saveSlots(@RequestParam String mail,@RequestParam String resName,@RequestParam String location,@RequestParam String from,@RequestParam String to,@RequestParam String slots, @RequestParam String numberOfSlots, @RequestParam String maxNumber)
	{
		ModelAndView m1 = new ModelAndView("addSlots");
        m1.addObject("mail", mail);
        m1.addObject("message", "Please Enter All the Fields");
        
        if(!mail.isBlank() && !resName.isBlank() && !location.isBlank() && !from.isBlank() && !to.isBlank() && !slots.isBlank())
        {
        	
        	try {
        		LocalDate fromDate = LocalDate.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            	LocalDate toDate = LocalDate.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            	LocalDate today = LocalDate.now();
            	if(fromDate.isBefore(today) || toDate.isBefore(today) || toDate.isBefore(fromDate))
            	{
            		m1 = new ModelAndView("addSlots");
                    m1.addObject("mail", mail);
                    m1.addObject("message", "Please Enter the correct date range");
            	}
            	else {
            		daoImpl.insertData(mail, resName, location, from, to, slots, numberOfSlots, maxNumber);
                	m1=new ModelAndView("postSlotsWindow");
                	m1.addObject("message", "Details Saved Successfully");
                	m1.addObject("mail", mail);
            	}
        		
        	}
        	catch(Exception e)
        	{
        		System.out.println(e.getMessage());
        		m1=new ModelAndView("addSlots");
        		m1.addObject("mail", mail);
        		m1.addObject("message", "Oops Something went wrong, please try again later");
        	}
        	
        }
        return m1;
	}
	
	@RequestMapping("/signup")
	public ModelAndView signup()
	{
		ModelAndView m1 = new ModelAndView("signupform");
		return m1;
	}
	
	@RequestMapping("/register")
	public ModelAndView register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String mobileNumber, @RequestParam String password)
	{
		ModelAndView m1 = new ModelAndView("signupform");
		m1.addObject("message", "Please Enter All the Fields");
		if(!firstName.isBlank() && !lastName.isBlank() && !email.isBlank() && !mobileNumber.isBlank() && !password.isBlank())
		{
			m1 = new ModelAndView("otpScreen");
			m1.addObject("message", "Please Enter the OTP Below");
			m1.addObject("firstName", firstName);
			m1.addObject("lastName", lastName);
			m1.addObject("email", email);
			m1.addObject("mobile", mobileNumber);
			m1.addObject("password", new BCryptPasswordEncoder().encode(password));
			sendOTP(email);
		}
		return m1;
	}
	
	@RequestMapping("/validateOTP")
	@Transactional
	public ModelAndView validateOTP(@RequestParam String otp,@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String mobile, @RequestParam String password)
	{
ModelAndView m1 = null;
		
		OTPEntity o = daoImpl.fetchOTP(email);
		
		String OTP = o.getOtp();
		
		String[] arr = OTP .split("=");
		LocalDateTime otpTime = LocalDateTime.parse(arr[1]);
		LocalDateTime current = LocalDateTime.now();
		
		
		if(current.getMinute()-otpTime.getMinute()<10 && otp.equalsIgnoreCase(arr[0]))
		{
		
		m1=new ModelAndView("login");
		try {
			LoginEntity le = daoImpl.getLoginData(email);
			if(le!=null)
			{
				m1.addObject("message", "User Already Exists, please sign in");
			}
			else if(le==null)
			{
			  daoImpl.insertUser(firstName, lastName, mobile, email, password);
			  m1.addObject("message", "User Data Successfully Added, please sign in");
			  
			}
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			 m1.addObject("message", "Oops something went wrong");
		}
		}
		else
		{
			m1 = new ModelAndView("otpScreen");
			m1.addObject("message", "Recheck the OTP");
			m1.addObject("firstName", firstName);
			m1.addObject("lastName", lastName);
			m1.addObject("mobileNumber", mobile);
			m1.addObject("email", email);
			m1.addObject("password", password);
		}
		return m1;
	}
	
	@RequestMapping("/resendOTP")
	public ModelAndView resendOTP(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String mobile,@RequestParam String email,@RequestParam String password)
	{
		ModelAndView m1 = new ModelAndView("otpScreen");
		m1.addObject("message", "OTP resent successfully");
		m1.addObject("firstName", firstName);
		m1.addObject("lastName", lastName);
		m1.addObject("mobileNumber", mobile);
		m1.addObject("email", email);
		m1.addObject("password", password);
		
		sendOTP(email);
	
		return m1;
	}
	
	@RequestMapping("/viewRestaurants")
	public ModelAndView getMyRestaurants(@RequestParam String mail)
	{
		ModelAndView m1 = null;
		List<RestaurantsJDBC> list = daoImpl.getRestaurantsForUser(mail);
		if(list.isEmpty())
		{
			m1 = new ModelAndView("empty");
		}
		else
		{
			m1 = new ModelAndView("notEmpty");
			m1.addObject("mail", mail);
			m1.addObject("restaurantList", list);
		}
		return m1;
	}
	
	@RequestMapping("/getBookingsForRestaurant")
	public ModelAndView getBookingsForRestaurant(@RequestParam String restaurantId, @RequestParam String mail)
	{
		ModelAndView m1=null;
		List<FinalSlots> list = daoImpl.getBookingsForRestaurant(restaurantId);
		if(!list.isEmpty())
		{
	    m1 = new ModelAndView("bookings");
		m1.addObject("finalList", list);
		}
		else
		{
			List<RestaurantsJDBC> ls = daoImpl.getRestaurantsForUser(mail);
			m1 = new ModelAndView("emptyBookings");
			m1.addObject("mail", mail);
			m1.addObject("restaurantList", ls);
			m1.addObject("MESSAGE", "Sorry There are no Bookings for that Restaurant");
		}
		return m1;
	}
}
