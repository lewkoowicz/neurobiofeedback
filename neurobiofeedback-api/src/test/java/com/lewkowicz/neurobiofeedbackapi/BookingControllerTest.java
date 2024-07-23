package com.lewkowicz.neurobiofeedbackapi;

import com.lewkowicz.neurobiofeedbackapi.controller.BookingController;
import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;
import com.lewkowicz.neurobiofeedbackapi.dto.ResponseDto;
import com.lewkowicz.neurobiofeedbackapi.service.IBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @Mock
    private IBookingService iBookingService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        BookingDto bookingDto = new BookingDto();
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Success");

        ResponseEntity<ResponseDto> response = bookingController.createBooking(bookingDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(iBookingService, times(1)).createBooking(bookingDto);
    }

    @Test
    void testFetchBookingsByEmail() {
        String email = "test@example.com";
        List<BookingDto> bookingList = Collections.singletonList(new BookingDto());
        when(iBookingService.fetchBooking(email)).thenReturn(bookingList);

        ResponseEntity<List<BookingDto>> response = bookingController.fetchBookingsByEmail(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingList, response.getBody());
    }

    @Test
    void testFetchAllBookings() {
        List<BookingDto> bookingList = Collections.singletonList(new BookingDto());
        when(iBookingService.fetchAllBookings()).thenReturn(bookingList);

        ResponseEntity<List<BookingDto>> response = bookingController.fetchAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingList, response.getBody());
    }

    @Test
    void testUpdateBooking() {
        BookingDto bookingDto = new BookingDto();
        when(iBookingService.updateBooking(bookingDto)).thenReturn(true);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Success");

        ResponseEntity<ResponseDto> response = bookingController.updateBooking(bookingDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookingService, times(1)).updateBooking(bookingDto);
    }

    @Test
    void testUpdateBookingFailed() {
        BookingDto bookingDto = new BookingDto();
        when(iBookingService.updateBooking(bookingDto)).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Failed");

        ResponseEntity<ResponseDto> response = bookingController.updateBooking(bookingDto);

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        verify(iBookingService, times(1)).updateBooking(bookingDto);
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;
        String email = "test@example.com";
        when(iBookingService.deleteBooking(bookingId, email)).thenReturn(true);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Success");

        ResponseEntity<ResponseDto> response = bookingController.deleteBooking(bookingId, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(iBookingService, times(1)).deleteBooking(bookingId, email);
    }

    @Test
    void testDeleteBookingFailed() {
        Long bookingId = 1L;
        String email = "test@example.com";
        when(iBookingService.deleteBooking(bookingId, email)).thenReturn(false);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Failed");

        ResponseEntity<ResponseDto> response = bookingController.deleteBooking(bookingId, email);

        assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
        verify(iBookingService, times(1)).deleteBooking(bookingId, email);
    }

    @Test
    void testFetchBookedTimeslots() {
        LocalDate date = LocalDate.now();
        List<LocalDateTime> timeslots = Collections.singletonList(LocalDateTime.now());
        when(iBookingService.fetchBookedTimeslots(date)).thenReturn(timeslots);

        ResponseEntity<List<LocalDateTime>> response = bookingController.getBookedTimeslots(date);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timeslots, response.getBody());
    }

}
