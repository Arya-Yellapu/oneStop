	package com.test.OneStop.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.test.OneStop.Dao.FinalSlots;
import com.test.OneStop.Dao.LoginDaoImpl;
import com.test.OneStop.Dao.LoginEntity;
import com.test.OneStop.Dao.OTPEntity;
import com.test.OneStop.Dao.RestaurantEntity;
import com.test.OneStop.Dao.Slots;


@Controller
public class HomeContoller {
	
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	private static final List<String> SCOPES =
		      Collections.singletonList(CalendarScopes.CALENDAR);
		  private static final String CREDENTIALS_FILE_PATH = "C:/Users/lenovo/Downloads/oneStopCredentials.json";
		  private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
			      throws IOException {
			    // Load client secrets.
			    InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
			    if (in == null) {
			      throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
			    }
			    GoogleClientSecrets clientSecrets =
			        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			    // Build flow and trigger user authorization request.
			    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
			        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
			        .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
			        .setAccessType("offline")
			        .build();
			    LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
			    Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
			    //returns an authorized Credential object.
			    return credential;
			  }
	
	public static int counter = 001;
	
	@Value("${spring.mail.username}")
	private String mailer;
	
	@Value("${server.port}")
	private String port;
	
	@Autowired
	JavaMailSender sender;
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	LoginDaoImpl daoImpl;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/login")
	public ModelAndView loginPage()
	{
		ModelAndView m1 = new ModelAndView("login");
		return m1;
	}
	
	@RequestMapping("/register")
	public ModelAndView registrationPage()
	{
		ModelAndView m1 = new ModelAndView("register");
		return m1;
	}
	
