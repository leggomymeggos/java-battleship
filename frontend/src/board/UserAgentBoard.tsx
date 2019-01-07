import * as React from "react";
import {connect} from "react-redux";
import {Agent} from "../domain/agent";
import BoardTile, {AgentType} from "./tile/BoardTile";

export interface IBoardPropsFromStore {
    id: number,
    gameId: number;
    grid: any[][];
    sunkenShips: string[];
    winner: Agent;
}

export type BoardProps = IBoardPropsFromStore;

export class UserAgentBoard extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public render() {
        return <div className="user--board">
            {this.renderColumnLabels()}
            <div className="board__grid-and-row-labels-container">
                {this.renderRowLabels()}
                {this.renderGrid()}
            </div>
        </div>
    }

    private renderColumnLabels() {
        return <div className="board__labels column">
            <div className="board__label column"/>
            {this.props.grid[0].map((_, index) => {
                return <div className="board__label column" key={UserAgentBoard.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })}</div>;
    }

    private renderRowLabels() {
        return <div key={UserAgentBoard.getKey()} className="board__labels row">{
            this.props.grid.map((value, rowIndex) => {
                return <div key={UserAgentBoard.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        return <div className="user--board__grid">{
            this.props.grid.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    return <BoardTile key={UserAgentBoard.getKey()}
                                      tile={tile}
                                      agentType={AgentType.USER}
                                      gameOver={this.props.winner != null}
                                      tileClicked={() => {
                                      }}
                                      coordinate={{x: columnIndex, y: rowIndex}}
                    />
                })
            })
        }</div>;
    }

    private static getKey() {
        return Math.random() * Math.random()
    }
}

export const mapStateToProps = (state: any): IBoardPropsFromStore => {
    return {
        ...state.userAgentReducer,
        winner: state.gameReducer.winner,
        gameId: state.gameReducer.id,
    }
};

export default connect(mapStateToProps)(UserAgentBoard);
