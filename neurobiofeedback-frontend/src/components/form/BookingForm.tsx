import React, {useEffect, useState} from 'react';
import {useLocation, useNavigate} from "react-router-dom";
import {useAuth, useLanguage} from "../../context";
import {generateTimeSlots} from "../../utils";
import {Alert, Button, FormInput, FormSelect} from "../index.ts";
import {
    createBooking,
    createDefaultBookingData,
    DefaultBookingData,
    fetchBookedTimeslots,
    fetchDefaultBookingData,
    fetchWorkingTime,
    updateBooking
} from '../../services';
import {translations} from "../../translations/translations.ts";

const BookingForm = () => {
    const location = useLocation();
    const { token, email: loggedInEmail, isLoggedIn, role } = useAuth();
    const {language} = useLanguage();
    const navigate = useNavigate();
    const searchParams = new URLSearchParams(location.search);
    const bookingId = parseInt(searchParams.get('id') || '0');
    const emailParam = searchParams.get('email');
    const fullNameParam = searchParams.get('fullName');
    const mobileNumberParam = searchParams.get('mobileNumber');
    const dateParam = searchParams.get('date');
    const timeParam = searchParams.get('time');

    const [email, setEmail] = useState(loggedInEmail || '');
    const [fullName, setFullName] = useState( fullNameParam || '');
    const [mobileNumber, setMobileNumber] = useState((mobileNumberParam || '').replace(/\s+/g, ''));
    const [date, setDate] = useState(dateParam || '');
    const [time, setTime] = useState(timeParam || '');
    const [alertType, setAlertType] = useState<'success' | 'error' | 'update' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [timeOptions, setTimeOptions] = useState<string[]>([]);
    const [bookedTimeslots, setBookedTimeslots] = useState<string[]>([]);
    const [alertKey, setAlertKey] = useState(0);
    const [rememberData, setRememberData] = useState(false);
    const [defaultBookingData, setDefaultBookingData] = useState<DefaultBookingData | null>(null);

    const t = translations[language];

    const isEditMode = Boolean(emailParam && dateParam && timeParam && fullNameParam && mobileNumberParam);
    const today = new Date().toISOString().split('T')[0];
    const now = new Date();
    const currentHour = now.getHours();

    const isWeekend = (dateStr: string) => {
        const date = new Date(dateStr);
        const day = date.getDay();
        return day === 0 || day === 6;
    };

    const formatDate = (dateStr: string) => {
        const [day, month, year] = dateStr.split('-');
        return `${year}-${month}-${day}`;
    };

    useEffect(() => {
        const fetchDefaultData = async () => {
            if (!isLoggedIn) return;
            try {
                const fetchedData = await fetchDefaultBookingData(email, token, language);
                if (!isEditMode) {
                    setFullName(fetchedData.fullName);
                    setDefaultBookingData(fetchedData);
                    setMobileNumber(fetchedData.mobileNumber);
                }
            } catch (error) {
                console.error(error);
            }
        };
        fetchDefaultData().then(r => r);
    }, [token, email, isLoggedIn]);

    useEffect(() => {
        if (dateParam) {
            setDate(formatDate(dateParam));
        }
    }, [dateParam]);

    useEffect(() => {
        if (isEditMode && role === "ROLE_ADMIN" && emailParam) {
            setEmail(emailParam);
        }
    }, [isEditMode, role, emailParam]);

    useEffect(() => {
        fetchWorkingTime(1, token)
            .then(data => {
                if (date === today) {
                    setTimeOptions(generateTimeSlots(currentHour + 1, data.endHour, data.intervalMinutes));
                } else {
                    setTimeOptions(generateTimeSlots(data.startHour, data.endHour, data.intervalMinutes));
                }
            })
            .catch(error => console.error('Failed to fetch working time', error));
    }, [token, date, today, currentHour]);

    useEffect(() => {
        if (date) {
            fetchBookedTimeslots(date, token, language).then(timeslots => {
                setBookedTimeslots(timeslots.map(dt => dt.split('T')[1].substring(0, 5)));
            }).catch(error => {
                console.error('Failed to fetch booked timeslots', error);
            });
        }
    }, [date, token]);

    const handleSubmit = async () => {
        const bookingDate = `${date}T${time}:00`;
        const bookingData = { bookingId, email, bookingDate, fullName, mobileNumber };

        try {
            const response = isEditMode ? await updateBooking(bookingData, token, language) : await createBooking(bookingData, token, language);
            if (rememberData) {
                const defaultData = { email, fullName, mobileNumber };
                await createDefaultBookingData(defaultData, token, email, language);
            }
            setAlertType(isEditMode ? 'update' : 'success');
            setAlertMessage(response.statusMsg);
            setAlertKey(prevKey => prevKey + 1);
            setTimeout(() => {
                navigate("/bookings")
            }, 2000)
        } catch (error) {
            if (error instanceof Error) {
                setAlertType('error');
                setAlertMessage(error.message);
                setAlertKey(prevKey => prevKey + 1);
            }
        }
    };

    const handleDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selectedDate = e.target.value;
        if (!isWeekend(selectedDate)) {
            setDate(selectedDate);
        } else {
            setAlertType("error");
            setAlertMessage("Rezerwacja nie może być złożona w danym dniu.");
            setAlertKey(prevKey => prevKey + 1);
        }
    };

    const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setRememberData(e.target.checked);
    };

    return (
        <div className="flex flex-col items-center justify-center w-full h-full text-base-content gap-4">
            {alertType && (
                <div className="fixed max-w-xs">
                    <Alert key={alertKey} type={alertType} message={alertMessage}/>
                </div>
            )}
            <div className="form-control w-full max-w-xs p-8 bg-base-200 shadow-xl rounded-lg">
                <FormInput label={t.bookingForm.email} type="email" placeholder={t.bookingForm.enterEmail}
                           value={role === "ROLE_ADMIN" || !isLoggedIn ? email : loggedInEmail}
                           onChange={e => setEmail(e.target.value)}
                           disabled={(isEditMode || isLoggedIn) && role !== "ROLE_ADMIN"}/>
                <FormInput label={t.bookingForm.fullName} type="string" placeholder={t.bookingForm.enterFullName} value={fullName}
                           onChange={e => setFullName(e.target.value)}/>
                <FormInput label={t.bookingForm.mobileNumber} type="string" placeholder={t.bookingForm.enterMobileNumber} value={mobileNumber}
                           onChange={e => setMobileNumber(e.target.value.replace(/\s+/g, ''))}/>
                {isLoggedIn && !defaultBookingData && !isEditMode && <div className="form-checkbox mt-4">
                    <label>
                        <input type="checkbox" checked={rememberData} onChange={handleCheckboxChange}/>
                        <span className={"ml-2"}>{t.bookingForm.rememberMyData}</span>
                    </label>
                </div>}
                <FormInput label={t.bookingForm.day} type="date" placeholder="" value={date} min={today}
                           onChange={handleDateChange}/>
                <FormSelect label={t.bookingForm.hour} options={timeOptions} placeholder={"--:--"} value={time}
                            onChange={e => setTime(e.target.value)} bookedTimeslots={bookedTimeslots}/>
                <Button className={"btn-primary mt-4"} onClick={handleSubmit}
                        text={isEditMode ? t.bookingForm.update : t.bookingForm.submit}/>
            </div>
        </div>
    );
};

export default BookingForm;
