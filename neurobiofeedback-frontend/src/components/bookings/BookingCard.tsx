import {Button} from "../index.ts";
import {useLanguage} from "../../context";
import {translations} from "../../translations/translations.ts";

interface BookingCardProps {
    bookingId: number;
    title: string;
    day: string;
    time: string;
    email: string;
    fullName: string;
    mobileNumber: string;
    onDelete: (bookingId: number) => void;
    onEdit: (bookingId: number, email: string, day: string, time: string, fullName: string, mobileNumber: string) => void;
}

const BookingCard = ({ bookingId, title, day, time, email, fullName, mobileNumber, onDelete, onEdit }: BookingCardProps) => {
    const {language} = useLanguage();
    const t = translations[language];

    return (
    <div className="card bordered bg-base-100 shadow-xl p-6">
        <div className="card-body">
            <h2 className="card-title text-2xl font-bold mb-4">{title}</h2>
            <p><strong>{t.bookingCard.day}</strong> {day}</p>
            <p><strong>{t.bookingCard.hour}</strong> {time}</p>
            <p><strong>{t.bookingCard.email}</strong> {email}</p>
            <p><strong>{t.bookingCard.fullName}</strong> {fullName}</p>
            <p><strong>{t.bookingCard.mobileNumber}</strong> {mobileNumber}</p>
            <div className="card-actions justify-center mt-6">
                <Button className={"btn-primary btn-md mr-2"}
                        onClick={() => onEdit(bookingId, email, day, time, fullName, mobileNumber)} text={t.bookingCard.edit}/>
                <Button className={"btn-error btn-md"} onClick={() => onDelete(bookingId)} text={t.bookingCard.cancel}/>
            </div>
        </div>
    </div>
)};

export default BookingCard;
