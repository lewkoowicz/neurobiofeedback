package com.lewkowicz.neurobiofeedbackapi.service;

import com.lewkowicz.neurobiofeedbackapi.dto.DefaultBookingDataDto;

public interface IDefaultBookingDataService {

    void createDefaultBookingData(DefaultBookingDataDto defaultBookingDataDto);

    boolean updateDefaultBookingData(DefaultBookingDataDto defaultBookingDataDto);

    DefaultBookingDataDto fetchDefaultBookingData(String email);

}
