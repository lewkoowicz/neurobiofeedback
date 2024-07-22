package com.lewkowicz.neurobiofeedbackapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class WorkingTimeDto {

    private Long workingTimeId;

    @NotNull(message = "validation.startHourNotNull")
    @Min(value = 0, message = "validation.startHourMin")
    @Max(value = 23, message = "validation.startHourMax")
    private Integer startHour;

    @NotNull(message = "validation.endHourNotNull")
    @Min(value = 0, message = "validation.endHourMin")
    @Max(value = 23, message = "validation.endHourMax")
    private Integer endHour;

    @NotNull(message = "validation.intervalMinutesNotNull")
    @Min(value = 1, message = "validation.intervalMinutesMin")
    @Max(value = 180, message = "validation.intervalMinutesMax")
    private Integer intervalMinutes;

}
