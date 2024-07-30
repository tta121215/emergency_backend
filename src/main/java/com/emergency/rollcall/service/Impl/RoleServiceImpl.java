package com.emergency.rollcall.service.Impl;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emergency.rollcall.dao.MenuDao;
import com.emergency.rollcall.dao.RoleDao;
import com.emergency.rollcall.dao.RoleDao;
import com.emergency.rollcall.dto.MenuDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RoleDto;
import com.emergency.rollcall.entity.Menu;
import com.emergency.rollcall.entity.Role;
import com.emergency.rollcall.service.RoleService;
import com.emergency.rollcall.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	
	private final Logger logger = Logger.getLogger(RoleService.class.getName());

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private MenuDao menuDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveRole(RoleDto roleDto) {
		ResponseDto res = new ResponseDto();
		Role role = new Role();
		List<Menu> menuList = new ArrayList<>();

		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		role = modelMapper.map(roleDto, Role.class);
		logger.info("Saving role entity: " + roleDto);

		role.setCreateddate(this.yyyyMMddFormat(strCreatedDate));
		if(!roleDto.getMenu().isEmpty()) {
			for (MenuDto menuDto : roleDto.getMenu()) {
				Menu menuData = new Menu();
				menuData.setName(menuDto.getName());
				menuData.setButtons(menuDto.getButtons());
				menuData.setCreateddate(strCreatedDate);
				menuData.setStatus(menuDto.getStatus());				
				menuDao.save(menuData);
				menuList.add(menuData);
			}
			
			
		}
	    role.setMenus(menuList);
		try {
			if (role.getSyskey() == 0) {
				Role entityres = roleDao.save(role);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Saved");
					logger.info("Successfully Saving role entity: " + entityres);

				}
			} else {
				Role entityres = roleDao.save(role);
				if (entityres.getSyskey() > 0) {
					res.setStatus_code(200);
					res.setMessage("Successfully Updated");
					logger.info("Successfully updating role entity: " + entityres);

				}
			}
		} catch (DataAccessException e) {
			logger.info("Error saving role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error saving role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}

		return res;
	}

	@Override
	public RoleDto getById(long id) {
		RoleDto roleDto = new RoleDto();
		Role role = new Role();
		logger.info("Searching role entity: " + id);
		try {
			Optional<Role> roleOptional = roleDao.findById(id);
			if (roleOptional.isPresent()) {
				role = roleOptional.get();
				roleDto = modelMapper.map(role, RoleDto.class);
				List<MenuDto> menuDtos = role.getMenus().stream().map(menu -> {
	                MenuDto menuDto = new MenuDto();
	                menuDto.setSyskey(menu.getSyskey());
	                menuDto.setName(menu.getName());
	                menuDto.setButtons(menu.getButtons());
	                menuDto.setCreateddate(menu.getCreateddate());
	                menuDto.setModifieddate(menu.getModifieddate());
	                menuDto.setStatus(menu.getStatus());
	                return menuDto;
	            }).collect(Collectors.toList());
				roleDto.setMenu(menuDtos);
				logger.info("Succesfully retrieving role entity: " +  roleDto);
			}
		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return roleDto;
	}

	@Override
	public ResponseDto updateRole(RoleDto modeNotiDto) {
		ResponseDto res = new ResponseDto();
		Role modeNoti = new Role();
		String createdDate;
		ZonedDateTime dateTime = ZonedDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);
		logger.info("Updating role entity: " + modeNotiDto);
		try {
			Optional<Role> RoleOptional = roleDao.findById(modeNotiDto.getSyskey());
			if (RoleOptional.isPresent()) {
				modeNoti = RoleOptional.get();
				createdDate = modeNoti.getCreateddate();
				modeNoti = modelMapper.map(modeNotiDto, Role.class);
				modeNoti.setCreateddate(createdDate);
				modeNoti.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
				roleDao.save(modeNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
				logger.info("Successfully updating role entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("Data does not found");
				logger.info("Data does not found for updating role entity: " + res.getMessage());
			}
		} catch (DataAccessException e) {
			logger.info("Error updaing role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error updating role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

	@Override
	public ResponseDto deleteRole(long id) {
		ResponseDto res = new ResponseDto();
		Role modeNoti = new Role();
		logger.info("Deleting role entity: " + id);
		try {
			Optional<Role> modeNotiOptional = roleDao.findById(id);
			if (modeNotiOptional.isPresent()) {
				modeNoti = modeNotiOptional.get();
				roleDao.delete(modeNoti);
				res.setStatus_code(200);
				res.setMessage("Successfully Deleted");
				logger.info("Successfully delete role entity: " + res.getMessage());
			} else {
				res.setStatus_code(401);
				res.setMessage("No data found");
				logger.info("No data found role entity: " + res.getMessage());

			}

		} catch (DataAccessException e) {
			logger.info("Errror deleting role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("Database error occurred: " + e.getMessage());
		} catch (Exception e) {
			logger.info("Error deleting role entity: " + e.getMessage());
			res.setStatus_code(500);
			res.setMessage("An unexpected error occurred: " + e.getMessage());
		}
		return res;
	}

//	@Override
//	public Page<RoleDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
//		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
//		Page<Role> modeNotiList;
//		List<RoleDto> modeNotiDtoList = new ArrayList<>();
//		logger.info("Searching role entity: ");
//		try {
//			if (params == null || params.isEmpty()) {
//				modeNotiList = roleDao.findByNameOrCode(pageRequest);
//			} else {
//				modeNotiList = roleDao.findByNameOrCode(pageRequest, params);
//			}
//			if (modeNotiList != null) {
//				for (Role modeNoti : modeNotiList) {
//					RoleDto modeNotiDto = new RoleDto();
//					modeNotiDto = modelMapper.map(modeNoti, RoleDto.class);
//					modeNotiDtoList.add(modeNotiDto);
//				}
//				logger.info("Successfully searching role entity: " + modeNotiDtoList);
//			}
//		} catch (DataAccessException dae) {
//			logger.info("Error searching role entity: " + dae.getMessage());
//			System.err.println("Database error occurred: " + dae.getMessage());
//			throw new RuntimeException("Database error occurred, please try again later.", dae);
//		} catch (Exception e) {
//			logger.info("Error searching role entity: " + e.getMessage());
//			System.err.println("An unexpected error occurred: " + e.getMessage());
//			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
//		}
//
//		return new PageImpl<>(modeNotiDtoList, pageRequest, modeNotiList.getTotalElements());
//	}
//
//	@Override
//	public List<RoleDto> getAllList() {
//
//		List<RoleDto> modeNotiDtoList = new ArrayList<>();
//		List<Role> modeNotiList = new ArrayList<>();
//		logger.info("Retrieving role entity: " );
//		try {
//			modeNotiList = roleDao.findAllByStatus(1);
//			if (modeNotiList != null) {
//				for (Role modeNoti : modeNotiList) {
//					RoleDto modeNotiDto = new RoleDto();
//					modeNotiDto = modelMapper.map(modeNoti, RoleDto.class);
//					modeNotiDtoList.add(modeNotiDto);
//				}
//				logger.info("Successfully role entity: " + modeNotiDtoList);
//			}
//
//		} catch (DataAccessException dae) {
//			logger.info("Error retrieving role entity: " + dae.getMessage());
//			System.err.println("Database error occurred: " + dae.getMessage());
//			throw new RuntimeException("Database error occurred, please try again later.", dae);
//		} catch (Exception e) {
//			logger.info("Error retrieving role entity: " + e.getMessage());
//			System.err.println("An unexpected error occurred: " + e.getMessage());
//			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
//		}
//
//		return modeNotiDtoList;
//	}

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
