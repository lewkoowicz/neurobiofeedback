import axios from "axios";
import {apiConfig, BASE_URL, ResponseDto, WorkingTime} from "./index";

export const updateWorkingTime = async (data: WorkingTime, token: string, language: string): Promise<ResponseDto> => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.put<ResponseDto>(`${BASE_URL}/working-time/update`, data, {
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

export const fetchWorkingTime = async (workingTimeId: number, language: string, token?: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.get<WorkingTime>(`${BASE_URL}/working-time/fetch?workingTimeId=${workingTimeId}`, {
            headers,
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Failed to fetch bookings.');
        }
        throw new Error('Network error');
    }
};
