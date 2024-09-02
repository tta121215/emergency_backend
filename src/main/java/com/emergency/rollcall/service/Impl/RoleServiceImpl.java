package com.emergency.rollcall.service.Impl;

import java.time.ZoneId;
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
import com.emergency.rollcall.dto.AssemblyDto;
import com.emergency.rollcall.dto.MenuDto;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.RoleDto;
import com.emergency.rollcall.entity.Assembly;
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

		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);

		role = modelMapper.map(roleDto, Role.class);
		logger.info("Saving role entity: " + roleDto);

		role.setCreateddate(strCreatedDate);
		if(!roleDto.getMenu().isEmpty()) {
			for (MenuDto menuDto : roleDto.getMenu()) {
				Menu menuData = new Menu();				
				menuData.setName(menuDto.getName());
				menuData.setButtons(menuDto.getButtons());
				menuData.setCreateddate(strCreatedDate);
				menuData.setStatus(menuDto.getStatus());	
				menuData.setId(menuDto.getId());
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
	                menuDto.setId(menu.getId());
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
	public ResponseDto updateRole(RoleDto roleDto) {
		ResponseDto res = new ResponseDto();
		List<Menu> menuList = new ArrayList<>();
		Role role = new Role();
		String createdDate;
		ZoneId malaysiaZoneId = ZoneId.of("Asia/Kuala_Lumpur");
		ZonedDateTime malaysiaDateTime = ZonedDateTime.now(malaysiaZoneId);		
		DateTimeFormatter timeformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		String strCreatedDate = malaysiaDateTime.format(timeformatter);
		logger.info("Updating role entity: " + roleDto);
		try {
			Optional<Role> RoleOptional = roleDao.findById(roleDto.getSyskey());
			if (RoleOptional.isPresent()) {
				role = RoleOptional.get();
				createdDate = role.getCreateddate();
				role = modelMapper.map(roleDto, Role.class);
				role.setCreateddate(createdDate);
				role.setModifieddate(strCreatedDate);
				if(!roleDto.getMenu().isEmpty()) {
					role.getMenus().clear();
					roleDao.save(role);
					for (MenuDto menuDto : roleDto.getMenu()) {						
						Menu menu = new Menu();
						menu.setId(menuDto.getId());
		                menu.setName(menuDto.getName());
		                menu.setButtons(String.join(",", menuDto.getButtons()));
		                menu.setStatus(menuDto.getStatus());
		                menu.setCreateddate(strCreatedDate);
		                menu.setModifieddate(strCreatedDate);
		                menuList.add(menu);
		                
					}
					role.setMenus(menuList);
					
				}
				roleDao.save(role);
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
		Role role = new Role();
		logger.info("Deleting role entity: " + id);
		try {
			Optional<Role> roleOptional = roleDao.findById(id);
			if (roleOptional.isPresent()) {
				role = roleOptional.get();
				roleDao.delete(role);
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

	@Override
	public Page<RoleDto> searchByParams(int page, int size, String params, String sortBy, String direction) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<Role> roleList;
		List<RoleDto> roleDtoList = new ArrayList<>();
		List<MenuDto> menuDtoList = new ArrayList<>();
		logger.info("Searching role entity: ");
		try {
			if (params == null || params.isEmpty()) {
				roleList = roleDao.findByName(pageRequest);
			} else {
				roleList = roleDao.findByName(pageRequest, params);
			}
			if (roleList != null) {
				for (Role role : roleList) {
					RoleDto roleDto = new RoleDto();
					roleDto = modelMapper.map(role, RoleDto.class);
					if(!role.getMenus().isEmpty()) {
						for(Menu menu : role.getMenus()) {
							MenuDto menuDto = modelMapper.map(menu, MenuDto.class);
							menuDtoList.add(menuDto);
						}
						roleDto.setMenu(menuDtoList);
						menuDtoList = new ArrayList<>();
					}
					roleDtoList.add(roleDto);
				}
				logger.info("Successfully searching role entity: " + roleDtoList);
			}
		} catch (DataAccessException dae) {
			logger.info("Error searching role entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching role entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(roleDtoList, pageRequest, roleList.getTotalElements());
	}

	@Override
	public List<RoleDto> getAllList() {

		List<RoleDto> roleDtoList = new ArrayList<>();
		List<Role> roleList = new ArrayList<>();
		List<MenuDto> menuDtoList = new ArrayList<>();

		logger.info("Retrieving role entity: " );
		try {
			roleList = roleDao.findAllByStatus(1);
			if (roleList != null) {
				for (Role role : roleList) {
					RoleDto roleDto = new RoleDto();
					roleDto = modelMapper.map(role, RoleDto.class);
					if(!role.getMenus().isEmpty()) {
						for(Menu menu : role.getMenus()) {
							MenuDto menuDto = modelMapper.map(menu, MenuDto.class);
							menuDtoList.add(menuDto);
						}
						roleDto.setMenu(menuDtoList);
						menuDtoList = new ArrayList<>();
					}
					roleDtoList.add(roleDto);
				}
				logger.info("Successfully role entity: " + roleDtoList);
			}

		} catch (DataAccessException dae) {
			logger.info("Error retrieving role entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error retrieving role entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return roleDtoList;
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
