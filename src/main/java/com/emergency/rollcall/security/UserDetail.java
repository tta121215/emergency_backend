//package com.emergency.emergency_rollcall.security;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.dohyoeyar.pm.dto.CustomerDTO;
//import com.dohyoeyar.pm.entity.Customer;
//import com.dohyoeyar.pm.entity.Role;
//import com.dohyoeyar.pm.enums.AccountStatus;
//import com.dohyoeyar.pm.repository.CustomerRepository;
//
//@Service
//public class UserDetail implements UserDetailsService {
//
//	@Autowired
//	private CustomerRepository customerRepository;
//
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		final CustomerDTO userDTO = new CustomerDTO();
//		
//		Customer usr = customerRepository.findByMobileNoAndStatus(username, AccountStatus.ACTIVE);
//		if (usr == null) {
//			// return error
//			throw new UsernameNotFoundException("Mobile No. '" + username + "' not found");
//		}
//
//		else {
//			userDTO.setMobileNo(usr.getMobileNo());
//			userDTO.setPassword(usr.getPassword());
//			userDTO.setRole(usr.getRole());
//		}
//
//		/*
//		 * here we need to add our role field to a list bcoz spring seurity role goes
//		 * with list
//		 */
//
//		List<Role> roleList = new ArrayList<Role>();
//		roleList.add(userDTO.getRole());
//		return org.springframework.security.core.userdetails.User.withUsername(userDTO.getMobileNo())
//				.password(userDTO.getPassword()).authorities(roleList).accountExpired(false).accountLocked(false)
//				.credentialsExpired(false).disabled(false).build();
//
//	}
//}


