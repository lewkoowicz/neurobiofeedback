import {createContext} from 'react';

interface AuthContextType {
    isLoggedIn: boolean;
    token: string | '';
    role: string | '';
    email: string | '';
    login: (email: string, password: string) => Promise<void>;
    signup: (email: string, password: string) => Promise<void>;
    logout: () => void;
    signedInWithGoogle: boolean;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);
