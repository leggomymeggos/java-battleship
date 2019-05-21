import * as React from "react";
import {connect} from "react-redux";
import {GameState} from "../game/gameReducer";
import {Agent, AgentState} from "../domain/agent";

export interface IAgentDataPropsFromStore {
    winner: Agent;
    opposingAgentId: number;
    attackResult: any;
}

export type AgentDataProps = IAgentDataPropsFromStore;

export class OpposingAgentData extends React.Component<AgentDataProps> {

    public render() {
        const gameOver = this.props.winner!!;
        const opposingAgentWon = gameOver && this.props.winner.id == this.props.opposingAgentId;

        return <div className="agent__main-container">
            <div className="agent__opponent--dialogue">{this.opposingAgentDialogue()}</div>
            <div
                className={gameOver && !opposingAgentWon ? "agent__opponent--avatar defeated" : "agent__opponent--avatar idle"}>
                <img src={require("../images/img_transparent.png")} alt="opponent avatar"/>
            </div>
        </div>
    }

    private opposingAgentDialogue(): string {
        if (!this.props.winner) {
            switch (this.props.attackResult.hitType) {
                case 'HIT':
                    return "Yes! A hit!";
                case 'SUNK':
                    let shipName = this.props.attackResult.shipName.toLowerCase();
                    return `Hahahaha! I sunk your ${shipName[0].toUpperCase()}${shipName.slice(1, shipName.length)}!`;
                case 'MISS':
                    return "...";
                default:
                    return "Hi! Ready to play?";
            }
        }

        if (this.props.winner.id == this.props.opposingAgentId) {
            return "Hahahaha!!! I have defeated you!!!";
        } else {
            return "Oh no! You defeated me!";
        }
    }
}

export const mapStateToProps = (state: {
    gameReducer: GameState,
    opposingAgentReducer: AgentState
}): IAgentDataPropsFromStore => {
    return {
        winner: state.gameReducer.winner,
        opposingAgentId: state.opposingAgentReducer.id,
        attackResult: state.opposingAgentReducer.recentAttackResult,
    }
};

export default connect(mapStateToProps, {})(OpposingAgentData);