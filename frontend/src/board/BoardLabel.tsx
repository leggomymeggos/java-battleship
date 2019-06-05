import * as React from "react";

export const BoardLabel = (props: { value: string | number, className: 'row' | 'column' }) => {
    return <div className={`board__label ${props.className}`}>
        <div className="board__label--value">{props.value}</div>
    </div>
};
