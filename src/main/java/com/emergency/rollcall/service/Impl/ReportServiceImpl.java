package com.emergency.rollcall.service.Impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.AssemblyCheckInDao;
import com.emergency.rollcall.dao.EmergencyActivateDao;
import com.emergency.rollcall.dao.LocEmergencyDao;
import com.emergency.rollcall.dao.NotiReadLogDao;
import com.emergency.rollcall.dto.DashboardDetailDto;
import com.emergency.rollcall.dto.HeadCountDto;
import com.emergency.rollcall.dto.NotiReadLogDto;
import com.emergency.rollcall.dto.StaffDto;
import com.emergency.rollcall.entity.EmergencyActivate;
import com.emergency.rollcall.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	private final Logger logger = Logger.getLogger(ReportService.class.getName());

	@Autowired
	private AssemblyCheckInDao assemblyCheckInDao;

	@Autowired
	private NotiReadLogDao notiReadLogDao;

	@Autowired
	private EmergencyActivateDao emergencyActivateDao;

	@Autowired
	private LocEmergencyDao locEmergencyDao;

	@Override
	public Page<DashboardDetailDto> getAllCheckInList(Long activateId, int page, int size, String sortBy,
			String direction, String params) {
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("username")) {
			sort = Sort.by(sortDirection, "username");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffid");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "mobileno");
		} else if (sortBy.equals("assemblyname")) {
			sort = Sort.by(sortDirection, "a.name");
		} else if (sortBy.equals("type")) {
			sort = Sort.by(sortDirection, "v.visitororvip");
		} else if (sortBy.equals("department")) {
			sort = Sort.by(sortDirection, "d.name");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		Page<Map<String, Object>> usersCheckedInPage;
		try {
			if (params == null || params.isEmpty()) {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyActivate(activateId, pageRequest);
			} else {
				usersCheckedInPage = assemblyCheckInDao.findUsersCheckedInByEmergencyActivate(activateId, pageRequest,
						params);
			}
			if (!usersCheckedInPage.isEmpty()) {
				dashboardDetailDtoList = usersCheckedInPage.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setId((BigDecimal) staff.get("id"));
					detailDto.setUsername((String) staff.get("username"));
					detailDto.setEmail((String) staff.get("email"));
					detailDto.setMobileNo((String) staff.get("mobileno"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						detailDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						detailDto.setIcnumber(passportNumber);
					} else {
						detailDto.setIcnumber(" ");
					}
					detailDto.setStaffId((String) staff.get("staffid"));
					detailDto.setName((String) staff.get("name"));
					detailDto.setDepartment((String) staff.get("deptName"));
					detailDto.setType((String) staff.get("visitor"));
					detailDto.setCheckInDate((String) staff.get("currentdate"));
					detailDto.setCheckInTime((String) staff.get("currenttime"));
					detailDto.setAssemblyName((String) staff.get("assembly"));
					return detailDto;
				}).collect(Collectors.toList());
			}
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<DashboardDetailDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}

				dashboardDetailDtoList.sort(comparator);
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, usersCheckedInPage.getTotalElements());

	}

	@Override
	public Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction,
			String params) {
		EmergencyActivate emergencyActivate = emergencyActivateDao.findById(activateId).orElse(null);
		List<String> buildingNames = new ArrayList<>();
		List<Long> mainIds = new ArrayList<>();
		if (emergencyActivate.getMainBuilding() != null && emergencyActivate.getMainBuilding() != "") {
			mainIds = Arrays.stream(emergencyActivate.getMainBuilding().split(",")).map(String::trim)
					.map(Long::parseLong).collect(Collectors.toList());
			List<Object[]> mainBuilding = locEmergencyDao.findByMainIds(mainIds);

			for (Object[] row : mainBuilding) {
				buildingNames.add((String) row[1]);
			}
		}
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//		Sort sort = Sort.by(sortDirection, sortBy);
//		if (sortBy.equals("name")) {
//			sort = Sort.by(sortDirection, "name");
//		} else if (sortBy.equals("username")) {
//			sort = Sort.by(sortDirection, "username");
//		} else if (sortBy.equals("staffid")) {
//			sort = Sort.by(sortDirection, "staffid");
//		} else if (sortBy.equals("mobileno")) {
//			sort = Sort.by(sortDirection, "mobileno");
//		} else if (sortBy.equals("type")) {
//			sort = Sort.by(sortDirection, "v.visitororvip");
//		} else if (sortBy.equals("department")) {
//			sort = Sort.by(sortDirection, "d.name");
//		} else {
//			sort = Sort.by(sortDirection, "name");
//		}
		PageRequest pageRequest = PageRequest.of(page, size);
		List<StaffDto> staffDtoList = new ArrayList<>();

		Page<Map<String, Object>> usersNotCheckedInPage;

		try {
			if (params == null || params.isEmpty()) {
				usersNotCheckedInPage = assemblyCheckInDao.findUsersNotCheckedInByEmergencyActivate(activateId,
						buildingNames, pageRequest);
			} else {
				usersNotCheckedInPage = assemblyCheckInDao.findNotCheckIn(activateId,
						pageRequest, buildingNames,params);
			}
			if (!usersNotCheckedInPage.isEmpty()) {
				staffDtoList = usersNotCheckedInPage.stream().map(staff -> {
					StaffDto staffDto = new StaffDto();
					staffDto.setId((BigDecimal) staff.get("id"));
					staffDto.setUsername((String) staff.get("username"));
					staffDto.setEmail((String) staff.get("email"));
					staffDto.setMobileNo((String) staff.get("mobileNo"));
					staffDto.setName((String) staff.get("name"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						staffDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						staffDto.setIcnumber(passportNumber);
					} else {
						staffDto.setIcnumber(" ");
					}
					staffDto.setStaffId((String) staff.get("staffid"));
					staffDto.setDepartment((String) staff.get("deptName"));
					staffDto.setType((String) staff.get("visitor"));
					return staffDto;
				}).collect(Collectors.toList());
			}
			
			if ("name".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getUsername().isEmpty() ? null : dto.getUsername(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}
			if ("type".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getType().isEmpty() ? null : dto.getType(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}
			if ("staffid".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getStaffId().isEmpty() ? null : dto.getStaffId(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}
			if ("department".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getDepartment().isEmpty() ? null : dto.getDepartment(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}
			if ("mobileno".equalsIgnoreCase(sortBy)) {
				Comparator<StaffDto> comparator = Comparator.comparing(
						dto -> dto.getMobileNo().isEmpty() ? null : dto.getMobileNo(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				staffDtoList.sort(comparator);
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(staffDtoList, pageRequest, usersNotCheckedInPage.getTotalElements());
	}

	@Override
	public byte[] exportUnCheckInListToExcelAsByteArray(Long activateId, String params) {

		List<StaffDto> staffDtoList = new ArrayList<>();
		List<Map<String, Object>> usersNotChekedInList;

		try {
			if (params == null || params.isEmpty()) {
				usersNotChekedInList = assemblyCheckInDao.findUsersNotCheckedInByExcel(activateId);
			} else {
				usersNotChekedInList = assemblyCheckInDao.findUsersNotCheckedInByExcel(activateId, params);
			}

			if (!usersNotChekedInList.isEmpty()) {
				staffDtoList = usersNotChekedInList.stream().map(staff -> {
					StaffDto staffDto = new StaffDto();
					staffDto.setId((BigDecimal) staff.get("id"));
					staffDto.setUsername((String) staff.get("username"));
					staffDto.setEmail((String) staff.get("email"));
					staffDto.setMobileNo((String) staff.get("mobileNo"));
					staffDto.setName((String) staff.get("name"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						staffDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						staffDto.setIcnumber(passportNumber);
					} else {
						staffDto.setIcnumber(" ");
					}
					staffDto.setStaffId((String) staff.get("staffid"));
					staffDto.setDepartment((String) staff.get("deptName"));
					staffDto.setType((String) staff.get("visitor"));
					return staffDto;
				}).collect(Collectors.toList());
			}

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("UnCheckInList");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Username");
			headerRow.createCell(2).setCellValue("Email");
			headerRow.createCell(3).setCellValue("Mobile No");
			headerRow.createCell(4).setCellValue("Name");
			headerRow.createCell(5).setCellValue("IC/Passport Number");
			headerRow.createCell(6).setCellValue("Staff ID");
			headerRow.createCell(7).setCellValue("Department");
			headerRow.createCell(8).setCellValue("Type");

			int rowNum = 1;
			for (StaffDto staffDto : staffDtoList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(staffDto.getId().toString());
				row.createCell(1).setCellValue(staffDto.getUsername());
				row.createCell(2).setCellValue(staffDto.getEmail());
				row.createCell(3).setCellValue(staffDto.getMobileNo());
				row.createCell(4).setCellValue(staffDto.getName());
				row.createCell(5).setCellValue(staffDto.getIcnumber());
				row.createCell(6).setCellValue(staffDto.getStaffId());
				row.createCell(7).setCellValue(staffDto.getDepartment());
				row.createCell(8).setCellValue(staffDto.getType());
			}

			for (int i = 0; i < 9; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();
			return out.toByteArray();

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public byte[] exportCheckInListToExcelAsByteArray(Long activateId, String params) {
		// TODO Auto-generated method stub

		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();
		List<Map<String, Object>> usersCheckedInList;
		try {
			if (params == null || params.isEmpty()) {
				usersCheckedInList = assemblyCheckInDao.findUsersCheckedInByExcel(activateId);
			} else {
				usersCheckedInList = assemblyCheckInDao.findUsersCheckedInByExcel(activateId, params);
			}

			if (!usersCheckedInList.isEmpty()) {
				dashboardDetailDtoList = usersCheckedInList.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setId((BigDecimal) staff.get("id"));
					detailDto.setUsername((String) staff.get("username"));
					detailDto.setEmail((String) staff.get("email"));
					detailDto.setMobileNo((String) staff.get("mobileno"));
					String icNumber = (String) staff.get("icnumber");
					String passportNumber = (String) staff.get("passportnumber");
					if (icNumber != null && !icNumber.isEmpty()) {
						detailDto.setIcnumber(icNumber);
					} else if (passportNumber != null && !passportNumber.isEmpty()) {
						detailDto.setIcnumber(passportNumber);
					} else {
						detailDto.setIcnumber(" ");
					}
					detailDto.setStaffId((String) staff.get("staffid"));
					detailDto.setName((String) staff.get("name"));
					detailDto.setDepartment((String) staff.get("deptName"));
					detailDto.setType((String) staff.get("visitor"));
					detailDto.setCheckInDate((String) staff.get("currentdate"));
					detailDto.setCheckInTime((String) staff.get("currenttime"));
					detailDto.setAssemblyName((String) staff.get("assembly"));
					return detailDto;
				}).collect(Collectors.toList());
			}

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("UnCheckInList");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Username");
			headerRow.createCell(2).setCellValue("Email");
			headerRow.createCell(3).setCellValue("Mobile No");
			headerRow.createCell(4).setCellValue("Name");
			headerRow.createCell(5).setCellValue("IC/Passport Number");
			headerRow.createCell(6).setCellValue("Staff ID");
			headerRow.createCell(7).setCellValue("Department");
			headerRow.createCell(8).setCellValue("Type");

			int rowNum = 1;
			for (DashboardDetailDto staffDto : dashboardDetailDtoList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(staffDto.getId().toString());
				row.createCell(1).setCellValue(staffDto.getUsername());
				row.createCell(2).setCellValue(staffDto.getEmail());
				row.createCell(3).setCellValue(staffDto.getMobileNo());
				row.createCell(4).setCellValue(staffDto.getName());
				row.createCell(5).setCellValue(staffDto.getIcnumber());
				row.createCell(6).setCellValue(staffDto.getStaffId());
				row.createCell(7).setCellValue(staffDto.getDepartment());
				row.createCell(8).setCellValue(staffDto.getType());
			}

			for (int i = 0; i < 9; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();
			return out.toByteArray();

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public Page<NotiReadLogDto> searchByParams(int page, int size, String params, String sortBy, String direction,
			Long emergencyId) {
		// TODO Auto-generated method stub
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "u.name");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "u.staffid");
		} else if (sortBy.equals("ename")) {
			sort = Sort.by(sortDirection, "ec.name");
		} else {
			sort = Sort.by(sortDirection, "u.name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<NotiReadLogDto> readNotiList = new ArrayList<>();
		Page<Map<String, Object>> readNotiPage = null;
		logger.info("Searching noti read log entity: ");
		try {
			if (params == null || params.isEmpty()) {
				readNotiPage = notiReadLogDao.findByUserName(pageRequest, emergencyId);
			} else {
				readNotiPage = notiReadLogDao.findByUserName(pageRequest, params, emergencyId);
			}
			if (!readNotiPage.isEmpty()) {
				readNotiList = readNotiPage.stream().map(readNoti -> {
					NotiReadLogDto notiReadDto = new NotiReadLogDto();
					notiReadDto.setUserName((String) readNoti.get("username"));
					notiReadDto.setEmergencyName((String) readNoti.get("ename"));
					notiReadDto.setStaffId((String) readNoti.get("staffid"));
					notiReadDto.setReadNotiDate((String) readNoti.get("notidate"));
					notiReadDto.setReadNotiTiime((String) readNoti.get("notitime"));
					return notiReadDto;
				}).collect(Collectors.toList());
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

		return new PageImpl<>(readNotiList, pageRequest, readNotiPage.getTotalElements());
	}

	@Override
	public byte[] getAllNotiReadLog(Long activateId, String params) {
		// TODO Auto-generated method stub

		List<NotiReadLogDto> readNotiList = new ArrayList<>();
		List<Map<String, Object>> readNotiAllList;
		logger.info("Searching noti read log entity: ");
		try {
			if (params == null || params.isEmpty()) {
				readNotiAllList = notiReadLogDao.findByUserNameExcel(activateId);
			} else {
				readNotiAllList = notiReadLogDao.findByUserNameExcel(activateId, params);
			}

			if (!readNotiAllList.isEmpty()) {
				readNotiList = readNotiAllList.stream().map(readNoti -> {
					NotiReadLogDto notiReadDto = new NotiReadLogDto();
					notiReadDto.setUserName((String) readNoti.get("username"));
					notiReadDto.setEmergencyName((String) readNoti.get("ename"));
					notiReadDto.setStaffId((String) readNoti.get("staffid"));
					notiReadDto.setReadNotiDate((String) readNoti.get("notidate"));
					notiReadDto.setReadNotiTiime((String) readNoti.get("notitime"));
					return notiReadDto;
				}).collect(Collectors.toList());
			}

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("UnCheckInList");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("ID");
			headerRow.createCell(1).setCellValue("Date");
			headerRow.createCell(2).setCellValue("Time");
			headerRow.createCell(3).setCellValue("StaffId");
			headerRow.createCell(4).setCellValue("User Name");
			headerRow.createCell(5).setCellValue("Emergency Name");

			int rowNum = 1;
			for (NotiReadLogDto notiDto : readNotiList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(notiDto.getSyskey().toString());
				row.createCell(1).setCellValue(notiDto.getReadNotiDate());
				row.createCell(2).setCellValue(notiDto.getReadNotiTiime());
				row.createCell(3).setCellValue(notiDto.getStaffId());
				row.createCell(4).setCellValue(notiDto.getUserName());
				row.createCell(5).setCellValue(notiDto.getEmergencyName());
			}

			for (int i = 0; i < 9; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();
			return out.toByteArray();

		} catch (DataAccessException dae) {
			logger.info("Error searching role entity: " + dae.getMessage());
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			logger.info("Error searching role entity: " + e.getMessage());
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

	}

	@Override
	public Page<HeadCountDto> getTotalHeadCountReport(Long activateId, int page, int size, String sortBy,
			String direction, String params) {

//		String sortColumn;
//		switch (sortBy.toLowerCase()) {
//		case "fullname":
//			sortColumn = "FULLNAME";
//			break;
//		case "department":
//			sortColumn = "NAME";
//			break;
//		case "staffid":
//			sortColumn = "STAFFNO";
//			break;
//		case "type":
//			sortColumn = "VISITORORVIP";
//			break;
//		default:
//			sortColumn = "FULLNAME"; // Default to fullname if no valid sortBy
//		}
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

		//Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortColumn));
		
		//String sortDirection = direction.equalsIgnoreCase("asc") ? "ASC" : "DESC";
		
		PageRequest pageRequest = PageRequest.of(page, size);

		List<HeadCountDto> headCountList = new ArrayList<>();
		EmergencyActivate eActivate = new EmergencyActivate();
		Page<Map<String, Object>> usersNotCheckedInPage;
		String mainString = "";
		List<String> buildingNames = new ArrayList<>();

		try {
			Optional<EmergencyActivate> emergencyOptional = emergencyActivateDao.findById(activateId);
			if (emergencyOptional.isPresent()) {
				eActivate = emergencyOptional.get();
				List<Long> mainIds = new ArrayList<>();
				if (eActivate.getMainBuilding() != null && eActivate.getMainBuilding() != "") {
					mainIds = Arrays.stream(eActivate.getMainBuilding().split(",")).map(String::trim)
							.map(Long::parseLong).collect(Collectors.toList());
					List<Object[]> mainBuilding = locEmergencyDao.findByMainIds(mainIds);

					for (Object[] row : mainBuilding) {
						buildingNames.add((String) row[1]);
					}
				}
			}
			if(params == null || params.isEmpty()) {
				usersNotCheckedInPage = assemblyCheckInDao.findHeadCountReport(pageRequest, buildingNames);
			} else {				
				usersNotCheckedInPage = assemblyCheckInDao.findHeadCountReport(pageRequest, buildingNames, params);
			}
			
			if (!usersNotCheckedInPage.isEmpty()) {
				headCountList = usersNotCheckedInPage.stream().map(headCount -> {
					HeadCountDto headCountDto = new HeadCountDto();
					headCountDto.setDepartment((String) headCount.get("NAME"));
					headCountDto.setUsername((String) headCount.get("FULLNAME"));
					headCountDto.setType((String) headCount.get("VISITORORVIP"));
					headCountDto.setMobileNo((String) headCount.get("CONTACTNO"));
					headCountDto.setCompany((String) headCount.get("COMPANYNAME"));
					headCountDto.setStaffId((String) headCount.get("STAFFNO"));
					headCountDto.setIcnumber(assemblyCheckInDao.findICByUser(headCountDto.getStaffId()));
					return headCountDto;
				}).collect(Collectors.toList());
			}
			if ("name".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getUsername().isEmpty() ? null : dto.getUsername(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}
			if ("type".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getType().isEmpty() ? null : dto.getType(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}
			if ("icnumber".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getIcnumber().isEmpty() ? null : dto.getIcnumber(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}
			if ("staffid".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getStaffId().isEmpty() ? null : dto.getStaffId(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}
			if ("department".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getDepartment().isEmpty() ? null : dto.getDepartment(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}
			if ("mobileno".equalsIgnoreCase(sortBy)) {
				Comparator<HeadCountDto> comparator = Comparator.comparing(
						dto -> dto.getMobileNo().isEmpty() ? null : dto.getMobileNo(), // Treat empty strings as null
						Comparator.nullsLast(String::compareTo));

				if (sortDirection.isDescending()) {
					comparator = comparator.reversed();
				}
				headCountList.sort(comparator);
			}



		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		// return staffDtoList;
		return new PageImpl<>(headCountList, pageRequest, usersNotCheckedInPage.getTotalElements());
	}

}
