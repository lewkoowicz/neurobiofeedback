import {Button} from "../components";
import {useAuth, useLanguage} from "../context";
import {useNavigate} from "react-router-dom";
import {translations} from "../translations/translations.ts";

const Settings = () => {
    const { role, signedInWithGoogle } = useAuth();
    const navigate = useNavigate();
    const { language } = useLanguage();

    const t = translations[language];

    function handleChangeWorkingTime() {
        navigate("/working-time")
    }

    function handleDefaultBookingData() {
        navigate("/default-booking-data")
    }

    function handleChangePassword() {
        navigate("/change-password")
    }

    function handleDeleteAccount() {
        navigate("/delete-account")
    }

    return (
        <div className="flex flex-col items-center justify-center p-4">
            <div className="form-control w-full max-w-md p-8 bg-base-200 shadow-xl rounded-lg">
                <h2 className="text-lg font-bold mb-4">{t.settings.settings}</h2>
                {role === "ROLE_ADMIN" && (
                    <div className="relative group">
                        <Button className="btn-neutral w-full mt-4" text={t.settings.changeWorkingTime}
                                onClick={handleChangeWorkingTime}></Button>
                    </div>
                )}
                <div className="relative group">
                    <Button className="btn-primary w-full mt-4" text={t.settings.changeDefaultBookingData}
                            onClick={handleDefaultBookingData}></Button>
                </div>
                <div className="relative group">
                    <Button className="btn-accent w-full mt-4" text={t.settings.changePassword}
                            onClick={handleChangePassword} disabled={true}/>
                    {signedInWithGoogle && (
                        <span
                            className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 hidden group-hover:block bg-gray-700 text-white text-xs rounded py-2 px-3 whitespace-nowrap">
                            {t.settings.notAvailable}
                        </span>
                    )}
                </div>
                <div className="relative group">
                    <Button className="btn-error w-full mt-4" text={t.settings.deleteAccount}
                            onClick={handleDeleteAccount} disabled={true}/>
                    {signedInWithGoogle && (
                        <span
                            className="absolute bottom-full left-1/2 transform -translate-x-1/2 mb-2 hidden group-hover:block bg-gray-700 text-white text-xs rounded py-2 px-3 whitespace-nowrap">
                            {t.settings.notAvailable}
                        </span>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Settings;
