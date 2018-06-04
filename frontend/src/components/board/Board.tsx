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
        .map((q, w) => String.fromCharCode(w + 97));

    public componentWillMount() {
        this.props.actions.getInitialBoard();
    }

    public render() {
        return <div>
            {this.renderRowLabels()}
            {this.renderColumnLabels()}
            {this.renderGrid()}
        </div>
    }

    private renderRowLabels() {
        return <div className="board__labels--row">{
            this.props.coordinates
                .map((it, index) => {
                    return <div className="board__label" key={index}>
                        {index + 1}
                    </div>;
                })
        }</div>;
    }

    private renderColumnLabels() {
        return <div className="board__labels--column">
            {this.props.coordinates[0].map((_, index) => {
                return <div key={index}>
                    {this.alphabet[index].toUpperCase()}
                </div>
            })}
        </div>;
    }

    private renderGrid() {
        return <div className="board__grid">
            {this.props.coordinates
                .reduce((a, b) => a.concat(b), [])
                .map((_, index) => {
                    return <div className="board__tile" key={index}/>
                })}
        </div>
    }
}

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardPropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions
        }, dispatch)
    }
};

export const mapStateToProps = (state: {boardState: {coordinates: any[][]}}): IBoardPropsFromStore => {
    return {
        coordinates: state.boardState.coordinates
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Board);
