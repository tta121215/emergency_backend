package com.emergency.rollcall.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:86400000}")
	private long validityInMilliseconds = 2592000000L;// 24hr

//	@Autowired
//	private UserDetail userDetail;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

//	public String createToken(String loginId, List<Role> roles) {
//		Claims claims = Jwts.claims().setSubject(loginId);
//		claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
//				.filter(Objects::nonNull).collect(Collectors.toList()));
//		Date now = new Date();
//		Date validity = new Date(now.getTime() + validityInMilliseconds);
//
//		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
//				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
//	}

//	public Authentication getAuthentication(String token) {
//		UserDetails userDetails = userDetail.loadUserByUsername(getUsername(token));
//		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	/*
	 * public String resolveToken(HttpServletRequest req) {
	 * 
	 * String bearerToken = req.getHeader("Authorization"); if (bearerToken != null
	 * && bearerToken.startsWith("DYY ")) { return bearerToken.substring(4); }
	 * return null;
	 * 
	 * 
	 * String bearerToken = req.getHeader("Authorization"); if (bearerToken != null)
	 * { return bearerToken.substring(7); } return null; }
	 */
	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			// return err
			throw new RuntimeException("Expired or invalid JWT token");
		}
	}
}
