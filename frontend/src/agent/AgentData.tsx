import * as React from "react";

export class AgentData extends React.Component {
    public render() {
        return <div className="agent__main-container">
            <div className={"agent__enemy--dialogue"}>Hi! Ready to play?</div>
            <div className="agent__enemy--avatar">
                <img src={require("../images/img_transparent.png")}/>
            </div>
        </div>
    }
}