import {ResponseDto} from "./types/ResponseDto.ts";
import {apiConfig, BASE_URL} from "./apiConfig.ts";
import axios from "axios";
import {DefaultBookingData} from "./types/DefaultBookingData.ts";

export const createDefaultBookingData = async (data: DefaultBookingData, token: string, email: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post<ResponseDto>(`${BASE_URL}/default-booking-data/create?email=${email}`, data, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Wprowadzone dane są niewłaściwe.');
        }
        throw new Error('Network error');
    }
};

export const updateDefaultBookingData = async (data: DefaultBookingData, token: string, email: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.put<ResponseDto>(`${BASE_URL}/default-booking-data/update?email=${email}`, data, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Wprowadzone dane są niewłaściwe.');
        }
        throw new Error('Network error');
    }
};

export const fetchDefaultBookingData = async (email: string, token: string, language: string)=> {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<DefaultBookingData>(`${BASE_URL}/default-booking-data/fetch?email=${email}`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Nie udało się pobrać danych.');
        }
        throw new Error('Network error');
    }
};
