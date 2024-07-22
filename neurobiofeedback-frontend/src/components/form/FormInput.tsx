import React from "react";

interface FormInputProps {
    label: string;
    type: string;
    placeholder: string;
    value: string;
    min?: string;
    onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
    className?: string;
    disabled?: boolean;
}

const FormInput = ({ label, type, placeholder, value, min, onChange, className, disabled }: FormInputProps) => (
    <div className={`form-control w-full ${className}`}>
        <label className="label">
            <span className="label-text">{label}</span>
        </label>
        <input type={type} placeholder={placeholder} value={value} min={min} onChange={onChange} className="input input-bordered w-full" disabled={disabled} />
    </div>
);

export default FormInput;
