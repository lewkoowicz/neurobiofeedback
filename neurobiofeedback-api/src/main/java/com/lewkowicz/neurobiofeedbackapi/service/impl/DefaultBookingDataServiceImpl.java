package com.lewkowicz.neurobiofeedbackapi.service.impl;

import com.lewkowicz.neurobiofeedbackapi.constants.DefaultBookingDataConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.DefaultBookingDataDto;
import com.lewkowicz.neurobiofeedbackapi.entity.DefaultBookingData;
import com.lewkowicz.neurobiofeedbackapi.exception.ResourceNotFoundException;
import com.lewkowicz.neurobiofeedbackapi.mapper.DefaultBookingDataMapper;
import com.lewkowicz.neurobiofeedbackapi.repository.DefaultBookingDataRepository;
import com.lewkowicz.neurobiofeedbackapi.service.IDefaultBookingDataService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DefaultBookingDataServiceImpl  implements IDefaultBookingDataService {

    private DefaultBookingDataRepository defaultBookingDataRepository;

    @Override
    public void createDefaultBookingData(DefaultBookingDataDto defaultBookingDataDto) {
        defaultBookingDataRepository.save(createNewBookingData(defaultBookingDataDto));
    }

    private DefaultBookingData createNewBookingData(DefaultBookingDataDto defaultBookingDataDto) {
        DefaultBookingData defaultBookingData = new DefaultBookingData();
        return DefaultBookingDataMapper.mapToDefaultBookingData(defaultBookingDataDto, defaultBookingData);
    }

    @Override
    public boolean updateDefaultBookingData(DefaultBookingDataDto defaultBookingDataDto) {
        DefaultBookingData existingDefaultBookingData = defaultBookingDataRepository.findById(
                defaultBookingDataDto.getDefaultBookingDataId())
                .orElseThrow(() -> new ResourceNotFoundException(DefaultBookingDataConstants.DEFAULT_BOOKING_DATA_NOT_FOUND));
        existingDefaultBookingData.setFullName(defaultBookingDataDto.getFullName());
        existingDefaultBookingData.setMobileNumber(defaultBookingDataDto.getMobileNumber());
        defaultBookingDataRepository.save(existingDefaultBookingData);
        return true;
    }

    @Override
    public DefaultBookingDataDto fetchDefaultBookingData(String email) {
        DefaultBookingData defaultBookingData = defaultBookingDataRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(DefaultBookingDataConstants.DEFAULT_BOOKING_DATA_NOT_FOUND));
        DefaultBookingDataDto defaultBookingDataDto = new DefaultBookingDataDto();
        DefaultBookingDataMapper.mapToDefaultBookingDataDto(defaultBookingData, defaultBookingDataDto);
        return defaultBookingDataDto;
    }

}
