package com.lewkowicz.neurobiofeedbackapi.controller;

import com.lewkowicz.neurobiofeedbackapi.constants.WorkingTimeConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.ResponseDto;
import com.lewkowicz.neurobiofeedbackapi.dto.WorkingTimeDto;
import com.lewkowicz.neurobiofeedbackapi.service.IWorkingTimeService;
import jakarta.validation.Valid;
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
@RequestMapping(path ="/api/working-time", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
@AllArgsConstructor
public class WorkingTimeController {

    private final IWorkingTimeService workingTimeService;
    private final MessageSource messageSource;

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateWorkingTime(@Valid @RequestBody WorkingTimeDto workingTimeDto) {
        boolean isUpdated = workingTimeService.updateWorkingTime(workingTimeDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(WorkingTimeConstants.WORKING_TIME_UPDATE_SUCCESS)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), getMessage(WorkingTimeConstants.WORKING_TIME_UPDATE_FAIL)));
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<WorkingTimeDto> fetchWorkingTime(@RequestParam Long workingTimeId) {
        WorkingTimeDto workingTimeDto = workingTimeService.fetchWorkingTime(workingTimeId);
        return ResponseEntity.status(HttpStatus.OK).body(workingTimeDto);
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
