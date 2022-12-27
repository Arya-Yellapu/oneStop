package com.onestop.restaurantSide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig{

	@Autowired
	UserDetailsService usd;
	
	@Bean
	public AuthenticationProvider getProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(usd);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		
		return provider;
	}
	
	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
		 .authorizeRequests().antMatchers("/login","/signup","/register","/validateOTP","/resendOTP").permitAll()
		 .anyRequest().authenticated()
		 .and()
		 .formLogin().loginPage("/login").usernameParameter("email").permitAll()
		 .and()
		 .logout().invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID").permitAll();
		
		return http.build();
		
	}
}
