import axios from "axios";
import {apiConfig, BASE_URL, Booking, ResponseDto} from "./index";

export const fetchAllBookings = async (token: string, language: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<Booking[]>(`${BASE_URL}/fetchAll`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};

export const fetchBookingsByEmail = async (email: string, token: string, language: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<Booking[]>(`${BASE_URL}/fetch?email=${email}`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};

export const createBooking = async (data: Booking, token: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post<ResponseDto>(`${BASE_URL}/create`, data, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            const errorMessage = language === 'pl' ? 'Nie udało się dokonać rezerwacji.' : 'It was not possible to make a booking.';
            throw new Error(error.response.data.errorMessage || errorMessage);
        }
        throw new Error('Network error');
    }
};

export const updateBooking = async (data: Booking, token: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.put<ResponseDto>(`${BASE_URL}/update?email=${encodeURIComponent(data.email)}`, data, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};

export const deleteBooking = async (bookingId: number, email: string, token: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.delete(`${BASE_URL}/delete?bookingId=${bookingId}&email=${encodeURIComponent(email)}`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};

export const fetchBookedTimeslots = async (date: string, token: string, language: string): Promise<string[]> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<string[]>(`${BASE_URL}/booked-timeslots?date=${date}`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error occurred');
        }
        throw new Error('Network error');
    }
};
