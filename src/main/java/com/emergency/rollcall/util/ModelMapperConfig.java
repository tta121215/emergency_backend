package com.emergency.rollcall.util;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.emergency.rollcall.dto.NotificationDto;
import com.emergency.rollcall.entity.Notification;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        
     // Custom mapping for modeNotiList
//        modelMapper.createTypeMap(Notification.class, NotificationDto.class)
//                   .addMappings(mapper -> mapper.map(Notification::getModeNotiList, NotificationDto::setModeNotiDto));

        return modelMapper;
    }
}

