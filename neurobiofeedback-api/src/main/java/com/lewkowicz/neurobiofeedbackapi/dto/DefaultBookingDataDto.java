package com.lewkowicz.neurobiofeedbackapi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DefaultBookingDataDto {

    private Long defaultBookingDataId;

    private String email;

    @NotEmpty(message = "validation.fullNameNotEmpty")
    @Pattern(regexp = "^[a-zA-Z]+ [a-zA-Z]+$", message = "validation.fullNameInvalid")
    private String fullName;

    @NotEmpty(message = "validation.mobileNumberNotEmpty")
    @Pattern(regexp = "^[0-9]{9}$", message = "validation.mobileNumberInvalid")
    private String mobileNumber;

}
