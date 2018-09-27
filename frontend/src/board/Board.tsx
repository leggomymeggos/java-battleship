import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions from "./boardActions";
import BoardTile from "./tile/BoardTile";

export interface IBoardPropsFromActions {
    actions: {
        getInitialBoard: any;
        tileHit: any;
    };
}

export interface IBoardPropsFromStore {
    coordinates: any[][];
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
            {this.props.coordinates[0].map((_, index) => {
                return <div className="board__label column" key={Board.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
            }</div>;
    }

    private renderRowLabels() {
        return <div key={Board.getKey()} className="board__labels row">{
            this.props.coordinates.map((value, rowIndex) => {
                return <div key={Board.getKey()} className="board__label row">{rowIndex + 1}</div>
            })
        }</div>;
    }

    private renderGrid() {
        return <div className={"board__grid"}>{
            this.props.coordinates.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    return <BoardTile key={Board.getKey()}
                                      tile={tile}
                                      coordinates={{xCoordinate: columnIndex, yCoordinate: rowIndex}}
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

export const mapStateToProps = (state: { boardReducer: { coordinates: any[][] } }): IBoardPropsFromStore => {
    return {
        coordinates: state.boardReducer.coordinates
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Board);
