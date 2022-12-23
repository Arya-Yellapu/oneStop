package com.test.OneStop.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsService usd;
	
	@Bean
	public AuthenticationProvider authProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(new BCryptPasswordEncoder());
		provider.setUserDetailsService(usd);
		
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		     .csrf().disable()
		     .authorizeRequests().antMatchers("/login","/register","/saveRegistrationDetails","/otpVerification","/resendOTP").permitAll()
		     .anyRequest().authenticated()
		     .and()
		     .formLogin().loginPage("/login").usernameParameter("email").permitAll()
		     .and()
		     .oauth2Login().loginPage("/login").permitAll()
		     .and()
		     .logout()
		     .invalidateHttpSession(true)
		     .clearAuthentication(true)
		     .deleteCookies("JSESSSIONID")
		     .logoutSuccessUrl("/login");

	}
}
