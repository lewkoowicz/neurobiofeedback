package com.lewkowicz.neurobiofeedbackapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long bookingId;

    @NotEmpty(message = "validation.emailNotEmpty")
    @Email(message = "validation.emailInvalid")
    private String email;

    @NotNull(message = "validation.bookingDateNotNull")
    private LocalDateTime bookingDate;

    @NotEmpty(message = "validation.fullNameNotEmpty")
    @Pattern(regexp = "^[a-zA-Z]+ [a-zA-Z]+$", message = "validation.fullNameInvalid")
    private String fullName;

    @NotEmpty(message = "validation.mobileNumberNotEmpty")
    @Pattern(regexp = "^[0-9]{9}$", message = "validation.mobileNumberInvalid")
    private String mobileNumber;

    public void setFullName(String fullName) {
        this.fullName = fullName != null ? fullName.trim() : null;
    }

}
