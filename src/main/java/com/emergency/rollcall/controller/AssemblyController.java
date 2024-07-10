package com.emergency.rollcall.controller;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.service.AssemblyService;


@RestController
@CrossOrigin
@RequestMapping("assembly")
public class AssemblyController {
	
	@Autowired
	private AssemblyService assemblyService;
	
	@PostMapping("")
	public ResponseEntity<?> saveAssembly(@RequestBody AssemblyDto data){
		if(data != null){
				ResponseDto result = assemblyService.saveAssembly(data);
				return ResponseEntity.accepted().body(result);
			}
		else{
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("")
	public ResponseEntity<?> getById(@RequestParam("id") Long id) {
		AssemblyDto result = null;
		if(id != null) {
			result = assemblyService.getById(id);
		}
		return ResponseEntity.accepted().body(result);
		

		
	}
	
//	@GetMapping("")
//	public ResponseEntity<?> getNumber(@RequestParam String searchVal,@RequestParam String date,@RequestParam String session){
//		 List<NumberRegiterDto> result = nrservice.getAllList(searchVal,date,session);
//				return ResponseEntity.accepted().body(result);
//		
//	}
//	
//	@GetMapping("/header")
//	public ResponseEntity<?> getNumberHeader(@RequestParam String searchVal,@RequestParam String date,@RequestParam String session){
//		 List<HeaderDto> result = nrservice.getAllHeaderList(searchVal,date,session);
//				return ResponseEntity.accepted().body(result);
//		
//	}

}
