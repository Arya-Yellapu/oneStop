package com.onestop.restaurantSide.controller;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.onestop.restaurantSide.dao.DaoImpl;
import com.onestop.restaurantSide.dao.LoginEntity;
import com.onestop.restaurantSide.dao.OTPEntity;



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
		ModelAndView m1 = new ModelAndView("index");
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
}