	public void sendOTP(String email)
	{
		int otp = (int) ((Math.random()*9000)+1000);
		LocalDateTime current = LocalDateTime.now();
		
		String finalOTP = otp+"="+current.toString();
		
		OTPEntity o = daoImpl.fetchOTP(email);
		
		if(o==null)
		{
			jdbc.execute("insert into otp(email,otp) values('"+email+"','"+finalOTP+"')");
			SimpleMailMessage message = new SimpleMailMessage();
			
			message.setFrom(mailer);
			message.setTo(email);
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
			jdbc.execute("update otp set otp = '"+finalOTP+"' where email = '"+email+"'");
			
            SimpleMailMessage message = new SimpleMailMessage();
			
			message.setFrom(mailer);
			message.setTo(email);
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
	
	
	@RequestMapping("/saveRegistrationDetails")
	@Transactional
	public ModelAndView saveRegistrationDetails(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String mobileNumber,@RequestParam String email,@RequestParam String password) 
	{
		
		ModelAndView m1 = new ModelAndView("register");
		m1.addObject("MESSAGE", "Please Enter Valid Details");
		if(!firstName.isBlank() && !lastName.isBlank() && !email.isBlank() && !mobileNumber.isBlank() && mobileNumber.length()==10 && !password.isBlank())
		{
			m1= new ModelAndView("otpScreen");
			m1.addObject("message", "Please Enter the OTP Below");
			m1.addObject("firstName", firstName);
			m1.addObject("lastName", lastName);
			m1.addObject("mobileNumber", mobileNumber);
			m1.addObject("email", email);
			//m1.addObject("password", password);
            m1.addObject("password", new BCryptPasswordEncoder().encode(password));
			sendOTP(email);

		}
		return m1;
	}
	
	@RequestMapping("/resendOTP")
	public ModelAndView resendOTP(@RequestParam String firstName,@RequestParam String lastName,@RequestParam String mobileNumber,@RequestParam String email,@RequestParam String password)
	{
		ModelAndView m1 = new ModelAndView("otpScreen");
		m1.addObject("message", "OTP resent successfully");
		m1.addObject("firstName", firstName);
		m1.addObject("lastName", lastName);
		m1.addObject("mobileNumber", mobileNumber);
		m1.addObject("email", email);
		m1.addObject("password", password);
		
		sendOTP(email);
	
		return m1;
	}
	
	@RequestMapping("/otpVerification")
	@Transactional
	public ModelAndView verifyUser(@RequestParam String otp,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String mobileNumber,@RequestParam String email,@RequestParam String password)
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
			LoginEntity le = daoImpl.findUserByEmail(email);
			if(le!=null)
			{
				m1.addObject("message", "User Already Exists, please sign in");
			}
			else if(le==null)
			{
			  daoImpl.insertUser(firstName, lastName, mobileNumber, email, password);
			  m1.addObject("message", "User Data Successfully Added, please sign in");
			  
			  logger.info(email+" has been added successfully");
			}
		}
		
		catch(Exception e)
		{
			logger.error(e.getMessage());
			 m1.addObject("message", "Oops something went wrong");
		}
		}
		else
		{
			m1 = new ModelAndView("otpScreen");
			m1.addObject("message", "Recheck the OTP");
			m1.addObject("firstName", firstName);
			m1.addObject("lastName", lastName);
			m1.addObject("mobileNumber", mobileNumber);
			m1.addObject("email", email);
			m1.addObject("password", password);
		}
		return m1;
	}
	
	@RequestMapping("/")
	@Transactional
	public ModelAndView homePage()
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
			jdbc.execute("update login set lastlogin = '"+currentDateTime+"' where email = '"+email+"'");
			
			LoginEntity le = daoImpl.findUserByEmail(email);
			name=le.getFirstname();
		}
		else if(auth.toString().startsWith("OAuth2AuthenticationToken"))
		{
			String s = auth.getPrincipal().toString();
			String[] arr = s.split(",");
		
			String firstName=arr[8].substring(12);
			String lastName=arr[9].substring(13);
			String email=arr[11].substring(7);
			currentUserMail=email;
			
			name=firstName;
			
			LoginEntity le = daoImpl.findUserByEmail(email);
			
			if(le==null)
			{
			jdbc.execute("insert into login(email,oauthprovider,role,firstName,lastName,lastlogin) values('"+email+"','google','USER','"+firstName+"','"+lastName+"','"+currentDateTime+"')");
			}
			else
			{
				jdbc.execute("update login set lastlogin = '"+currentDateTime+"' where email = '"+email+"'");
			}
		}
		
    	ModelAndView m1 = new ModelAndView("index");
    	m1.addObject("currentUser", name);
    	m1.addObject("currentUserMail", currentUserMail);
    	m1.addObject("port", port);
    	
		return m1;
	}
	
	@RequestMapping("/location")
	@Transactional
	public ModelAndView getRestaurantsByLocation(@RequestParam String currentUser,@RequestParam String currentUserMail,@RequestParam String location)
	{
		ModelAndView m1 = null;
		List<RestaurantEntity> resList = daoImpl.getRestaurantsByLocation(location);
		if(resList.isEmpty())
		{
			m1 = new ModelAndView("index");
			m1.addObject("currentUser", currentUser);
	    	m1.addObject("currentUserMail", currentUserMail);
	    	m1.addObject("message", "There are no restuarants available in this location, please try again");
		}
		else
		{
			m1 = new ModelAndView("restaurantsByLocation");
			m1.addObject("resList", resList);
			m1.addObject("currentUser", currentUser);
			m1.addObject("currentUserMail", currentUserMail);
			m1.addObject("location", location);
		}
		return m1;
	}
	
	@RequestMapping("/selectedLocation")
	@Transactional
	public ModelAndView getRestaurantSlots(@RequestParam(required=false) String currentResId,@RequestParam String currentUser,@RequestParam String currentUserMail,@RequestParam String location)
	{
		List<RestaurantEntity> ressList = new LinkedList<>();
		
		ModelAndView m1 = null;
		String arr[] = currentResId.split(",");
		if(arr.length>1)
		{
			
			String[] userArr=currentUser.split(",");
			String[] userMailArr=currentUserMail.split(",");
			String[] locationArr=location.split(",");
			
			ressList = daoImpl.getRestaurantsByLocation(locationArr[0]);
			
			m1 = new ModelAndView("restaurantsByLocation");
			m1.addObject("currentUser", userArr[0]);
	    	m1.addObject("currentUserMail", userMailArr[0]);
	    	m1.addObject("resList", ressList);
	    	m1.addObject("location", locationArr[0]);
	    	m1.addObject("message", "Please select only one restaurant");
		}
		else
		{
		List<Slots> resList = daoImpl.getRestaurantSlots(currentResId);
		
//		if(resList.isEmpty())
//		{
//			String[] userArr=currentUser.split(",");
//			String[] userMailArr=currentUserMail.split(",");
//			String[] locationArr=location.split(",");
//			
//			ressList = daoImpl.getRestaurantsByLocation(locationArr[0]);
//			
//			m1 = new ModelAndView("restaurantsByLocation");
//			m1.addObject("currentUser", userArr[0]);
//	    	m1.addObject("currentUserMail", userMailArr[0]);
//	    	m1.addObject("resList", ressList);
//	    	m1.addObject("location", locationArr[0]);
//	    	m1.addObject("message", "Sorry there are no slots available at this moment for this restaurant, please try another restaurant");
//		}
//		else
//		{
			m1 = new ModelAndView("restaurantSlots");
			m1.addObject("resList", resList);
			m1.addObject("currentUser", currentUser);
			m1.addObject("currentUserMail", currentUserMail);
			m1.addObject("location", location);
			m1.addObject("currentResId", currentResId);
		//}
		}
		return m1;
	}
	
	@RequestMapping("/selectSlots")
	@Transactional
	public ModelAndView confirmRestaurantSlots(@RequestParam(required=false) String currentSlot,@RequestParam String currentUser,@RequestParam String currentUserMail,@RequestParam String location,@RequestParam String currentResId) throws IOException, GeneralSecurityException
	{
		
		ModelAndView m1 = null;
		String arr[] = currentSlot.split(",");
		if(arr.length>1)
		{
			String[] sArr = currentResId.split(",");
			List<Slots> ressList = daoImpl.getRestaurantSlots(sArr[0]);
			
			
			m1 = new ModelAndView("restaurantSlots");
			m1.addObject("resList", ressList);
			m1.addObject("currentUser", currentUser);
			m1.addObject("currentUserMail", currentUserMail);
			m1.addObject("location", location);
			m1.addObject("currentResId", currentResId);
	    	m1.addObject("message", "Please select only one slot");
		}
		else
		{
	    String[] sCurrent = currentUser.split(",");
		String[] sArr = currentResId.split(",");
		String[] sMailArr = currentUserMail.split(",");
		String[] sLocationArr=location.split(",");
		
		String[] slotArray = currentSlot.split("/");
        String date = slotArray[0];
        String time = slotArray[1];

        String reservationId="oneStop"+counter++;
        
        //jdbc.execute("update slots set reservationid = '"+reservationId+"',status = 'Booked' where date_field = '"+date +"' and slot = '"+time+"' and res_id = '"+Integer.valueOf(sArr[0])+"'");
        daoImpl.updateSlot(reservationId, date, time, Integer.valueOf(sArr[0]), sMailArr[0]);
        
        String resName = jdbc.queryForObject("select restaurant_name from restaurants where id = "+Integer.valueOf(sArr[0]), String.class);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailer);
        message.setTo(sMailArr[0]);
        message.setSubject("Reservation Confirmation "+reservationId);
        message.setText("Your Reservation has been confirmed at "+resName+" at "+date+" and at "+time+" hours");
        
        sender.send(message);
        
        String oauthprovider = "";
        LoginEntity le = daoImpl.findUserByEmail(sMailArr[0]);
        if(le!=null)
        {
        	oauthprovider=le.getEmail();
        }
        if(oauthprovider!=null && le.getOauthprovider().equalsIgnoreCase("google"))
        {
        
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service =
            new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        

     Event event = new Event()
         .setSummary("Reservation Confirmation "+reservationId)
         .setLocation(sLocationArr[0]);
     
 	DateTime startDateTime = new DateTime("2022-12-28T09:00:00-07:00");
	EventDateTime start = new EventDateTime()
	    .setDateTime(startDateTime)
	    .setTimeZone("America/Los_Angeles");
	event.setStart(start);

	DateTime endDateTime = new DateTime("2022-12-28T17:00:00-07:00");
	EventDateTime end = new EventDateTime()
	    .setDateTime(endDateTime)
	    .setTimeZone("America/Los_Angeles");
	event.setEnd(end);

//     DateTime startDateTime = new DateTime(date+"T"+time);
//     EventDateTime start = new EventDateTime()
//         .setDateTime(startDateTime)
//         .setTimeZone("India");
//     event.setStart(start);

//     DateTime endDateTime = startDateTime.
//     EventDateTime end = new EventDateTime()
//         .setDateTime(endDateTime)
//         .setTimeZone("America/Los_Angeles");
//     event.setEnd(end);

     String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
     event.setRecurrence(Arrays.asList(recurrence));

     EventAttendee[] attendees = new EventAttendee[] {
         new EventAttendee().setEmail(oauthprovider)
     };
     event.setAttendees(Arrays.asList(attendees));

     EventReminder[] reminderOverrides = new EventReminder[] {
         new EventReminder().setMethod("email").setMinutes(24 * 60),
         new EventReminder().setMethod("popup").setMinutes(10),
     };
     Event.Reminders reminders = new Event.Reminders()
         .setUseDefault(false)
         .setOverrides(Arrays.asList(reminderOverrides));
     event.setReminders(reminders);

     String calendarId = "primary";
     event = service.events().insert(calendarId, event).execute();
     System.out.printf("Event created: %s\n", event.getHtmlLink());
        }
        
        m1=new ModelAndView("confirmation");
        m1.addObject("reservationid", reservationId);
        m1.addObject("currentUser", sCurrent[0]);
        m1.addObject("currentUserMail", sMailArr[0]);
        m1.addObject("port", port);
        
		}
		return m1;
	}
	
	@RequestMapping("/bookMore")
	public ModelAndView bookMore(@RequestParam String currentUser, @RequestParam String currentUserMail)
	{
		return homePage();
	}
	
	@RequestMapping("/getReservedSlots")
	@Transactional
	public ModelAndView getReservedSlots(@RequestParam String currentUser, @RequestParam String currentUserMail)
	{
		ModelAndView m1 = null;
		List<FinalSlots> list =daoImpl.getSlots(currentUserMail);
		if(!list.isEmpty())
		{
		m1 = new ModelAndView("bookedSlots");
		m1.addObject("finalSlots", list);
		m1.addObject("port", port);
		m1.addObject("currentUser", currentUser);
		m1.addObject("currentUserMail", currentUserMail);
		}
		else
		{
			m1=new ModelAndView("emptySlots");
			m1.addObject("port", port);
		}
		return m1;
	}
	
	@RequestMapping("/cancelSlot")
	@Transactional
	public ModelAndView cancelSlot(@RequestParam String currentUser, @RequestParam String currentUserMail, @RequestParam String reservationId, @RequestParam String slotId)
	{
		daoImpl.cancelSlot(reservationId, slotId);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(currentUserMail);
		message.setSubject("Cancellation Of Reservation "+reservationId);
		message.setText("Your Reservation "+reservationId+" has been cancelled");
		message.setFrom(mailer);
		sender.send(message);
		return getReservedSlots(currentUser,currentUserMail);
	}
	
}

