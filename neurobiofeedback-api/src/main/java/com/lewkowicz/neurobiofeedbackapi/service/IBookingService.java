package com.lewkowicz.neurobiofeedbackapi.service;

import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IBookingService {

    void createBooking(BookingDto bookingDto);

    List<BookingDto> fetchBooking(String email);

    List<BookingDto> fetchAllBookings();

    boolean updateBooking(BookingDto bookingDto);

    boolean deleteBooking(Long bookingId, String email);

    List<LocalDateTime> fetchBookedTimeslots(LocalDate date);
}
