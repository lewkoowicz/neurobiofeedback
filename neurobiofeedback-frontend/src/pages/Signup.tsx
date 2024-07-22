import {useState} from 'react';
import {Alert, Button, FormInput} from "../components";
import {BASE_URL, signup} from "../services";
import {useNavigate} from "react-router-dom";
import {useLanguage} from "../context";
import {translations} from "../translations/translations.ts";

const Signup = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);
    const navigate = useNavigate();
    const {language} = useLanguage();

    const t = translations[language];

    const handleSubmit = async () => {
        if (password !== confirmPassword) {
            setAlertType('error');
            setAlertMessage(t.signup.passwordsDontMatch);
            setAlertKey(prevKey => prevKey + 1);
            return;
        }
        try {
            const data = await signup(email, password, language);
            setAlertType('success');
            setAlertMessage(data.statusMsg);
            setAlertKey(prevKey => prevKey + 1);
            setTimeout(() => {
                navigate("/sign-in")
            }, 1500)
        } catch (error) {
            if (error instanceof Error) {
                setAlertType('error');
                setAlertMessage(error.message);
                setAlertKey(prevKey => prevKey + 1);
            }
        }
    };

    const handleGoogleSignUp = () => {
        window.location.href = `${BASE_URL}/oauth2/authorization/google`;
    };

    return (
        <div className="relative flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.signup.signup}</h1>
            {alertType && (
                <div className="fixed max-w-xs">
                    <Alert key={alertKey} type={alertType} message={alertMessage}/>
                </div>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.signup.email} type="email" placeholder="" value={email}
                           onChange={e => setEmail(e.target.value)}/>
                <FormInput label={t.signup.password} type="password" placeholder="" value={password}
                           onChange={e => setPassword(e.target.value)}/>
                <FormInput label={t.signup.confirmPassword} type="password" placeholder="" value={confirmPassword}
                           onChange={e => setConfirmPassword(e.target.value)}/>
                <Button className={"btn-primary mt-4"} onClick={handleSubmit} text={t.signup.signup}/>
                <Button className={"btn-outline mt-4 flex items-center justify-center"} onClick={handleGoogleSignUp} text="">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 488 512" className="w-6 h-6 mr-2">
                        <path d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123 24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0 135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z"/>
                    </svg>
                    {t.signup.signUpWithGoogle}
                </Button>
            </div>
        </div>
    );
};

export default Signup;
