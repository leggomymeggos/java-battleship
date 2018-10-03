import * as React from "react";
import Board from "./board/Board";
import {AgentData} from "./agent/AgentData";

export default class Layout extends React.Component {
    public render() {
        return <div className="game__content">
            <Board/>
            <div className="game__content__metadata">
                <AgentData />
            </div>
        </div>
    }
}