import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import {Agent} from "../domain/agent";
import BoardTile, {AgentType} from "./tile/BoardTile";
import boardActions, {Coordinate} from "./boardActions";

export interface IBoardPropsFromStore {
    attackerId: number;
    gameId: number,
    grid: any[][];
    winner: Agent;
}

export interface IBoardPropsFromActions {
    actions: {
        boardHit: (gameId: number, attackerId: number, coordinate: Coordinate) => any;
    };
}

export type BoardProps = IBoardPropsFromStore & IBoardPropsFromActions;

export class OpposingAgentBoard extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public render() {
        return <div className="target--board">
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
                return <div className="board__label column" key={OpposingAgentBoard.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
            }</div>;
    }

    private renderRowLabels() {
        return <div key={OpposingAgentBoard.getKey()} className="board__labels row">{
            this.props.grid.map((value, rowIndex) => {
                return <div key={OpposingAgentBoard.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        const gameOver = this.props.winner != null;
        return <div className="target--board__grid">{
            this.props.grid.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    const coordinate = {x: columnIndex, y: rowIndex};
                    return <BoardTile key={OpposingAgentBoard.getKey()}
                                      tile={tile}
                                      tileClicked={() => {
                                          if (!gameOver) {
                                              this.props.actions.boardHit(
                                                  this.props.gameId,
                                                  this.props.attackerId,
                                                  coordinate
                                              );
                                          }
                                      }}
                                      agentType={AgentType.OPPONENT}
                                      gameOver={gameOver}
                                      coordinate={coordinate}
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
        grid: state.opposingAgentReducer.grid,
        winner: state.gameReducer.winner,
        gameId: state.gameReducer.id,
        attackerId: state.userAgentReducer.id
    }
};

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardPropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions,
        }, dispatch)
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(OpposingAgentBoard);
