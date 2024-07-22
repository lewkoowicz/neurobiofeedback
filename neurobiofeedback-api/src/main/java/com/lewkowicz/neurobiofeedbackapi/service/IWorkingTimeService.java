package com.lewkowicz.neurobiofeedbackapi.service;

import com.lewkowicz.neurobiofeedbackapi.dto.WorkingTimeDto;

public interface IWorkingTimeService {

    boolean updateWorkingTime(WorkingTimeDto workingTimeDto);

    WorkingTimeDto fetchWorkingTime(Long workingTimeId);

}
