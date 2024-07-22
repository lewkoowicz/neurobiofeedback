import {Alert, Button, FormInput} from "../components";
import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {useState} from "react";
import {deleteAccount} from "../services";

const DeleteAccount = () => {
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);
    const {email, token, logout} = useAuth();
    const {language} = useLanguage();

    const t = translations[language];

    const handleSubmit = async () => {
        if (password !== confirmPassword) {
            setAlertType('error');
            setAlertMessage(t.deleteAccount.passwordsDontMatch);
            setAlertKey(prevKey => prevKey + 1);
            return;
        }
        try {
            const data = await deleteAccount(email, password, language, token);
            setAlertType('success');
            setAlertMessage(data.statusMsg);
            setAlertKey(prevKey => prevKey + 1);
            setTimeout(() => {
                logout();
            }, 1500)
        } catch (error) {
            if (error instanceof Error) {
                setAlertType('error');
                setAlertMessage(error.message);
                setAlertKey(prevKey => prevKey + 1);
            }
        }
    };

    return (
        <div className="flex flex-col items-center justify-center p-4">
            {alertType && (
                <div className="fixed max-w-xs">
                    <Alert key={alertKey} type={alertType} message={alertMessage}/>
                </div>
            )}
            <div className="form-control w-full max-w-md p-8 bg-base-200 shadow-xl rounded-lg">
                <h2 className="text-lg font-bold mb-4">{t.deleteAccount.deleteAccount}</h2>
                <FormInput label={t.deleteAccount.password} type={"password"} placeholder={""} value={password}
                           onChange={e => setPassword(e.target.value)}/>
                <FormInput label={t.deleteAccount.confirmPassword} type={"password"} placeholder={""} value={confirmPassword}
                           onChange={e => setConfirmPassword(e.target.value)}/>
                <Button className="btn-primary mt-4" text={t.deleteAccount.submit} onClick={handleSubmit}></Button>
            </div>
        </div>
    )
};

export default DeleteAccount;
