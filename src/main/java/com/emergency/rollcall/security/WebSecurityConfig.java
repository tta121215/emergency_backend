package com.emergency.rollcall.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Entry points
		http.authorizeRequests().antMatchers("/api/user/login").permitAll().antMatchers("/api/customer/login")
				.permitAll().antMatchers("/assembly").permitAll()
				.antMatchers("/appuser/login").permitAll().antMatchers("/assembly/update").permitAll()
				.antMatchers("/assembly/delete").permitAll().antMatchers("/assembly/assembly-list").permitAll()
				.antMatchers("/emergency").permitAll().antMatchers("/emergency/delete").permitAll()
				.antMatchers("/emergency/emergency-list").permitAll()
				.antMatchers("/condition").permitAll().antMatchers("/condition/update").permitAll()
				.antMatchers("/condition/condition-list").permitAll().antMatchers("/condition/delete").permitAll()
				.antMatchers("/noti").permitAll().antMatchers("/noti/update").permitAll()
				.antMatchers("/noti/noti-list").permitAll().antMatchers("/noti/delete").permitAll()
				.antMatchers("/noti/search-by-params").permitAll().antMatchers("/condition/search-by-params").permitAll()
				.antMatchers("/api/customer/seller-register").permitAll().antMatchers("/api/customer/seller-login")
				.permitAll()
				// Disallow everything else..
				.anyRequest().authenticated();

		// If a user try to access a resource without having enough permissions
		http.exceptionHandling().accessDeniedPage("/login");

		// Apply JWT
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

		// Optional, if you want to test the API from a browser
		// http.httpBasic();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
}
