package com.lewkowicz.neurobiofeedbackapi.mapper;

import com.lewkowicz.neurobiofeedbackapi.dto.WorkingTimeDto;
import com.lewkowicz.neurobiofeedbackapi.entity.WorkingTime;

public class WorkingTimeMapper {

    public static WorkingTimeDto mapToWorkingTimeDto(WorkingTime workingTime, WorkingTimeDto workingTimeDto) {
        workingTimeDto.setWorkingTimeId(workingTime.getWorkingTimeId());
        workingTimeDto.setStartHour(workingTime.getStartHour());
        workingTimeDto.setEndHour(workingTime.getEndHour());
        workingTimeDto.setIntervalMinutes(workingTime.getIntervalMinutes());
        return workingTimeDto;
    }

    public static WorkingTime mapToWorkingTime(WorkingTimeDto workingTimeDto, WorkingTime workingTime) {
        workingTime.setWorkingTimeId(workingTimeDto.getWorkingTimeId());
        workingTime.setStartHour(workingTimeDto.getStartHour());
        workingTime.setEndHour(workingTimeDto.getEndHour());
        workingTime.setIntervalMinutes(workingTimeDto.getIntervalMinutes());
        return workingTime;
    }

}
