package com.lewkowicz.neurobiofeedbackapi;

import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;
import com.lewkowicz.neurobiofeedbackapi.entity.Booking;
import com.lewkowicz.neurobiofeedbackapi.exception.BookingAlreadyExistsException;
import com.lewkowicz.neurobiofeedbackapi.exception.InvalidBookingDateException;
import com.lewkowicz.neurobiofeedbackapi.exception.ResourceNotFoundException;
import com.lewkowicz.neurobiofeedbackapi.repository.BookingRepository;
import com.lewkowicz.neurobiofeedbackapi.service.impl.BookingServiceImpl;
import com.lewkowicz.neurobiofeedbackapi.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingDate(LocalDateTime.now().plusDays(1));
        bookingDto.setEmail("test@example.com");
        when(bookingRepository.findByBookingDate(any())).thenReturn(Optional.empty());

        bookingService.createBooking(bookingDto);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBookingAlreadyExists() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingDate(LocalDateTime.now().plusDays(1));
        when(bookingRepository.findByBookingDate(any())).thenReturn(Optional.of(new Booking()));

        assertThrows(BookingAlreadyExistsException.class, () -> bookingService.createBooking(bookingDto));
    }

    @Test
    void testCreateBookingInvalidDate() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingDate(LocalDateTime.now().minusDays(1));

        assertThrows(InvalidBookingDateException.class, () -> bookingService.createBooking(bookingDto));
    }

    @Test
    void testFetchBooking() {
        String email = "test@example.com";
        List<Booking> bookings = Collections.singletonList(new Booking());
        when(bookingRepository.findByEmail(email)).thenReturn(bookings);

        List<BookingDto> result = bookingService.fetchBooking(email);

        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFetchAllBookings() {
        List<Booking> bookings = Collections.singletonList(new Booking());
        when(bookingRepository.findAll(any(Sort.class))).thenReturn(bookings);

        List<BookingDto> result = bookingService.fetchAllBookings();

        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAll(any(Sort.class));
    }

    @Test
    void testUpdateBooking() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(1L);
        bookingDto.setBookingDate(LocalDateTime.now().plusDays(1));
        bookingDto.setEmail("test@example.com");
        bookingDto.setFullName("John Doe");
        bookingDto.setMobileNumber("123456789");

        Booking existingBooking = new Booking();
        existingBooking.setBookingId(1L);
        existingBooking.setBookingDate(LocalDateTime.now().plusDays(2));
        existingBooking.setEmail("old@example.com");
        existingBooking.setFullName("Jane Doe");
        existingBooking.setMobileNumber("987654321");

        when(bookingRepository.findById(bookingDto.getBookingId())).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.findByBookingDate(any())).thenReturn(Optional.empty());

        boolean result = bookingService.updateBooking(bookingDto);

        assertTrue(result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testUpdateBookingNotFound() {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(1L);
        when(bookingRepository.findById(bookingDto.getBookingId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.updateBooking(bookingDto));
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;
        String email = "test@example.com";

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setEmail(email);
        booking.setBookingDate(LocalDateTime.now().plusDays(1));

        when(bookingRepository.existsById(bookingId)).thenReturn(true);
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        boolean result = bookingService.deleteBooking(bookingId, email);

        assertTrue(result);
        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void testDeleteBookingNotFound() {
        Long bookingId = 1L;
        String email = "test@example.com";
        when(bookingRepository.existsById(bookingId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookingService.deleteBooking(bookingId, email));
    }

    @Test
    void testFetchBookedTimeslots() {
        LocalDate date = LocalDate.now();
        List<Booking> bookings = Collections.singletonList(new Booking());
        when(bookingRepository.findByBookingDateBetween(any(), any())).thenReturn(bookings);

        List<LocalDateTime> result = bookingService.fetchBookedTimeslots(date);

        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByBookingDateBetween(any(), any());
    }
}
