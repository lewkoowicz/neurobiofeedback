package com.lewkowicz.neurobiofeedbackapi.mapper;

import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;
import com.lewkowicz.neurobiofeedbackapi.entity.Booking;

public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking, BookingDto bookingDto) {
        bookingDto.setBookingId(booking.getBookingId());
        bookingDto.setEmail(booking.getEmail());
        bookingDto.setBookingDate(booking.getBookingDate());
        bookingDto.setFullName(booking.getFullName());
        bookingDto.setMobileNumber(booking.getMobileNumber());
        return bookingDto;
    }

    public static Booking mapToBooking(BookingDto bookingDto, Booking booking) {
        booking.setBookingId(bookingDto.getBookingId());
        booking.setEmail(bookingDto.getEmail());
        booking.setBookingDate(bookingDto.getBookingDate());
        booking.setFullName(bookingDto.getFullName());
        booking.setMobileNumber(bookingDto.getMobileNumber());
        return booking;
    }

}
