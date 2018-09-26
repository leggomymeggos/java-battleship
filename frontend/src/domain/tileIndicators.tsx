import * as React from "react";

export const hitIndicator = () => {
    return <svg className="aimed--hit--image" viewBox="0 0 100 100" version="1.1" stroke="black" strokeWidth="20"
                xmlns="http://www.w3.org/2000/svg">
        <path d="M15,85 L85,15"/>
        <path d="M85,85 L15,15"/>
    </svg>
};

export const missIndicator = () => {
    return <svg className="aimed--miss--image" viewBox="0 0 100 100" version="1.1" stroke="black" strokeWidth="20" strokeOpacity="0.5"
                xmlns="http://www.w3.org/2000/svg">
        <path d="M10,50 L90,50"/>
    </svg>
};