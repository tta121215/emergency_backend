package com.emergency.rollcall.security;

import java.util.Arrays;

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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
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

//		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Entry points
		http.authorizeRequests().antMatchers("/api/user/login").permitAll().antMatchers("/api/customer/login")
				.permitAll().antMatchers("/assembly").permitAll()
				.antMatchers("/appuser/login").permitAll()
				.antMatchers("/assembly").permitAll().antMatchers("/assembly/**").permitAll()			
				.antMatchers("/emergency").permitAll().antMatchers("/emergency/**").permitAll()		
				.antMatchers("/condition").permitAll().antMatchers("/condition/**").permitAll()
				.antMatchers("/route").permitAll().antMatchers("/route/**").permitAll()
				.antMatchers("/noti").permitAll().antMatchers("/noti/**").permitAll()
				.antMatchers("/modenoti").permitAll().antMatchers("/modenoti/**").permitAll()
				.antMatchers("/renoti").permitAll().antMatchers("/renoti/**").permitAll()		
				.antMatchers("/locemergency").permitAll().antMatchers("/locemergency/**").permitAll()
				.antMatchers("/eactivate").permitAll().antMatchers("/eactivate/**").permitAll()
				.antMatchers("/asscheckin").permitAll().antMatchers("/asscheckin/**").permitAll()
				// Disallow everything else..
				.anyRequest().authenticated();

		// If a user try to access a resource without having enough permissions
	//	http.exceptionHandling().accessDeniedPage("/login");

		// Apply JWT
	//	http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

		// Optional, if you want to test the API from a browser
		// http.httpBasic();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type",
        		"X-Requested-With",
        		"accept",
        		"Origin",
        		"authorization",
        		"Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
       // configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin",
        		"Content-Disposition",
        		"Access-Control-Allow-Credentials",
        		"Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//	@Override
//	@Bean
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
}
