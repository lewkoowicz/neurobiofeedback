import {useState} from "react";
import {Alert, Button, FormInput} from "../components";
import {useAuth, useLanguage} from "../context";
import {updateWorkingTime} from "../services";
import {translations} from "../translations/translations.ts";

const WorkingTime = () => {
    const [startHour, setStartHour] = useState('');
    const [endHour, setEndHour] = useState('');
    const [intervalMinutes, setIntervalMinutes] = useState('');
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);
    const { token } = useAuth();
    const {language} = useLanguage();

    const t = translations[language];

    const handleSubmit = async () => {
        const data = {
            workingTimeId: 1,
            startHour: parseInt(startHour),
            endHour: parseInt(endHour),
            intervalMinutes: parseInt(intervalMinutes)
        };

        try {
            const response = await updateWorkingTime(data, token, language);
            setAlertType('success');
            setAlertMessage(response.statusMsg);
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
                <h2 className="text-lg font-bold mb-4">{t.workingTime.changeWorkingTime}</h2>
                <FormInput label={t.workingTime.startHour} type={"number"} placeholder={""} value={startHour}
                           onChange={e => setStartHour(e.target.value)}/>
                <FormInput label={t.workingTime.endHour} type={"number"} placeholder={""} value={endHour}
                           onChange={e => setEndHour(e.target.value)}/>
                <FormInput label={t.workingTime.visitDuration} type={"number"} placeholder={""} value={intervalMinutes}
                           onChange={e => setIntervalMinutes(e.target.value)}/>
                <Button className="btn-primary mt-4" text={t.workingTime.submit} onClick={handleSubmit}></Button>
            </div>
        </div>
    );
};

export default WorkingTime;
