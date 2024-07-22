import {Alert, Button, FormInput} from "../components";
import {useAuth, useLanguage} from "../context";
import {translations} from "../translations/translations.ts";
import {useState} from "react";
import {changePassword} from "../services";

const ChangePassword = () => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);
    const {email, token} = useAuth();
    const {language} = useLanguage();

    const t = translations[language]

    const handleSubmit = async () => {
        if (newPassword !== confirmPassword) {
            setAlertType('error');
            setAlertMessage(t.changePassword.passwordsDontMatch);
            setAlertKey(prevKey => prevKey + 1);
            return;
        }
        const passwordChangeData = { email, oldPassword, newPassword }
        try {
            const data = await changePassword(passwordChangeData, language, token);
            setAlertType('success');
            setAlertMessage(data.statusMsg);
            setAlertKey(prevKey => prevKey + 1);
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
                <h2 className="text-lg font-bold mb-4">{t.changePassword.changePassword}</h2>
                <FormInput label={t.changePassword.oldPassword} type={"password"} placeholder={""} value={oldPassword}
                           onChange={e => setOldPassword(e.target.value)}/>
                <FormInput label={t.changePassword.newPassword} type={"password"} placeholder={""} value={newPassword}
                           onChange={e => setNewPassword(e.target.value)}/>
                <FormInput label={t.changePassword.confirmNewPassword} type={"password"} placeholder={""} value={confirmPassword}
                           onChange={e => setConfirmPassword(e.target.value)}/>
                <Button className="btn-primary mt-4" text={t.changePassword.submit} onClick={handleSubmit}></Button>
            </div>
        </div>
    )
}

export default ChangePassword;
