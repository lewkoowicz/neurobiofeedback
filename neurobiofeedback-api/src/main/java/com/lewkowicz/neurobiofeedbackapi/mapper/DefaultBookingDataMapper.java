package com.lewkowicz.neurobiofeedbackapi.mapper;

import com.lewkowicz.neurobiofeedbackapi.dto.DefaultBookingDataDto;
import com.lewkowicz.neurobiofeedbackapi.entity.DefaultBookingData;

public class DefaultBookingDataMapper {

    public static DefaultBookingDataDto mapToDefaultBookingDataDto(DefaultBookingData defaultBookingData, DefaultBookingDataDto defaultBookingDataDto) {
        defaultBookingDataDto.setDefaultBookingDataId(defaultBookingData.getDefaultBookingDataId());
        defaultBookingDataDto.setEmail(defaultBookingData.getEmail());
        defaultBookingDataDto.setFullName(defaultBookingData.getFullName());
        defaultBookingDataDto.setMobileNumber(defaultBookingData.getMobileNumber());
        return defaultBookingDataDto;
    }

    public static DefaultBookingData mapToDefaultBookingData(DefaultBookingDataDto defaultBookingDataDto, DefaultBookingData defaultBookingData) {
        defaultBookingData.setDefaultBookingDataId(defaultBookingDataDto.getDefaultBookingDataId());
        defaultBookingData.setEmail(defaultBookingDataDto.getEmail());
        defaultBookingData.setFullName(defaultBookingDataDto.getFullName());
        defaultBookingData.setMobileNumber(defaultBookingDataDto.getMobileNumber());
        return defaultBookingData;
    }

}
