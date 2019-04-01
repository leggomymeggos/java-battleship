import * as React from "react";
import {Redirect} from "react-router";
import TargetBoard from "../board/OpposingAgentBoard";
import AgentData from "../agent/OpposingAgentData";
import {connect} from "react-redux";
import PlayerBoard from "../board/UserAgentBoard";
import {GameStatus} from "./gameReducer";

interface IGamePropsFromState {
    status: GameStatus
}

export type GameProps = IGamePropsFromState

export class Game extends React.Component<GameProps> {
    public render() {
        if (this.props.status === GameStatus.NONE) {
            return <Redirect to={"/"}/>
        }

        return <div className="game__content">
            <TargetBoard/>
            <div className="game__content__metadata">
                <AgentData/>
            </div>
            <div className="game__content--player" style={{borderLeft: "1px solid #aaa"}}>
                <p>Your ships</p>
                <PlayerBoard/>
            </div>
        </div>
    }
}

export const mapStateToProps = (state: { gameReducer: { status: GameStatus } }): IGamePropsFromState => {
    return {
        status: state.gameReducer.status
    }
};

export default connect(mapStateToProps, null)(Game);