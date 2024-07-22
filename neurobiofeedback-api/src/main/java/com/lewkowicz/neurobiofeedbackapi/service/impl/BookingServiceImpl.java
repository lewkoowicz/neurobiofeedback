package com.lewkowicz.neurobiofeedbackapi.service.impl;

import com.lewkowicz.neurobiofeedbackapi.constants.BookingConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.BookingDto;
import com.lewkowicz.neurobiofeedbackapi.entity.Booking;
import com.lewkowicz.neurobiofeedbackapi.exception.BookingAlreadyExistsException;
import com.lewkowicz.neurobiofeedbackapi.exception.InvalidBookingDateException;
import com.lewkowicz.neurobiofeedbackapi.exception.ResourceNotFoundException;
import com.lewkowicz.neurobiofeedbackapi.mapper.BookingMapper;
import com.lewkowicz.neurobiofeedbackapi.repository.BookingRepository;
import com.lewkowicz.neurobiofeedbackapi.service.IBookingService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private BookingRepository bookingRepository;
    private EmailServiceImpl emailService;
    private MessageSource messageSource;

    @Override
    @Transactional
    public void createBooking(BookingDto bookingDto) {
        Optional<Booking> optionalBooking = bookingRepository.findByBookingDate(bookingDto.getBookingDate());
        if (optionalBooking.isPresent()) {
            throw new BookingAlreadyExistsException(getMessage(BookingConstants.BOOKING_ALREADY_EXISTS));
        }

        if (bookingDto.getBookingDate().isBefore(LocalDateTime.now())) {
            throw new InvalidBookingDateException(getMessage(BookingConstants.BOOKING_CANNOT_BE_FROM_THE_PAST));
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = bookingDto.getBookingDate().format(dateFormatter);
        String formattedTime = bookingDto.getBookingDate().format(timeFormatter);

        Locale locale = LocaleContextHolder.getLocale();

        try {
            emailService.sendBookingConfirmation(
                    bookingDto.getEmail(),
                    formattedDate,
                    formattedTime,
                    locale
            );
        } catch (MessagingException e) {
            e.getCause();
        }

        bookingRepository.save(createNewBooking(bookingDto));
    }

    private Booking createNewBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        return BookingMapper.mapToBooking(bookingDto, booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> fetchBooking(String email) {
        List<Booking> bookings = bookingRepository.findByEmail(email);

        return bookings.stream()
                .map(booking -> BookingMapper.mapToBookingDto(booking, new BookingDto()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> fetchAllBookings() {
        List<Booking> bookings = bookingRepository.findAll(Sort.by(Sort.Direction.ASC, "bookingId"));

        return bookings.stream()
                .map(booking -> BookingMapper.mapToBookingDto(booking, new BookingDto()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean updateBooking(BookingDto bookingDto) {
        Booking existingBooking = bookingRepository.findById(bookingDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException(getMessage(BookingConstants.BOOKING_NOT_FOUND)));

        Optional<Booking> bookingWithNewDate = bookingRepository.findByBookingDate(bookingDto.getBookingDate());
        if (bookingWithNewDate.isPresent() &&
                !bookingWithNewDate.get().getBookingId().equals(existingBooking.getBookingId()) &&
                (bookingDto.getFullName().equals(bookingWithNewDate.get().getFullName()) ||
                        bookingDto.getMobileNumber().equals(bookingWithNewDate.get().getMobileNumber()))) {
            throw new BookingAlreadyExistsException(getMessage(BookingConstants.BOOKING_ALREADY_EXISTS));
        }

        boolean isUpdated = false;
        if (!existingBooking.getEmail().equals(bookingDto.getEmail())) {
            existingBooking.setEmail(bookingDto.getEmail());
            isUpdated = true;
        }
        if (!existingBooking.getBookingDate().equals(bookingDto.getBookingDate())) {
            existingBooking.setBookingDate(bookingDto.getBookingDate());
            isUpdated = true;
        }
        if (!existingBooking.getFullName().equals(bookingDto.getFullName())) {
            existingBooking.setFullName(bookingDto.getFullName());
            isUpdated = true;
        }
        if (!existingBooking.getMobileNumber().equals(bookingDto.getMobileNumber())) {
            existingBooking.setMobileNumber(bookingDto.getMobileNumber());
            isUpdated = true;
        }

        if (!isUpdated) {
            throw new BookingAlreadyExistsException(getMessage(BookingConstants.BOOKING_ALREADY_EXISTS));
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = bookingDto.getBookingDate().format(dateFormatter);
        String formattedTime = bookingDto.getBookingDate().format(timeFormatter);
        String fullName = bookingDto.getFullName();
        String mobileNumber = bookingDto.getMobileNumber();

        Locale locale = LocaleContextHolder.getLocale();

        try {
            emailService.sendBookingUpdate(
                    bookingDto.getEmail(),
                    formattedDate,
                    formattedTime,
                    fullName,
                    mobileNumber,
                    locale
            );
        } catch (MessagingException e) {
            e.getCause();
        }

        bookingRepository.save(existingBooking);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteBooking(Long bookingId, String email) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new ResourceNotFoundException(getMessage(BookingConstants.BOOKING_NOT_FOUND));
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new ResourceNotFoundException(getMessage(BookingConstants.BOOKING_NOT_FOUND));
        }
        Booking booking = optionalBooking.get();
        BookingDto bookingDto = BookingMapper.mapToBookingDto(booking, new BookingDto());

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedDate = bookingDto.getBookingDate().format(dateFormatter);
        String formattedTime = bookingDto.getBookingDate().format(timeFormatter);

        Locale locale = LocaleContextHolder.getLocale();

        try {
            emailService.sendBookingCancel(
                    bookingDto.getEmail(),
                    formattedDate,
                    formattedTime,
                    locale
            );
        } catch (MessagingException e) {
            e.getCause();
        }

        bookingRepository.deleteById(bookingId);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> fetchBookedTimeslots(LocalDate date) {
        return bookingRepository.findByBookingDateBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream()
                .map(Booking::getBookingDate)
                .collect(Collectors.toList());
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
