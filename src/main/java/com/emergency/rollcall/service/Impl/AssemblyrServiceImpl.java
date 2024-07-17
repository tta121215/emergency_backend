package com.emergency.rollcall.service.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.AssemblyDao;
import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.entity.Assembly;
import com.emergency.rollcall.service.AssemblyService;

@Service
public class AssemblyrServiceImpl implements AssemblyService {

	@Autowired
	private AssemblyDao assemblyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveAssembly(AssemblyDto data) {
		ResponseDto res = new ResponseDto();
		Assembly entity = new Assembly();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		entity = modelMapper.map(data, Assembly.class);
		entity.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (entity.getSyskey() == 0) {
			Assembly entityres = assemblyDao.save(entity);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved.");
			}
		} else {
			Assembly entityres = assemblyDao.save(entity);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated.");
			}
		}

		return res;
	}

	@Override
	public AssemblyDto getById(Long id) {
		AssemblyDto assemblyDto = new AssemblyDto();
		Assembly assembly = new Assembly();
		Optional<Assembly> assemblyOptional = assemblyDao.findById(id);
		if (assemblyOptional.isPresent()) {
			assembly = assemblyOptional.get();
			assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
		} else {

		}
		return assemblyDto;
	}

	@Override
	public ResponseDto updateAssembly(AssemblyDto data) {
		ResponseDto res = new ResponseDto();
		Assembly assembly = new Assembly();
		String createdDate;
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);	
		
		Optional<Assembly> assemblyOptional = assemblyDao.findById(data.getSyskey());
		if (assemblyOptional.isPresent()) {
			assembly = assemblyOptional.get();
			createdDate = assembly.getCreateddate();
			assembly = modelMapper.map(data, Assembly.class);
			assembly.setCreateddate(assembly.getCreateddate());
			assembly.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			assembly.setCreateddate(createdDate);
			assemblyDao.save(assembly);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setStatus_code(401);;
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteAssembly(long id) {
		ResponseDto res = new ResponseDto();
		Assembly assembly = new Assembly();

		Optional<Assembly> assemblyOptional = assemblyDao.findById(id);
		if (assemblyOptional.isPresent()) {
			assembly = assemblyOptional.get();
			assemblyDao.delete(assembly);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<AssemblyDto> searchByParams(int page, int size, String params,String sortBy,String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
	    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));		
		Page<Assembly> assemblyList;
		List<AssemblyDto> assemblyDtoList = new ArrayList<>();

		if (params == null && params.isEmpty()) {
			assemblyList = assemblyDao.searchByParams(pageRequest);
			if (assemblyList != null) {
				for (Assembly assembly : assemblyList) {
					AssemblyDto assemblyDto = new AssemblyDto();
					assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
					assemblyDtoList.add(assemblyDto);
				}
			}

		} else {
			assemblyList = assemblyDao.searchByParams(pageRequest, params);
			if (assemblyList != null) {
				for (Assembly assembly : assemblyList) {
					AssemblyDto assemblyDto = new AssemblyDto();
					assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
					assemblyDtoList.add(assemblyDto);
				}
			}

		}

		return new PageImpl<>(assemblyDtoList, pageRequest, assemblyList.getTotalElements());
	}

	public String ddMMyyyFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.substring(6) + "-" + aDate.substring(4, 6) + "-" + aDate.substring(0, 4);

		return l_Date;
	}

	public String yyyyMMddFormat(String aDate) {
		String l_Date = "";
		if (!aDate.equals("") && aDate != null)
			l_Date = aDate.replaceAll("-", "");

		return l_Date;
	}
}
