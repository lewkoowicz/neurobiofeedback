package com.lewkowicz.neurobiofeedbackapi.controller;

import com.lewkowicz.neurobiofeedbackapi.constants.BookingConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;
import com.lewkowicz.neurobiofeedbackapi.dto.ResponseDto;
import com.lewkowicz.neurobiofeedbackapi.service.IBookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(path = "/api", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
public class BookingController {

    private final IBookingService iBookingService;
    private final MessageSource messageSource;

    public BookingController(IBookingService iBookingService, MessageSource messageSource) {
        this.iBookingService = iBookingService;
        this.messageSource = messageSource;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        iBookingService.createBooking(bookingDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), getMessage(BookingConstants.BOOKING_CREATE_SUCCESS)));
    }

    @GetMapping("/fetch")
    public ResponseEntity<List<BookingDto>> fetchBookingsByEmail(@RequestParam @Email String email) {
        List<BookingDto> bookingDto = iBookingService.fetchBooking(email);
        return ResponseEntity.status(HttpStatus.OK).body(bookingDto);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<BookingDto>> fetchAllBookings() {
        List<BookingDto> bookings = iBookingService.fetchAllBookings();
        return ResponseEntity.status(HttpStatus.OK).body(bookings);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateBooking(@Valid @RequestBody BookingDto bookingDto) {
        boolean isUpdated = iBookingService.updateBooking(bookingDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(BookingConstants.BOOKING_UPDATE_SUCCESS)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), getMessage(BookingConstants.BOOKING_UPDATE_FAIL)));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteBooking(@Valid @RequestParam Long bookingId, @RequestParam @Email String email) {
        boolean isDeleted = iBookingService.deleteBooking(bookingId, email);
        if (isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(BookingConstants.BOOKING_CANCEL_SUCCESS)));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(HttpStatus.EXPECTATION_FAILED.toString(), getMessage(BookingConstants.BOOKING_CANCEL_FAIL)));
        }
    }

    @GetMapping("/booked-timeslots")
    public ResponseEntity<List<LocalDateTime>> getBookedTimeslots(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<LocalDateTime> bookedTimeslots = iBookingService.fetchBookedTimeslots(date);
        return ResponseEntity.ok(bookedTimeslots);
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }
}
