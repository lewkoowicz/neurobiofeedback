import axios from "axios";
import {apiConfig, BASE_URL, PasswordChangeDto} from "./index";

export const login = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/login`, { email, password }, { headers });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};

export const signup = async (email: string, password: string, language: string) => {
    const headers = apiConfig.getHeaders(language);
    try {
        const response = await axios.post(`${BASE_URL}/signup`, { email, password }, { headers });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};

export const changePassword = async (data: PasswordChangeDto, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(`${BASE_URL}/change-password?email=${encodeURIComponent(data.email)}`, data,{ headers });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};

export const deleteAccount = async (email: string, password: string, language: string, token: string) => {
    const headers = apiConfig.getHeaders(language, token);
    try {
        const response = await axios.post(`${BASE_URL}/delete-account?email=${encodeURIComponent(email)}`, { email, password }, { headers });
        return response.data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            throw new Error(error.response.data.errorMessage || 'Unknown error.');
        }
        throw new Error('Unknown error.');
    }
};
