package com.emergency.rollcall.service.Impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

	private final Logger logger = Logger.getLogger(AssemblyService.class.getName());

	@Autowired
	private AssemblyDao assemblyDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveAssembly(AssemblyDto data) {
		ResponseDto res = new ResponseDto();
		Assembly entity = new Assembly();

		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		entity = modelMapper.map(data, Assembly.class);
		entity.setCreateddate(strCreatedDate);
		logger.info("Saving assembly entity: " + entity);
		try {
			if (entity.getSyskey() == 0) {
				Assembly entityres = assemblyDao.save(entity);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved.");
					logger.info("Assembly entity saved successfully: " + entityres);
				}
			} else {
				Assembly entityres = assemblyDao.save(entity);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated.");
					logger.info("Assembly entity updated successfully: " + entityres);
				}
			}

		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
			logger.severe("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
			logger.severe("Database error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public AssemblyDto getById(Long id) {
		AssemblyDto assemblyDto = new AssemblyDto();
		Assembly assembly = new Assembly();
		logger.info("Retrieve assembly data: " + id);
		try {
			Optional<Assembly> assemblyOptional = assemblyDao.findById(id);
			if (assemblyOptional.isPresent()) {
				assembly = assemblyOptional.get();
				assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
				logger.info("Successfully retrieve assembly data: " + assemblyDto);
			} else {

			}
		} catch (DataAccessException dae) {
			logger.info("Error retrieve assembly data : " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);

		} catch (Exception e) {
			logger.info("Error retrieve assembly data : " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return assemblyDto;
	}

	@Override
	public ResponseDto updateAssembly(AssemblyDto data) {
		ResponseDto res = new ResponseDto();
		Assembly assembly = new Assembly();
		String createdDate;
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		logger.info("Received update assembly data : " + data);
		try {
			Optional<Assembly> assemblyOptional = assemblyDao.findById(data.getSyskey());
			if (assemblyOptional.isPresent()) {
				assembly = assemblyOptional.get();
				createdDate = assembly.getCreateddate();
				assembly = modelMapper.map(data, Assembly.class);
				logger.info("Updating assembly data : " + data);
				assembly.setCreateddate(assembly.getCreateddate());
				//assembly.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				assembly.setModifieddate(strCreatedDate);
				assembly.setCreateddate(createdDate);
				assemblyDao.save(assembly);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updated assembly data : " + res);
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Does no found updated assembly data : " + res);
			}
		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
			logger.info("Error updated assembly data : " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
			logger.info("Error updated assembly data : " + e.getMessage());
		}

		return res;
	}

	@Override
	public ResponseDto deleteAssembly(long id) {
		ResponseDto res = new ResponseDto();
		Assembly assembly = new Assembly();
		logger.info("Received delete assembly data : " + id);
		try {
			Long count = assemblyDao.countEmergencyActivatesByAssemblyId(id);

			if (count > 0) {
				res.setStatus_code(200);
				res.setMessage("Cannot delete the assembly because it is associated with active emergencies.");
				return res;
			}
			Optional<Assembly> assemblyOptional = assemblyDao.findById(id);
			if (assemblyOptional.isPresent()) {
				assembly = assemblyOptional.get();
				assembly.setIsDelete(1);
				assemblyDao.save(assembly);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfuly deleted assembly data : " + res);
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("Does not found deleted assembly data : " + res);
			}

		} catch (DataAccessException e) {
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
			logger.info("Error deleted assembly data : " + e.getMessage());
		} catch (Exception e) {
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
			logger.info("Error deleted assembly data : " + e.getMessage());
		}

		return res;
	}

	@Override
	public Page<AssemblyDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Assembly> assemblyList;
		List<AssemblyDto> assemblyDtoList = new ArrayList<>();
		logger.info("Received search assembly data : " + params);
		try {
			if (params == null && params.isEmpty()) {
				assemblyList = assemblyDao.searchByParams(pageRequest);
			} else {
				assemblyList = assemblyDao.searchByParams(pageRequest, params);
			}
			if (!assemblyList.isEmpty()) {
				for (Assembly assembly : assemblyList) {
					AssemblyDto assemblyDto = new AssemblyDto();
					assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
					assemblyDtoList.add(assemblyDto);
				}
			}
			logger.info("Searched assembly data : " + assemblyDtoList);

		} catch (DataAccessException dae) {
			logger.info("Database error occurred:  : " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("An unexpected error occurred: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(assemblyDtoList, pageRequest, assemblyList.getTotalElements());
	}

	@Override
	public List<AssemblyDto> getAllList() {

		List<AssemblyDto> assemblyDtoList = new ArrayList<>();
		List<Assembly> assemblyList = new ArrayList<>();
		logger.info("Received assembly data list ");
		try {
			assemblyList = assemblyDao.findAllByStatusAndIsDelete(1, 0);
			if (!assemblyList.isEmpty()) {
				for (Assembly assembly : assemblyList) {
					AssemblyDto assemblyDto = new AssemblyDto();
					assemblyDto = modelMapper.map(assembly, AssemblyDto.class);
					assemblyDtoList.add(assemblyDto);
				}
				logger.info("Received assembly data list " + assemblyDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Database error occurred: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("An unexpected error occurred: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return assemblyDtoList;
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

//	public String getTodayDate() {
//		Date d = new Date();
//		String data;
//		new SimpleDateFormat("yyyyMMdd").format(d);
//		new SimpleDateFormat("yyyyMMddhhmmss").format(d);
//		return data;
//	}
}
