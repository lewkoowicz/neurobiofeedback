import React, {useEffect, useState} from "react";

interface AlertProps {
    type: 'success' | 'error' | 'update';
    message?: string;
}

const Alert: React.FC<AlertProps> = ({ type, message }) => {
    const [show, setShow] = useState(true);

    useEffect(() => {
        setShow(true);
        const timer = setTimeout(() => {
            setShow(false);
        }, 2000);

        return () => clearTimeout(timer);
    }, [type, message]);

    if (!show) return null;

    return (
        <>
            {type === 'success' && (
                <div role="alert" className="alert alert-success">
                    <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none"
                         viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    <span>{message || 'Twoja rezerwacja została złożona! Wkrótce otrzymasz mail z potwierdzeniem.'}</span>
                </div>
            )}
            {type === 'error' && (
                <div role="alert" className="alert alert-error">
                    <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none"
                         viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                              d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    <span>{message || 'Error! Task failed successfully.'}</span>
                </div>
            )}
            {type === 'update' && (
                <div role="alert" className="alert alert-success">
                    <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none"
                         viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                              d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    <span>{message || 'Twoja rezerwacja została zaktualizowana! Wkrótce otrzymasz mail z potwierdzeniem.'}</span>
                </div>
            )}
        </>
    );
};

export default Alert;
