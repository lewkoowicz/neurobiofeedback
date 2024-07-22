import React from "react";

interface FormSelectProps {
    label: string;
    options: string[];
    placeholder: string;
    value: string;
    onChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
    className?: string;
    bookedTimeslots?: string[];
}

const FormSelect = ({ label, options, placeholder, value, onChange, className, bookedTimeslots }: FormSelectProps) => (
    <div className={`form-control w-full ${className}`}>
        <label className="label">
            <span className="label-text">{label}</span>
        </label>
        <select className="select select-bordered w-full" value={value} onChange={onChange}>
            {value === '' && <option value="" disabled>{placeholder}</option>}
            {options.map((option) => (
                <option key={option}
                        value={option}
                        disabled={bookedTimeslots?.includes(option)}
                        className={bookedTimeslots?.includes(option) ? 'text-gray-700' : ''}
                >{option}</option>
            ))}
        </select>
    </div>
);

export default FormSelect;
