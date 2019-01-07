import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import {Agent} from "../domain/agent";
import BoardTile, {AgentType} from "./tile/BoardTile";
import boardActions from "./boardActions";

export interface IBoardPropsFromStore {
    id: number
    gameId: number,
    grid: any[][];
    sunkenShips: string[];
    winner: Agent;
}

export interface IBoardPropsFromActions {
    actions: {
        boardHit: any;
    };
}

export type BoardProps = IBoardPropsFromStore & IBoardPropsFromActions;

export class EnemyAgentBoard extends React.Component<BoardProps> {
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
                return <div className="board__label column" key={EnemyAgentBoard.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
            }</div>;
    }

    private renderRowLabels() {
        return <div key={EnemyAgentBoard.getKey()} className="board__labels row">{
            this.props.grid.map((value, rowIndex) => {
                return <div key={EnemyAgentBoard.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        return <div className="target--board__grid">{
            this.props.grid.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    const coordinate = {x: columnIndex, y: rowIndex};
                    return <BoardTile key={EnemyAgentBoard.getKey()}
                                      tile={tile}
                                      tileClicked={() => {
                                          this.props.actions.boardHit(this.props.gameId, coordinate);
                                      }}
                                      agentType={AgentType.ENEMY}
                                      gameOver={this.props.winner != null}
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
        ...state.enemyAgentReducer,
        winner: state.gameReducer.winner,
        gameId: state.gameReducer.id,
    }
};

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardPropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions,
        }, dispatch)
    }
};


export default connect(mapStateToProps, mapDispatchToProps)(EnemyAgentBoard);
