package com.lewkowicz.neurobiofeedbackapi.service.impl;

import com.lewkowicz.neurobiofeedbackapi.constants.WorkingTimeConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.WorkingTimeDto;
import com.lewkowicz.neurobiofeedbackapi.entity.WorkingTime;
import com.lewkowicz.neurobiofeedbackapi.exception.ResourceNotFoundException;
import com.lewkowicz.neurobiofeedbackapi.mapper.WorkingTimeMapper;
import com.lewkowicz.neurobiofeedbackapi.repository.WorkingTimeRepository;
import com.lewkowicz.neurobiofeedbackapi.service.IWorkingTimeService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class WorkingTimeServiceImpl implements IWorkingTimeService {

    private WorkingTimeRepository workingTimeRepository;
    private final MessageSource messageSource;

    @Override
    public boolean updateWorkingTime(WorkingTimeDto workingTimeDto) {
        WorkingTime workingTime = workingTimeRepository.findById(workingTimeDto.getWorkingTimeId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(WorkingTimeConstants.WORKING_TIME_NOT_FOUND)));
        workingTime.setStartHour(workingTimeDto.getStartHour());
        workingTime.setEndHour(workingTimeDto.getEndHour());
        workingTime.setIntervalMinutes(workingTimeDto.getIntervalMinutes());
        workingTimeRepository.save(workingTime);
        return true;
    }

    @Override
    public WorkingTimeDto fetchWorkingTime(Long workingTimeId) {
        WorkingTime workingTime = workingTimeRepository.findById(workingTimeId)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(WorkingTimeConstants.WORKING_TIME_NOT_FOUND)));
        WorkingTimeDto workingTimeDto = new WorkingTimeDto();
        WorkingTimeMapper.mapToWorkingTimeDto(workingTime, workingTimeDto);
        return workingTimeDto;
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
