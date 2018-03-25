import * as React from "react";

export interface IHelloProps {
    compiler: string;
    framework: string;
}

export class Hello extends React.Component<IHelloProps, {}> {
    render() {
        return <h1 className="hello-text">Hello from {this.props.compiler} and {this.props.framework}!</h1>;
    }
}