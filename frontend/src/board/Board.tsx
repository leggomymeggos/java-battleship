import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions from "./boardActions";
import BoardTile from "./tile/BoardTile";
import {rootState} from "../rootReducer";
import {Player} from "../agent/Player";

export interface IBoardPropsFromActions {
    actions: {
        getInitialBoard: any;
    };
}

export interface IBoardPropsFromStore {
    grid: any[][];
    sunkenShips: string[];
    winner: Player;
}

export type BoardProps = IBoardPropsFromActions & IBoardPropsFromStore;

export class Board extends React.Component<BoardProps> {
    private alphabet = [...Array(26)]
        .map((_, index) => String.fromCharCode(index + 97));

    public componentWillMount() {
        this.props.actions.getInitialBoard();
    }

    public render() {
        return <div className="board">
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
                return <div className="board__label column" key={Board.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
            }</div>;
    }

    private renderRowLabels() {
        return <div key={Board.getKey()} className="board__labels row">{
            this.props.grid.map((value, rowIndex) => {
                return <div key={Board.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        return <div className={"board__grid"}>{
            this.props.grid.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    return <BoardTile key={Board.getKey()}
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

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardPropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions
        }, dispatch)
    }
};

export const mapStateToProps = (state: rootState): IBoardPropsFromStore => {
    return {
        ...state.boardReducer,
        winner: state.gameReducer.winner,
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Board);
