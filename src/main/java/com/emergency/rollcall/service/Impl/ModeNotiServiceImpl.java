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
import com.emergency.rollcall.dao.ModeNotiDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.entity.ModeNoti;
import com.emergency.rollcall.service.ModeNotiService;

@Service
public class ModeNotiServiceImpl implements ModeNotiService {
	
	private final Logger logger = Logger.getLogger(ModeNotiService.class.getName());

	@Autowired
	private ModeNotiDao modeNotiDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveModeNoti(ModeNotiDto modeNotiDto) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		modeNoti = modelMapper.map(modeNotiDto, ModeNoti.class);
		logger.info("Saving mode noti entity: " + modeNotiDto);

		modeNoti.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {
			if (modeNoti.getSyskey() == 0) {
				ModeNoti entityres = modeNotiDao.save(modeNoti);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully Saving mode noti entity: " + entityres);

				}
			} else {
				ModeNoti entityres = modeNotiDao.save(modeNoti);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully updating mode noti entity: " + entityres);

				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ModeNotiDto getById(long id) {
		ModeNotiDto modeNotiDto = new ModeNotiDto();
		ModeNoti modeNoti = new ModeNoti();
		logger.info("Searching mode noti entity: " + id);
		try {
			Optional<ModeNoti> ModeNotiOptional = modeNotiDao.findById(id);
			if (ModeNotiOptional.isPresent()) {
				modeNoti = ModeNotiOptional.get();
				modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
				logger.info("Succesfully retrieving mode noti entity: " +  modeNotiDto);
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
	public ResponseDto updateModeNoti(ModeNotiDto modeNotiDto) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating mode noti entity: " + modeNotiDto);
		try {
			Optional<ModeNoti> ModeNotiOptional = modeNotiDao.findById(modeNotiDto.getSyskey());
			if (ModeNotiOptional.isPresent()) {
				modeNoti = ModeNotiOptional.get();
				createdDate = modeNoti.getCreateddate();
				modeNoti = modelMapper.map(modeNotiDto, ModeNoti.class);
				modeNoti.setCreateddate(createdDate);
				modeNoti.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				modeNotiDao.save(modeNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating mode noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found for updating mode noti entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error updaing mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public ResponseDto deleteModeNoti(long id) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();
		logger.info("Deleting mode noti entity: " + id);
		try {
			Long count = modeNotiDao.findNotiTemplatesByModeNotiSyskey(id);		
			if(count > 0) {
				res.setStatus_code(200);
				res.setMessage("Cannot delete the mode noti because it is associated with noti template.");
				return res;
			}
			Optional<ModeNoti> modeNotiOptional = modeNotiDao.findById(id);
			if (modeNotiOptional.isPresent()) {
				modeNoti = modeNotiOptional.get();
				modeNoti.setIsDelete(1);
				modeNotiDao.save(modeNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully delete mode noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found mode noti entity: " + res.getMessage());

			}

		} catch (DataAccessException e) {
			logger.info("Errror deleting mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting mode noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public Page<ModeNotiDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<ModeNoti> modeNotiList;
		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		logger.info("Searching mode noti entity: ");
		try {
			if (params == null || params.isEmpty()) {
				modeNotiList = modeNotiDao.findByNameOrCode(pageRequest);
			} else {
				modeNotiList = modeNotiDao.findByNameOrCode(pageRequest, params);
			}
			if (modeNotiList != null) {
				for (ModeNoti modeNoti : modeNotiList) {
					ModeNotiDto modeNotiDto = new ModeNotiDto();
					modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
					modeNotiDtoList.add(modeNotiDto);
				}
				logger.info("Successfully searching mode noti entity: " + modeNotiDtoList);
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching mode noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching mode noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(modeNotiDtoList, pageRequest, modeNotiList.getTotalElements());
	}

	@Override
	public List<ModeNotiDto> getAllList() {

		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		List<ModeNoti> modeNotiList = new ArrayList<>();
		logger.info("Retrieving mode noti entity: " );
		try {
			modeNotiList = modeNotiDao.findAllByStatusAndIsDelete(1,0);
			if (modeNotiList != null) {
				for (ModeNoti modeNoti : modeNotiList) {
					ModeNotiDto modeNotiDto = new ModeNotiDto();
					modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
					modeNotiDtoList.add(modeNotiDto);
				}
				logger.info("Successfully mode noti entity: " + modeNotiDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving mode noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving mode noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return modeNotiDtoList;
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
