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

        return <div className="game__container">
            <div className="game__content">
                <div className="boards">
                    <TargetBoard/>
                    <div className="divider"/>
                    <div className="game__content--player">
                        <PlayerBoard/>
                    </div>
                </div>
                <div className="game__content__metadata">
                    <AgentData/>
                </div>
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