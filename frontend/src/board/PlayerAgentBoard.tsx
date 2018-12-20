import * as React from "react";
import {connect} from "react-redux";
import {PlayerAgentBoardTile} from "./tile/PlayerAgentBoardTile";
import {rootState} from "../rootReducer";
import {Agent} from "../agent/Agent";

export interface IBoardPropsFromStore {
    id: number,
    grid: any[][];
    sunkenShips: string[];
    winner: Agent;
}

export type BoardProps = IBoardPropsFromStore;

export class PlayerAgentBoard extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public render() {
        return <div className="player--board">
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
                return <div className="board__label column" key={PlayerAgentBoard.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
            }</div>;
    }

    private renderRowLabels() {
        return <div key={PlayerAgentBoard.getKey()} className="board__labels row">{
            this.props.grid.map((value, rowIndex) => {
                return <div key={PlayerAgentBoard.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        return <div className="player--board__grid">{
            this.props.grid.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    return <PlayerAgentBoardTile key={PlayerAgentBoard.getKey()}
                                                 tile={tile}
                                                 winner={this.props.winner != null}
                                                 coordinates={{x: columnIndex, y: rowIndex}}
                    />
                })
            })
        }</div>;
    }

    private static getKey() {
        return Math.random() * Math.random()
    }
}

export const mapStateToProps = (state: rootState): IBoardPropsFromStore => {
    return {
        ...state.playerAgentReducer,
        winner: state.gameReducer.winner,
    }
};

export default connect(mapStateToProps)(PlayerAgentBoard);
