import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useAuth, useLanguage} from "../../context";
import {Alert, BookingCard, Button} from "../index.ts";
import {deleteBooking, fetchAllBookings, fetchBookingsByEmail} from '../../services';
import {translations} from "../../translations/translations.ts";

interface Booking {
    bookingId: number;
    day: string;
    time: string;
    email: string;
    fullName: string;
    mobileNumber: string;
    status: 'active' | 'completed';
}

const Bookings = () => {
    const [bookingsData, setBookingsData] = useState<Booking[]>([]);
    const [uniqueDates, setUniqueDates] = useState<string[]>([]);
    const [filterDate, setFilterDate] = useState('');
    const [filterName, setFilterName] = useState('');
    const [sortOrder, setSortOrder] = useState('asc');
    const [alertType, setAlertType] = useState<'success' | 'error' | ''>('');
    const [alertMessage, setAlertMessage] = useState('');
    const [alertKey, setAlertKey] = useState(0);
    const { token, role, email, isLoggedIn } = useAuth();
    const navigate = useNavigate();
    const {language} = useLanguage();

    const t = translations[language];

    useEffect(() => {
        fetchBookings().then(r => r);
    }, [token, role, email, isLoggedIn]);

    const fetchBookings = async () => {
        try {
            let bookings;
            if (role === 'ROLE_ADMIN') {
                bookings = await fetchAllBookings(token, language);
            } else {
                bookings = await fetchBookingsByEmail(email, token, language);
            }
            const formattedBookings = formatBookings(bookings);
            setBookingsData(formattedBookings);
            setUniqueDates(() => {
                const now = new Date();
                now.setSeconds(0, 0);
                const futureDates = formattedBookings
                    .filter(booking => new Date(booking.day.split('-').reverse().join('-') + 'T' + booking.time) > now)
                    .map(booking => booking.day);
                const uniqueFutureDates = [...new Set(futureDates)];
                uniqueFutureDates.sort((a, b) => new Date(a.split('-').reverse().join('-')).getTime() - new Date(b.split('-').reverse().join('-')).getTime());
                return uniqueFutureDates;
            });
        } catch (error) {
            console.error('Error fetching bookings:', error);
        }
    };

    const formatBookings = (bookings: { bookingId: number; bookingDate: string; email: string; fullName: string, mobileNumber: string }[]) => {
        const now = new Date();
        now.setSeconds(0, 0);
        return bookings.map(({ bookingId, bookingDate, email, fullName, mobileNumber }) => {
            const bookingDateTime = new Date(bookingDate);
            const day = bookingDate.split('T')[0].split('-').reverse().join('-');
            const time = bookingDate.split('T')[1].substring(0, 5);
            const status = bookingDateTime < now ? 'completed' : 'active';
            return {
                bookingId,
                day,
                time,
                email,
                fullName,
                mobileNumber: mobileNumber.toString().replace(/(\d{3})(\d{3})(\d{3})/, '$1 $2 $3'),
                status: status as 'completed' | 'active'
            };
        });
    };

    const handleDeleteBooking = async (bookingId: number, email: string) => {
        try {
            const response = await deleteBooking(bookingId, email, token, language);
            await fetchBookings();
            setAlertType('success');
            setAlertMessage(response.statusMsg);
            setAlertKey(prevKey => prevKey + 1);
            setTimeout(() => {
                setAlertType('');
                setAlertMessage('');
            }, 2000);
        } catch (error) {
            console.error(error);
        }
    };

    const handleAddBooking = () => {
        navigate('/add-booking');
    };

    const handleEditBooking = (bookingId: number, email: string, day: string, time: string, fullName: string, mobileNumber: string) => {
        navigate(`/add-booking?id=${bookingId}&email=${encodeURIComponent(email)}&date=${day}&time=${time}&fullName=${fullName}&mobileNumber=${mobileNumber}`);
    };

    const handleDateChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setFilterDate(event.target.value);
    };

    const handleNameChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setFilterName(event.target.value);
    };

    const toggleSortOrder = () => {
        setSortOrder(prevOrder => prevOrder === 'asc' ? 'desc' : 'asc');
    };

    const filteredBookings = bookingsData
        .filter(booking => {
            const now = new Date();
            now.setSeconds(0, 0);
            const bookingDateTime = new Date(booking.day.split('-').reverse().join('-') + 'T' + booking.time);

            const matchesDate = filterDate === 'completed'
                ? bookingDateTime < now
                : filterDate === '' ? bookingDateTime >= now : booking.day === filterDate;

            const matchesName = filterName === '' || booking.fullName.toLowerCase().includes(filterName.toLowerCase());

            return matchesDate && matchesName;
        })
        .sort((a, b) => {
            const dateA = new Date(a.day.split('-').reverse().join('-') + 'T' + a.time);
            const dateB = new Date(b.day.split('-').reverse().join('-') + 'T' + b.time);
            if (sortOrder === 'asc') {
                return dateA.getTime() - dateB.getTime();
            } else if (sortOrder === 'desc') {
                return dateB.getTime() - dateA.getTime();
            }
            return 0;
        });

    const getFilteredNames = () => {
        const now = new Date();
        now.setSeconds(0, 0);
        return [...new Set(bookingsData
            .filter(booking => {
                const bookingDateTime = new Date(booking.day.split('-').reverse().join('-') + 'T' + booking.time);
                if (filterDate === 'completed') {
                    return bookingDateTime < now;
                } else if (filterDate === '') {
                    return bookingDateTime >= now;
                } else {
                    return booking.day === filterDate;
                }
            })
            .map(booking => booking.fullName)
        )].sort();
    };

    const uniqueNames = getFilteredNames();

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-bold text-center mb-8 text-primary">{t.bookings.myBookings}</h1>
            <div className="mb-4">
                <div className="flex items-center space-x-2">
                    <div className="dropdown">
                        <div tabIndex={0} role="button" className="flex items-center space-x-2">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" fill="none"
                                 stroke="currentColor"
                                 strokeWidth="32" className="w-6 h-6">
                                <path
                                    d="M3.9 54.9C10.5 40.9 24.5 32 40 32H472c15.5 0 29.5 8.9 36.1 22.9s4.6 30.5-5.2 42.5L320 320.9V448c0 12.1-6.8 23.2-17.7 28.6s-23.8 4.3-33.5-3l-64-48c-8.1-6-12.8-15.5-12.8-25.6V320.9L9 97.3C-.7 85.4-2.8 68.8 3.9 54.9z"/>
                            </svg>
                            <span>{t.bookings.filters}</span>
                        </div>
                        <div className="dropdown-content z-10">
                            <select value={filterDate} onChange={handleDateChange}
                                    className="select select-bordered mt-2 w-40">
                                <option value="" disabled selected hidden>{t.bookings.selectDate}</option>
                                <option value="">{t.bookings.all}</option>
                                {uniqueDates.map(date => (
                                    <option key={date} value={date}>{date}</option>
                                ))}
                                <option value="completed">{t.bookings.completed}</option>
                            </select>
                            <select value={filterName} onChange={handleNameChange}
                                    className="select select-bordered mt-2 w-40">
                                <option value="" disabled selected hidden>{t.bookings.selectName}</option>
                                <option value="">{t.bookings.all}</option>
                                {uniqueNames.map(name => (
                                    <option key={name} value={name}>{name}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                    <button onClick={toggleSortOrder} className="flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512" fill="none"
                             stroke="currentColor"
                             strokeWidth="32" className="w-6 h-6 ml-2 mr-2">
                            <path d="M137.4 41.4c12.5-12.5 32.8-12.5 45.3 0l128 128c9.2 9.2 11.9 22.9 6.9 34.9s-16.6 19.8-29.6 19.8H32c-12.9 0-24.6-7.8-29.6-19.8s-2.2-25.7 6.9-34.9l128-128zm0 429.3l-128-128c-9.2-9.2-11.9-22.9-6.9-34.9s16.6-19.8 29.6-19.8H288c12.9 0 24.6 7.8 29.6 19.8s2.2 25.7-6.9 34.9l-128 128c-12.5 12.5-32.8 12.5-45.3 0z"/>
                        </svg>
                        <span className="mr-2">
                            {t.bookings.sort}: {sortOrder === 'asc' ? t.bookings.ascending : t.bookings.descending}
                        </span>
                    </button>
                    <Button className="btn-square btn-success btn-sm" onClick={handleAddBooking} text="">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 448 512" fill="currentColor" className="w-4 h-4">
                            <path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32V224H48c-17.7 0-32 14.3-32 32s14.3 32 32 32H192V432c0 17.7 14.3 32 32 32s32-14.3 32-32V288H400c-17.7 0 32-14.3 32-32s-14.3-32-32-32H256V80z"/>
                        </svg>
                    </Button>
                </div>
            </div>

            {!isLoggedIn && (
                <div className="text-center py-16">
                    <span>
                        <Link to="/sign-in" className="text-blue-500 hover:text-blue-700">{t.bookings.signIn}</Link>
                        {t.bookings.or}
                        <Link to="/sign-up" className="text-blue-500 hover:text-blue-700">{t.bookings.signUp}</Link>
                        {t.bookings.toSeeYourBookings}
                    </span>
                </div>
            )}

            {alertType && (
                <div className="fixed inset-0 flex items-center justify-center z-50 px-4">
                    <div className="max-w-md w-full">
                        <Alert key={alertKey} type={alertType} message={alertMessage}/>
                    </div>
                </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8">
                {filteredBookings.map((booking, index) => (
                    <BookingCard
                        key={index}
                        bookingId={booking.bookingId}
                        title={`${t.bookings.booking} ${index + 1}`}
                        day={booking.day}
                        time={booking.time}
                        email={booking.email}
                        fullName={booking.fullName}
                        mobileNumber={booking.mobileNumber}
                        onDelete={() => handleDeleteBooking(booking.bookingId, booking.email)}
                        onEdit={handleEditBooking}
                    />
                ))}
            </div>
        </div>
    );
};

export default Bookings;