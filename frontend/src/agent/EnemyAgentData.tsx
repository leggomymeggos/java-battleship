import * as React from "react";
import {connect} from "react-redux";
import {GameState} from "../game/gameReducer";
import {Agent, AgentState} from "../domain/agent";

export interface IAgentDataPropsFromStore {
    winner: Agent;
    enemyAgentId: number;
}

export type AgentDataProps = IAgentDataPropsFromStore;

export class EnemyAgentData extends React.Component<AgentDataProps> {

    public render() {
        const gameOver = this.props.winner!!;
        const enemyAgentWon = gameOver && this.props.winner.id == this.props.enemyAgentId;

        return <div className="agent__main-container">
            <div className="agent__enemy--dialogue">{this.enemyAgentDialogue()}</div>
            <div className={gameOver && !enemyAgentWon ? "agent__enemy--avatar defeated" : "agent__enemy--avatar idle"}>
                <img src={require("../images/img_transparent.png")} alt="opponent avatar"/>
            </div>
        </div>
    }

    private enemyAgentDialogue(): string {
        if (!this.props.winner) {
            return "Hi! Ready to play?";
        }

        if (this.props.winner.id == this.props.enemyAgentId) {
            return "Hahahaha!!! I have defeated you!!!";
        } else {
            return "Oh no! You defeated me!";
        }
    }
}

export const mapStateToProps = (state: {
    gameReducer: GameState,
    enemyAgentReducer: AgentState
}): IAgentDataPropsFromStore => {
    return {
        winner: state.gameReducer.winner,
        enemyAgentId: state.enemyAgentReducer.id
    }
};

export default connect(mapStateToProps, {})(EnemyAgentData);