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
import com.emergency.rollcall.dao.ContentNotiDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ContentNotiDto;
import com.emergency.rollcall.entity.ContentNoti;
import com.emergency.rollcall.service.ContentNotiService;

@Service
public class ContentNotiServiceImpl implements ContentNotiService {

	private final Logger logger = Logger.getLogger(ContentNotiService.class.getName());

	@Autowired
	private ContentNotiDao contentNotiDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveContentNoti(ContentNotiDto contentNotiDto) {
		ResponseDto res = new ResponseDto();
		ContentNoti contentNoti = new ContentNoti();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		contentNoti = modelMapper.map(contentNotiDto, ContentNoti.class);
		logger.info("Saving conent noti entity: " + contentNotiDto);

		contentNoti.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		try {

			ContentNoti entityres = contentNotiDao.save(contentNoti);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
				logger.info("Successfully Saving content noti entity: " + entityres);

			}

		} catch (DataAccessException e) {
			logger.info("Error saving content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public ContentNotiDto getById(long id) {
		ContentNotiDto contentNotiDto = new ContentNotiDto();
		ContentNoti contentNoti = new ContentNoti();
		logger.info("Searching content noti entity: " + id);
		try {
			Optional<ContentNoti> contentNotiOptional = contentNotiDao.findById(id);
			if (contentNotiOptional.isPresent()) {
				contentNoti = contentNotiOptional.get();
				contentNotiDto = modelMapper.map(contentNoti, ContentNotiDto.class);
				logger.info("Succesfully retrieving content noti entity: " + contentNotiDto);
			}
		} catch (DataAccessException dae) {
			logger.info("Error content noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error content noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return contentNotiDto;
	}

	@Override
	public ResponseDto updateContentNoti(ContentNotiDto contentNotiDto) {
		ResponseDto res = new ResponseDto();
		ContentNoti contentNoti = new ContentNoti();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating content noti entity: " + contentNotiDto);
		try {
			Optional<ContentNoti> contentNotiOptional = contentNotiDao.findById(contentNotiDto.getSyskey());
			if (contentNotiOptional.isPresent()) {
				contentNoti = contentNotiOptional.get();
				createdDate = contentNoti.getCreateddate();
				contentNoti = modelMapper.map(contentNotiDto, ContentNoti.class);
				contentNoti.setCreateddate(createdDate);
				contentNoti.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				contentNotiDao.save(contentNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating content noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found for updating mode noti entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error updaing content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public ResponseDto deleteContentNoti(long id) {
		ResponseDto res = new ResponseDto();
		ContentNoti contentNoti = new ContentNoti();
		logger.info("Deleting content noti entity: " + id);
		try {
			Long count = contentNotiDao.findNotiTemplatesByContentNotiSyskey(id);		
			if(count > 0) {
				res.setStatus_code(200);
				res.setMessage("Cannot delete the content noti because it is associated with noti template.");
				return res;
			}
			Optional<ContentNoti> contentNotiOptional = contentNotiDao.findById(id);
			if (contentNotiOptional.isPresent()) {
				contentNoti = contentNotiOptional.get();
				contentNoti.setIsDelete(1);
				contentNotiDao.save(contentNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully delete mode noti entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found mode noti entity: " + res.getMessage());

			}

		} catch (DataAccessException e) {
			logger.info("Errror deleting content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting content noti entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public Page<ContentNotiDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<ContentNoti> contentNotiList;
		List<ContentNotiDto> contentNotiDtoList = new ArrayList<>();
		logger.info("Searching content noti entity: ");
		try {
			if (params == null || params.isEmpty()) {
				contentNotiList = contentNotiDao.findByNameOrCode(pageRequest);
			} else {
				contentNotiList = contentNotiDao.findByNameOrCode(pageRequest, params);
			}
			if (!contentNotiList.isEmpty()) {
				for (ContentNoti contentNoti : contentNotiList) {
					ContentNotiDto contentNotiDto = new ContentNotiDto();
					contentNotiDto = modelMapper.map(contentNoti, ContentNotiDto.class);
					contentNotiDtoList.add(contentNotiDto);
				}
				logger.info("Successfully searching content noti entity: " + contentNotiDtoList);
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching content noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching content noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(contentNotiDtoList, pageRequest, contentNotiList.getTotalElements());
	}

	@Override
	public List<ContentNotiDto> getAllList() {

		List<ContentNotiDto> contentNotiDtoList = new ArrayList<>();
		List<ContentNoti> contentNotiList = new ArrayList<>();
		logger.info("Retrieving content noti entity: ");
		try {
			contentNotiList = contentNotiDao.findAllByStatusAndIsDelete(1,0);
			if (!contentNotiList.isEmpty()) {
				for (ContentNoti contentNoti : contentNotiList) {
					ContentNotiDto contentNotiDto = new ContentNotiDto();
					contentNotiDto = modelMapper.map(contentNoti, ContentNotiDto.class);
					contentNotiDtoList.add(contentNotiDto);
				}
				logger.info("Successfully content noti entity: " + contentNotiDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving content noti entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving content noti entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return contentNotiDtoList;
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
