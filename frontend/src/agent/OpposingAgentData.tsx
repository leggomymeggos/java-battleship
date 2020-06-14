import * as React from "react";
import {connect} from "react-redux";
import {GameState} from "../game/gameReducer";
import {Agent, AgentState} from "../domain/agent";

export interface IAgentDataPropsFromStore {
    winnerId: number;
    opposingAgentId: number;
    attackResult: any;
}

export type AgentDataProps = IAgentDataPropsFromStore;

export class OpposingAgentData extends React.Component<AgentDataProps> {

    public render() {
        const gameOver = this.props.winnerId!! && this.props.winnerId !== -1;
        const opposingAgentWon = gameOver && this.props.winnerId == this.props.opposingAgentId;

        return <div className="agent__main-container">
            <div className="agent__opponent--dialogue">{this.opposingAgentDialogue()}</div>
            <div
                className={gameOver && !opposingAgentWon ? "agent__opponent--avatar defeated" : "agent__opponent--avatar idle"}>
                <img src={require("../images/img_transparent.png")} alt="opponent avatar"/>
            </div>
        </div>
    }

    private opposingAgentDialogue(): string {
        if (!this.props.winnerId || this.props.winnerId === -1) {
            switch (this.props.attackResult.hitType) {
                case 'HIT':
                    return "Yes! A hit!";
                case 'SUNK':
                    let shipName = this.props.attackResult.ship.toLowerCase();
                    return `Hahahaha! I sunk your ${OpposingAgentData.shipDisplayName(shipName)}!`;
                case 'MISS':
                    return "...";
                default:
                    return "Hi! Ready to play?";
            }
        }

        if (this.props.winnerId == this.props.opposingAgentId) {
            return "Hahahaha!!! I have defeated you!!!";
        } else {
            return "Oh no! You defeated me!";
        }
    }

    private static shipDisplayName(shipName: string): string {
        return shipName.split("_").map((it) =>
            `${it[0].toUpperCase()}${it.slice(1, it.length)}`
        ).join(" ");
    }
}

export const mapStateToProps = (state: {
    gameReducer: GameState,
    opposingAgentReducer: AgentState
}): IAgentDataPropsFromStore => {
    return {
        winnerId: state.gameReducer.winnerId,
        opposingAgentId: state.opposingAgentReducer.id,
        attackResult: state.opposingAgentReducer.recentAttackResult,
    }
};

export default connect(mapStateToProps, {})(OpposingAgentData);