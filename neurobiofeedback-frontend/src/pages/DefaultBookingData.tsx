import {useEffect, useState} from 'react';
import {Alert, Button, FormInput} from "../components";
import {
    createDefaultBookingData,
    DefaultBookingData as defaultData,
    fetchDefaultBookingData,
    updateDefaultBookingData
} from '../services';
import {useAuth, useLanguage} from '../context';
import {translations} from "../translations/translations.ts";

const DefaultBookingData = () => {
    const { token, email } = useAuth();
    const {language} = useLanguage();
    const [fullName, setFullName] = useState('');
    const [mobileNumber, setMobileNumber] = useState('');
    const [defaultData, setDefaultData] = useState<defaultData | null>(null);
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);

    const t = translations[language];

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchDefaultBookingData(email, token, language);
                if (data) {
                    setFullName(data.fullName);
                    setMobileNumber(data.mobileNumber);
                    setDefaultData(data);
                }
            } catch (error) {
                console.error(error);
            }
        };

        fetchData().then(r => r);
    }, [email, token]);

    const handleConfirm = async () => {
        const data = { defaultBookingDataId: defaultData?.defaultBookingDataId, email, fullName, mobileNumber };
        try {
            if (defaultData) {
                const response = await updateDefaultBookingData(data, token, email, language);
                setAlertType('success');
                setAlertMessage(response.statusMsg);
                setAlertKey(prevKey => prevKey + 1);
            } else {
                const response = await createDefaultBookingData(data, token, email, language);
                setAlertType('success');
                setAlertMessage(response.statusMsg);
                setAlertKey(prevKey => prevKey + 1);
            }
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
                <h2 className="text-lg font-bold mb-4">{t.defaultBookingData.changeDefaultBookingData}</h2>
                <FormInput label={t.defaultBookingData.fullName} type={"string"} placeholder={""} value={fullName}
                           onChange={e => setFullName(e.target.value)}/>
                <FormInput label={t.defaultBookingData.mobileNumber} type={"string"} placeholder={""} value={mobileNumber}
                           onChange={e => setMobileNumber(e.target.value)}/>
                <Button className="btn-primary mt-4" text={t.defaultBookingData.submit} onClick={handleConfirm}></Button>
            </div>
        </div>
    );
};

export default DefaultBookingData;
