package com.lewkowicz.neurobiofeedbackapi.controller;

import com.lewkowicz.neurobiofeedbackapi.constants.DefaultBookingDataConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.DefaultBookingDataDto;
import com.lewkowicz.neurobiofeedbackapi.dto.ResponseDto;
import com.lewkowicz.neurobiofeedbackapi.service.IDefaultBookingDataService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(path ="/api/default-booking-data", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
@AllArgsConstructor
public class DefaultBookingDataController {

    private final IDefaultBookingDataService defaultBookingDataService;
    private final MessageSource messageSource;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createDefaultBookingData(@Validated @RequestBody DefaultBookingDataDto defaultBookingDataDto) {
        defaultBookingDataService.createDefaultBookingData(defaultBookingDataDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), getMessage(DefaultBookingDataConstants.DEFAULT_BOOKING_DATA_CREATED)));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateDefaultBookingData(@Validated @RequestBody DefaultBookingDataDto defaultBookingDataDto) {
        boolean isUpdated = defaultBookingDataService.updateDefaultBookingData(defaultBookingDataDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(DefaultBookingDataConstants.DEFAULT_BOOKING_DATA_UPDATED)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), getMessage(DefaultBookingDataConstants.DEFAULT_BOOKING_DATA_UPDATE_FAIL)));
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<DefaultBookingDataDto> fetchDefaultBookingData(@RequestParam String email) {
        DefaultBookingDataDto defaultBookingDataDto = defaultBookingDataService.fetchDefaultBookingData(email);
        return ResponseEntity.status(HttpStatus.OK).body(defaultBookingDataDto);
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
