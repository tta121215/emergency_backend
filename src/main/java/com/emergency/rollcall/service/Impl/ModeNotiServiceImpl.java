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
import org.springframework.stereotype.Service;
import com.emergency.rollcall.dao.ModeNotiDao;
import com.emergency.rollcall.dto.ResponseDto;
import com.emergency.rollcall.dto.ModeNotiDto;
import com.emergency.rollcall.entity.ModeNoti;
import com.emergency.rollcall.service.ModeNotiService;

@Service
public class ModeNotiServiceImpl implements ModeNotiService {

	@Autowired
	private ModeNotiDao modeNotiDao;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ResponseDto saveModeNoti(ModeNotiDto modeNotiDto) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();

		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		modeNoti = modelMapper.map(modeNotiDto, ModeNoti.class);

		modeNoti.setCreateddate(this.yyyyMMddFormat(strCreatedDate));

		if (modeNoti.getSyskey() == 0) {
			ModeNoti entityres = modeNotiDao.save(modeNoti);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Saved");
			}
		} else {
			ModeNoti entityres = modeNotiDao.save(modeNoti);
			if (entityres.getSyskey() > 0) {
				res.setStatus_code(200);
				res.setMessage("Successfully Updated");
			}
		}

		return res;
	}

	@Override
	public ModeNotiDto getById(long id) {
		ModeNotiDto ModeNotiDto = new ModeNotiDto();
		ModeNoti modeNoti = new ModeNoti();
		Optional<ModeNoti> ModeNotiOptional = modeNotiDao.findById(id);
		if (ModeNotiOptional.isPresent()) {
			modeNoti = ModeNotiOptional.get();
			ModeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
		}
		return ModeNotiDto;
	}

	@Override
	public ResponseDto updateModeNoti(ModeNotiDto ModeNotiDto) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();
		String createdDate;
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String strCreatedDate = dateTime.format(formatter);

		Optional<ModeNoti> ModeNotiOptional = modeNotiDao.findById(ModeNotiDto.getSyskey());
		if (ModeNotiOptional.isPresent()) {
			modeNoti = ModeNotiOptional.get();
			createdDate = modeNoti.getCreateddate();
			modeNoti = modelMapper.map(ModeNotiDto, ModeNoti.class);
			modeNoti.setCreateddate(createdDate);
			modeNoti.setModifieddate(this.yyyyMMddFormat(strCreatedDate));
			modeNotiDao.save(modeNoti);
			res.setStatus_code(200);
			res.setMessage("Successfully Updated");
		} else {
			res.setStatus_code(401);
			res.setMessage("Data does not found");
		}

		return res;
	}

	@Override
	public ResponseDto deleteModeNoti(long id) {
		ResponseDto res = new ResponseDto();
		ModeNoti modeNoti = new ModeNoti();

		Optional<ModeNoti> modeNotiOptional = modeNotiDao.findById(id);
		if (modeNotiOptional.isPresent()) {
			modeNoti = modeNotiOptional.get();
			modeNotiDao.delete(modeNoti);
			res.setStatus_code(200);
			res.setMessage("Successfully Deleted");
		} else {
			res.setStatus_code(401);
			res.setMessage("No data found");
		}

		return res;
	}

	@Override
	public Page<ModeNotiDto> searchByParams(int page, int size, String params) {
		PageRequest pageRequest = PageRequest.of(page, size);
		Page<ModeNoti> modeNotiList;
		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		if (params == null || params.isEmpty()) {
			modeNotiList = modeNotiDao.findByNameOrCode(pageRequest);
			if (modeNotiList != null) {
				for (ModeNoti modeNoti : modeNotiList) {
					ModeNotiDto modeNotiDto = new ModeNotiDto();
					modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
					modeNotiDtoList.add(modeNotiDto);
				}
			}

		} else {
			modeNotiList = modeNotiDao.findByNameOrCode(pageRequest, params);
			if (modeNotiList != null) {
				for (ModeNoti modeNoti : modeNotiList) {
					ModeNotiDto modeNotiDto = new ModeNotiDto();
					modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
					modeNotiDtoList.add(modeNotiDto);
				}
			}
		}

		return new PageImpl<>(modeNotiDtoList, pageRequest, modeNotiList.getTotalElements());
	}

	@Override
	public List<ModeNotiDto> getAllList() {

		List<ModeNotiDto> modeNotiDtoList = new ArrayList<>();
		List<ModeNoti> modeNotiList = new ArrayList<>();
		modeNotiList = modeNotiDao.findAll();
		if (modeNotiList != null) {
			for (ModeNoti modeNoti : modeNotiList) {
				ModeNotiDto modeNotiDto = new ModeNotiDto();
				modeNotiDto = modelMapper.map(modeNoti, ModeNotiDto.class);
				modeNotiDtoList.add(modeNotiDto);
			}
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
