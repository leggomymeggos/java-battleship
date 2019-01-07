import * as React from "react";
import TargetBoard from "../board/EnemyAgentBoard";
import AgentData from "../agent/AgentData";
import gameActions from "./gameActions";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import PlayerBoard from "../board/UserAgentBoard";

interface IGamePropsFromActions {
    actions: {
        createNewGame: any
    }
}

export type GameProps = IGamePropsFromActions

export class Game extends React.Component<GameProps> {
    public componentWillMount() {
        this.props.actions.createNewGame()
    }

    public render() {
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

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IGamePropsFromActions => {
    return {
        actions: bindActionCreators({
            ...gameActions
        }, dispatch)
    }
};

export default connect(null, mapDispatchToProps)(Game);