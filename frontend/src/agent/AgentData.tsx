import * as React from "react";
import {connect} from "react-redux";
import {GameState} from "../game/gameReducer";

export interface IAgentDataPropsFromStore {
    winner: boolean;
}

export type AgentDataProps = IAgentDataPropsFromStore;

export class AgentData extends React.Component<AgentDataProps> {
    public render() {
        return <div className="agent__main-container">
            <div className="agent__enemy--dialogue">{
                this.props.winner ?
                    "Oh no! You defeated me!" :
                    "Hi! Ready to play?"
            }</div>
            <div className="agent__enemy--avatar">
                <img src={require("../images/img_transparent.png")}/>
            </div>
        </div>
    }
}

export const mapStateToProps = (state: { gameReducer: GameState }): IAgentDataPropsFromStore => {
    return {
        ...state.gameReducer,
    }
};

export default connect(mapStateToProps, {})(AgentData);