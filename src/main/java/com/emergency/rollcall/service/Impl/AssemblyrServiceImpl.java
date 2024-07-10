package com.emergency.rollcall.service.Impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

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

	@Override
	public ResponseDto saveAssembly(AssemblyDto data) {
		ResponseDto res = new ResponseDto();
		Assembly entity = new Assembly();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		entity.setSyskey(data.getId());
		entity.setCreateddate(strCreatedDate);
		entity.setModifieddate(strCreatedDate);
		entity.setStatus(1);
		entity.setName(data.getName());
		entity.setLatitude(data.getLatitude());
		entity.setLongtiude(data.getLongtiude());

		entity.setCreateddate(this.yyyyMMddFormat(data.getCreateddate()));

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
		AssemblyDto assemblyDto = null;
		Assembly assembly = new Assembly();
		Optional<Assembly> assemblyOptional = assemblyDao.findById(id);
		if(assemblyOptional.isPresent()) {
			assembly = assemblyOptional.get();
			assemblyDto.setId(assembly.getSyskey());
			assemblyDto.setName(assembly.getName());
			assemblyDto.setLatitude(assembly.getLatitude());
			assemblyDto.setLongtiude(assembly.getLongtiude());
			assemblyDto.setIpaddress(assembly.getIpaddress());
			assemblyDto.setCreateddate(assembly.getCreateddate());
			assemblyDto.setModifieddate(assembly.getModifieddate());
		}
		return assemblyDto;
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
