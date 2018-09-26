import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions from "./boardActions";
import {Tile} from "../domain/Tile";
import {hitIndicator, missIndicator} from "../domain/tileIndicators";

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
        return <div>
            {this.renderColumnLabels()}
            {this.renderRowLabels()}
            {this.renderGrid()}
        </div>

    }

    private renderColumnLabels() {
        return <div>{
            this.props.coordinates[0].map((_, index) => {
                return <div className="board__label--column" key={Board.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })
        }</div>;
    }

    private renderRowLabels() {
        return this.props.coordinates.map((value, rowIndex) => {
            return <div key={Board.getKey()}>
                <div className="board__label--row">{rowIndex + 1}</div>
            </div>
        });
    }

    private renderGrid() {
        return <div className={"board__grid"}>{
            this.props.coordinates.map((value, rowIndex) => {
                return value.map((tile, columnIndex) => {
                    return <div key={Board.getKey()}
                                className={Board.classNameForCoordinate(columnIndex, rowIndex)}
                                onClick={() => {
                                    this.props.actions.tileHit(columnIndex, rowIndex)
                                }}>
                        {Board.tileIndicator(tile)}
                    </div>
                })
            })
        }</div>;
    }

    private static classNameForCoordinate(columnNumber: number, rowNumber: number): string {
        let className = "board__tile";

        let bothEven = columnNumber % 2 == 0 && rowNumber % 2 == 0;
        let bothOdd = columnNumber % 2 != 0 && rowNumber % 2 != 0;

        if (bothEven || bothOdd) {
            className += " rotated";
        }

        return className;
    }

    private static tileIndicator(tile: Tile) {
        if (tile.hit && tile.shipId !== null) {
            return <span className="aimed--hit">
                {hitIndicator()}
            </span>
        } else if (tile.hit) {
            return <span className="aimed--miss">
                {missIndicator()}
            </span>
        } else {
            return null;
        }
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
