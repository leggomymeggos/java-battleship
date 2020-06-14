import * as React from "react";
import {connect} from "react-redux";
import BoardTile, {AgentType} from "./tile/BoardTile";
import {BoardLabel} from "./BoardLabel";

export interface IBoardPropsFromStore {
    id: number,
    gameId: number;
    grid: any[][];
    sunkenShips: string[];
    winnerId: number;
}

export type BoardProps = IBoardPropsFromStore;

export class UserAgentBoard extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public render() {
        return <div className="user--board">
            <div className="board__label"/>
            {this.renderColumnLabels()}
            {this.renderRowLabels()}
            <div className="user--board__grid">
                {this.renderGrid()}
            </div>
        </div>
    }

    private renderColumnLabels() {
        return this.props.grid[0].map((_, index) => {
            return <BoardLabel value={this.alphabet[index].toUpperCase()}
                               className={"column"}
                               key={UserAgentBoard.getKey()}/>;
        });
    }

    private renderRowLabels() {
        return this.props.grid.map((value, rowIndex) => {
            return <BoardLabel value={rowIndex + 1}
                               className={"row"}
                               key={UserAgentBoard.getKey()}/>;
        });
    }

    private renderGrid() {
        return this.props.grid.map((value, rowIndex) => {
            return value.map((tile, columnIndex) => {
                return <BoardTile key={UserAgentBoard.getKey()}
                                  tile={tile}
                                  agentType={AgentType.USER}
                                  gameOver={this.props.winnerId !== null && this.props.winnerId !== -1}
                                  tileClicked={() => {
                                  }}
                                  coordinate={{column: columnIndex, row: rowIndex}}
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
        ...state.userAgentReducer,
        winnerId: state.gameReducer.winnerId,
        gameId: state.gameReducer.id,
    }
};

export default connect(mapStateToProps)(UserAgentBoard);
