import * as React from "react";
import {connect} from "react-redux";
import BoardTile, {AgentType} from "./tile/BoardTile";
import boardActions, {Coordinate} from "./boardActions";
import {bindActionCreators, Dispatch} from "redux";

export interface IBoardPropsFromStore {
    attackerId: number;
    gameId: number,
    grid: any[][];
    winnerId: number;
}

export interface IBoardPropsFromActions {
    actions: {
        boardHit: (gameId: number, attackerId: number, coordinate: Coordinate) => any;
    };
}

export type BoardProps = IBoardPropsFromStore & IBoardPropsFromActions;

const BoardLabel = (props: { value: string | number, className: 'row' | 'column' }) => {
    return <div className={`board__label ${props.className}`}>
        <div className="board__label--value">{props.value}</div>
    </div>
};

export class OpposingAgentBoard extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public render() {
        return <div className="target--board">
            <div className="board__label"/>
            {this.renderColumnLabels()}
            {this.renderRowLabels()}
            <div className="target--board__grid">
                {this.renderGrid()}
            </div>
        </div>
    }

    private renderColumnLabels() {
        return this.props.grid[0].map((_, index) => {
            return <BoardLabel value={this.alphabet[index].toUpperCase()}
                               className={"column"}
                               key={OpposingAgentBoard.getKey()}/>;
        });
    }

    private renderRowLabels() {
        return this.props.grid.map((value, rowIndex) => {
            return <BoardLabel value={rowIndex + 1}
                               className={"row"}
                               key={OpposingAgentBoard.getKey()}/>;
        });
    }

    private renderGrid() {
        const gameOver = this.props.winnerId !== null && this.props.winnerId !== -1;
        return this.props.grid.map((value, rowIndex) => {
            return value.map((tile, columnIndex) => {
                const coordinate = {column: columnIndex, row: rowIndex};
                return <BoardTile key={OpposingAgentBoard.getKey()}
                                  tile={tile}
                                  agentType={AgentType.OPPONENT}
                                  gameOver={gameOver}
                                  tileClicked={() => {
                                      if (!gameOver) {
                                          this.props.actions.boardHit(
                                              this.props.gameId,
                                              this.props.attackerId,
                                              coordinate
                                          );
                                      }
                                  }}
                                  coordinate={coordinate}
                />
            })
        });
    }

    private static getKey() {
        return Math.random() * Math.random()
    }
}

export const mapStateToProps = (state: any): IBoardPropsFromStore => {
    return {
        grid: state.opposingAgentReducer.grid,
        winnerId: state.gameReducer.winnerId,
        gameId: state.gameReducer.id,
        attackerId: state.userAgentReducer.id
    }
};
export const mapDispatchToProps = (dispatch: Dispatch<{type: any}>): IBoardPropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions,
        }, dispatch)
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(OpposingAgentBoard);
