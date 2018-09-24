import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions from "./boardActions";

export interface IBoardPropsFromActions {
    actions: {
        getInitialBoard: any;
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
        return <table className="board__grid">
            <tbody>
            {this.renderColumnLabels()}
            {this.renderGrid()}
            </tbody>
        </table>
    }

    private renderColumnLabels() {
        return <tr>
            <th/>
            {this.props.coordinates[0].map((_, index) => {
                return <th className="board__label--column" key={Board.getKey()}>
                    {this.alphabet[index].toUpperCase()}
                </th>
            })}
        </tr>;
    }

    private renderGrid() {
        return this.props.coordinates.map((value, index) => {
            return <tr key={Board.getKey()}>
                <th className="board__label--row">{index + 1}</th>
                {value.map((x) => {
                    return <td key={Board.getKey()} className={Board.classNameForCoordinate(x, index)}/>
                })}
            </tr>
        });
    }

    private static classNameForCoordinate(columnNumber: number, rowNumber: number): string {
        let className = "board__tile";

        let bothEven = columnNumber % 2 == 0 && rowNumber % 2 == 0;
        let bothOdd = columnNumber % 2 != 0 && rowNumber % 2 != 0;

        if (bothEven || bothOdd) {
            className += " rotated"
        }

        return className;
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
