package com.emergency.rollcall.service.Impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.emergency.rollcall.entity.AssemblyCheckIn;
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
		} else if (sortBy.equals("type")) {
			sort = Sort.by(sortDirection, "type");
		} else if (sortBy.equals("icnumber")) {
			sort = Sort.by(sortDirection, "ic_number");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staff_no");
		} else if (sortBy.equals("department")) {
			sort = Sort.by(sortDirection, "department");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "contact_Number");
		} else if (sortBy.equals("assembly")) {
			sort = Sort.by(sortDirection, "a.name");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<DashboardDetailDto> dashboardDetailDtoList = new ArrayList<>();

		Page<Map<String, Object>> checkInPage;
		try {
			if (params == null || params.isEmpty()) {
				checkInPage = assemblyCheckInDao.findCheckedInByEmergency(activateId, pageRequest);

			} else {
				checkInPage = assemblyCheckInDao.findCheckedInByEmergency(activateId, pageRequest, params);

			}
			if (!checkInPage.isEmpty()) {
				dashboardDetailDtoList = checkInPage.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setUsername((String) staff.get("name"));
					detailDto.setName((String) staff.get("name"));
					detailDto.setMobileNo((String) staff.get("contact_Number"));
					detailDto.setIcnumber((String) staff.get("ic_number"));
					detailDto.setStaffId((String) staff.get("staff_no"));
					detailDto.setDepartment((String) staff.get("department"));
					detailDto.setType((String) staff.get("type"));
					detailDto.setCheckInDate((String) staff.get("currentdate"));
					detailDto.setCheckInTime((String) staff.get("currenttime"));
					detailDto.setAssemblyName((String) staff.get("AssemblyName"));
					return detailDto;
				}).collect(Collectors.toList());
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(dashboardDetailDtoList, pageRequest, checkInPage.getTotalElements());

	}

	@Override
	public Page<StaffDto> getAllUnCheckInList(Long activateId, int page, int size, String sortBy, String direction,
			String params) {
		EmergencyActivate emergencyActivate = emergencyActivateDao.findById(activateId).orElse(null);

		Page<AssemblyCheckIn> unCheckInPage;
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("type")) {
			sort = Sort.by(sortDirection, "type");
		} else if (sortBy.equals("icnumber")) {
			sort = Sort.by(sortDirection, "icNumber");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffNo");
		} else if (sortBy.equals("department")) {
			sort = Sort.by(sortDirection, "department");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "contactNumber");
		} else {
			sort = Sort.by(sortDirection, "name");
		}

		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<StaffDto> staffDtoList = new ArrayList<>();

		try {
			if (params == null || params.isEmpty()) {
				unCheckInPage = assemblyCheckInDao.getUnCheckInList(activateId, pageRequest);
			} else {
				unCheckInPage = assemblyCheckInDao.getUnCheckInList(activateId, pageRequest, params);
			}

			if (!unCheckInPage.isEmpty()) {
				staffDtoList = unCheckInPage.stream().map(staff -> {
					StaffDto staffDto = new StaffDto();
					staffDto.setSyskey(staff.getSyskey());
					staffDto.setUsername((String) staff.getName());
					staffDto.setMobileNo((String) staff.getContactNumber());
					staffDto.setName((String) staff.getName());
					staffDto.setIcnumber((String) staff.getIcNumber());
					staffDto.setDepartment(staff.getDepartment());
					staffDto.setType(staff.getType());
					staffDto.setStaffId(staff.getStaffNo());
					// staffDto.setLastEntryPoint(assemblyCheckInDao.getlocationVisited(staffDto.getStaffId()));
					return staffDto;
				}).collect(Collectors.toList());
			}
			System.out.println("List " + staffDtoList.size());

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(staffDtoList, pageRequest, unCheckInPage.getTotalElements());
	}

	@Override
	public byte[] exportUnCheckInListToExcelAsByteArray(Long activateId, String params) {

		List<StaffDto> staffDtoList = new ArrayList<>();
		List<Map<String, Object>> usersNotChekedInList;
		List<AssemblyCheckIn> unCheckInList = new ArrayList<>();
		try {
			if (params == null || params.isEmpty()) {
				unCheckInList = assemblyCheckInDao.getUnCheckInExcel(activateId);
			} else {
				unCheckInList = assemblyCheckInDao.getUnCheckInExcel(activateId, params);
			}

			if (!unCheckInList.isEmpty()) {
				staffDtoList = unCheckInList.stream().map(staff -> {
					StaffDto staffDto = new StaffDto();

					staffDto.setUsername((String) staff.getName());
					staffDto.setMobileNo((String) staff.getContactNumber());
					staffDto.setName((String) staff.getName());
					staffDto.setIcnumber((String) staff.getIcNumber());
					staffDto.setDepartment(staff.getDepartment());
					staffDto.setType(staff.getType());
					staffDto.setStaffId(staff.getStaffNo());
					// staffDto.setLastEntryPoint(assemblyCheckInDao.getlocationVisited(staffDto.getStaffId()));
					return staffDto;
				}).collect(Collectors.toList());
			}

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("UnCheckInList");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Username");
			headerRow.createCell(1).setCellValue("Email");
			headerRow.createCell(2).setCellValue("Mobile No");
			headerRow.createCell(3).setCellValue("Name");
			headerRow.createCell(4).setCellValue("IC/Passport Number");
			headerRow.createCell(5).setCellValue("Staff ID");
			headerRow.createCell(6).setCellValue("Department");
			headerRow.createCell(7).setCellValue("Type");

			int rowNum = 1;
			for (StaffDto staffDto : staffDtoList) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(staffDto.getUsername());
				row.createCell(1).setCellValue(staffDto.getEmail());
				row.createCell(2).setCellValue(staffDto.getMobileNo());
				row.createCell(3).setCellValue(staffDto.getName());
				row.createCell(4).setCellValue(staffDto.getIcnumber());
				row.createCell(5).setCellValue(staffDto.getStaffId());
				row.createCell(6).setCellValue(staffDto.getDepartment());
				row.createCell(7).setCellValue(staffDto.getType());
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
		List<AssemblyCheckIn> checkInList = new ArrayList<>();
		try {
			if (params == null || params.isEmpty()) {
				checkInList = assemblyCheckInDao.getCheckInList(activateId);

			} else {
				checkInList = assemblyCheckInDao.getCheckInList(activateId, params);

			}
			if (!checkInList.isEmpty()) {
				dashboardDetailDtoList = checkInList.stream().map(staff -> {
					DashboardDetailDto detailDto = new DashboardDetailDto();
					detailDto.setUsername((String) staff.getName());
					detailDto.setMobileNo((String) staff.getContactNumber());
					detailDto.setName((String) staff.getName());
					detailDto.setIcnumber((String) staff.getIcNumber());
					detailDto.setDepartment(staff.getDepartment());
					detailDto.setType(staff.getType());
					detailDto.setStaffId(staff.getStaffNo());
					return detailDto;
				}).collect(Collectors.toList());
			}

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("UnCheckInList");

			Row headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue("Username");
			headerRow.createCell(1).setCellValue("Email");
			headerRow.createCell(2).setCellValue("Mobile No");
			headerRow.createCell(3).setCellValue("Name");
			headerRow.createCell(4).setCellValue("IC/Passport Number");
			headerRow.createCell(5).setCellValue("Staff ID");
			headerRow.createCell(6).setCellValue("Department");
			headerRow.createCell(7).setCellValue("Type");

			int rowNum = 1;
			for (DashboardDetailDto staffDto : dashboardDetailDtoList) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(staffDto.getUsername());
				row.createCell(1).setCellValue(staffDto.getEmail());
				row.createCell(2).setCellValue(staffDto.getMobileNo());
				row.createCell(3).setCellValue(staffDto.getName());
				row.createCell(4).setCellValue(staffDto.getIcnumber());
				row.createCell(5).setCellValue(staffDto.getStaffId());
				row.createCell(6).setCellValue(staffDto.getDepartment());
				row.createCell(7).setCellValue(staffDto.getType());
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
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(sortDirection, sortBy);
		if (sortBy.equals("name")) {
			sort = Sort.by(sortDirection, "name");
		} else if (sortBy.equals("type")) {
			sort = Sort.by(sortDirection, "type");
		} else if (sortBy.equals("icnumber")) {
			sort = Sort.by(sortDirection, "icNumber");
		} else if (sortBy.equals("staffid")) {
			sort = Sort.by(sortDirection, "staffNo");
		} else if (sortBy.equals("department")) {
			sort = Sort.by(sortDirection, "department");
		} else if (sortBy.equals("mobileno")) {
			sort = Sort.by(sortDirection, "contactNumber");
		} else {
			sort = Sort.by(sortDirection, "name");
		}
		PageRequest pageRequest = PageRequest.of(page, size, sort);
		List<HeadCountDto> headCountList = new ArrayList<>();
		EmergencyActivate eActivate = new EmergencyActivate();
		Page<Map<String, Object>> usersNotCheckedInPage;
		Page<AssemblyCheckIn> headCountPage;

		try {
			Optional<EmergencyActivate> emergencyOptional = emergencyActivateDao.findById(activateId);
			if (emergencyOptional.isPresent()) {
				eActivate = emergencyOptional.get();
			}
			if (params == null || params.isEmpty()) {
				headCountPage = assemblyCheckInDao.getAllHeadCountReport(activateId, pageRequest);
			} else {
				headCountPage = assemblyCheckInDao.getAllHeadCountReport(activateId, pageRequest, params);
			}

			if (!headCountPage.isEmpty()) {
				headCountList = headCountPage.stream().map(headCount -> {
					HeadCountDto headCountDto = new HeadCountDto();
					headCountDto.setDepartment((String) headCount.getDepartment());
					headCountDto.setUsername((String) headCount.getName());
					headCountDto.setType((String) headCount.getType());
					headCountDto.setMobileNo((String) headCount.getContactNumber());
					headCountDto.setStaffId((String) headCount.getStaffNo());
					headCountDto.setIcnumber(assemblyCheckInDao.findICByUser(headCountDto.getStaffId()));
					return headCountDto;
				}).collect(Collectors.toList());
			}

		} catch (DataAccessException dae) {
			System.err.println("Database error occurred: " + dae.getMessage());
			throw new RuntimeException("Database error occurred, please try again later.", dae);
		} catch (Exception e) {
			System.err.println("An unexpected error occurred: " + e.getMessage());
			throw new RuntimeException("An unexpected error occurred, please try again later.", e);
		}

		return new PageImpl<>(headCountList, pageRequest, headCountPage.getTotalElements());
	}

}
