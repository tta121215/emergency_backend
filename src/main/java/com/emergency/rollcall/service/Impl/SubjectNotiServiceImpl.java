package com.emergency.rollcall.service.Impl;

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
import com.emergency.rollcall.dao.SubjectNotiDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.SubjectNotiDto;
import com.emergency.rollcall.entity.SubjectNoti;
import com.emergency.rollcall.service.ModeNotiService;
import com.emergency.rollcall.service.SubjectNotiService;

@Service
public class SubjectNotiServiceImpl implements SubjectNotiService {

	private final Logger logger = Logger.getLogger(ModeNotiService.class.getName());

	@Autowired
	private SubjectNotiDao subjectNotiDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveSubjectNoti(SubjectNotiDto subjectNotiDto) {
		ResponseDto res = new ResponseDto();
		SubjectNoti subjectNoti = new SubjectNoti();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		subjectNoti = modelMapper.map(subjectNotiDto, SubjectNoti.class);
		logger.info("Saving subject noti entity: " + subjectNotiDto);

		subjectNoti.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (subjectNoti.getSyskey() == 0) {
				SubjectNoti entityres = subjectNotiDao.save(subjectNoti);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully Saving subject noti entity: " + entityres);

				}
			} else {
				SubjectNoti entityres = subjectNotiDao.save(subjectNoti);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully updating subject noti entity: " + entityres);

				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public SubjectNotiDto getById(long id) {
		SubjectNotiDto modeNotiDto = new SubjectNotiDto();
		SubjectNoti subjectNoti = new SubjectNoti();
		logger.info("Searching subject noti entity: " + id);
		try {
			Optional<SubjectNoti> subjectNotiOptional = subjectNotiDao.findById(id);
			if (subjectNotiOptional.isPresent()) {
				subjectNoti = subjectNotiOptional.get();
				modeNotiDto = modelMapper.map(subjectNoti, SubjectNotiDto.class);
				logger.info("Succesfully retrieving subject noti entity: " + modeNotiDto);
			}
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return modeNotiDto;
	}

	@Override
	public ResponseDto updateSubjectNoti(SubjectNotiDto subjectNotiDto) {
		ResponseDto res = new ResponseDto();
		SubjectNoti subjectNoti = new SubjectNoti();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating subject noti entity: " + subjectNotiDto);
		try {
			Optional<SubjectNoti> subjectNotiOptional = subjectNotiDao.findById(subjectNotiDto.getSyskey());
			if (subjectNotiOptional.isPresent()) {
				subjectNoti = subjectNotiOptional.get();
				createdDate = subjectNoti.getCreateddate();
				subjectNoti = modelMapper.map(subjectNotiDto, SubjectNoti.class);
				subjectNoti.setCreateddate(createdDate);
				subjectNoti.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				subjectNotiDao.save(subjectNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating subject noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found for updating subject noti entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error updaing subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public ResponseDto deleteSubjectNoti(long id) {
		ResponseDto res = new ResponseDto();
		SubjectNoti subjectNoti = new SubjectNoti();
		logger.info("Deleting subject noti entity: " + id);
		try {
			Optional<SubjectNoti> subjectNotiOptional = subjectNotiDao.findById(id);
			if (subjectNotiOptional.isPresent()) {
				subjectNoti = subjectNotiOptional.get();
				subjectNotiDao.delete(subjectNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully delete subject noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found subject noti entity: " + res.getMessage());

			}

		} catch (DataAccessException e) {
			logger.info("Errror deleting subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting subject noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public Page<SubjectNotiDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<SubjectNoti> subjectNotiList;
		List<SubjectNotiDto> subjectNotiDtoList = new ArrayList<>();
		logger.info("Searching subject noti entity: ");
		try {
			if (params == null || params.isEmpty()) {
				subjectNotiList = subjectNotiDao.findByNameOrCode(pageRequest);
			} else {
				subjectNotiList = subjectNotiDao.findByNameOrCode(pageRequest, params);
			}
			if (subjectNotiList != null) {
				for (SubjectNoti subjectNoti : subjectNotiList) {
					SubjectNotiDto subjectNotiDto = new SubjectNotiDto();
					subjectNotiDto = modelMapper.map(subjectNoti, SubjectNotiDto.class);
					subjectNotiDtoList.add(subjectNotiDto);
				}
				logger.info("Successfully searching subject noti entity: " + subjectNotiDtoList);
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching subject noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching subject noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(subjectNotiDtoList, pageRequest, subjectNotiList.getTotalElements());
	}

	@Override
	public List<SubjectNotiDto> getAllList() {

		List<SubjectNotiDto> subjectNotiDtoList = new ArrayList<>();
		List<SubjectNoti> subjectNotiList = new ArrayList<>();
		logger.info("Retrieving subject noti entity: ");
		try {
			subjectNotiList = subjectNotiDao.findAllByStatus(1);
			if (subjectNotiList != null) {
				for (SubjectNoti subjectNoti : subjectNotiList) {
					SubjectNotiDto subjectNotiDto = new SubjectNotiDto();
					subjectNotiDto = modelMapper.map(subjectNoti, SubjectNotiDto.class);
					subjectNotiDtoList.add(subjectNotiDto);
				}
				logger.info("Successfully subject noti entity: " + subjectNotiDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving subject noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving subject noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return subjectNotiDtoList;
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
