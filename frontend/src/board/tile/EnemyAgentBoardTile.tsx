import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions, {Coordinate} from "../boardActions";
import {Tile} from "../../domain/Tile";
import {targetHitIndicator, targetMissIndicator} from "./tileIndicators";

export interface IBoardTilePropsFromActions {
    actions: {
        boardHit: any;
    };
}

export interface IBoardTilePropsFromParent {
    tile: Tile,
    winner: boolean,
    coordinates: Coordinate
}

export type BoardTileProps = IBoardTilePropsFromParent & IBoardTilePropsFromActions;

export class EnemyAgentBoardTile extends React.Component<BoardTileProps> {
    public render() {
        const tile = this.props.tile;
        return <div className={this.className()}
                    onClick={() => {
                        if (tile.hit || this.props.winner) {
                            return;
                        }
                        this.props.actions.boardHit(this.props.coordinates);
                    }}>
            {EnemyAgentBoardTile.tileIndicator(tile)}
        </div>;
    }

    private className(): string {
        let className = "board__tile";

        if (this.props.tile.hit || this.props.winner) {
            className += " clicked";
        }

        className += " " + this.classNameForCoordinate();
        return className
    }

    private classNameForCoordinate() {
        let {x, y} = this.props.coordinates;
        const rowEven = x % 2 == 0;
        const columnEven = y % 2 == 0;

        let bothEven = columnEven && rowEven;
        let bothOdd = !columnEven && !rowEven;

        if (bothEven || bothOdd) {
            return "rotated _155-reverse";
        } else {
            return "rotated _62";
        }
    }

    private static tileIndicator(tile: Tile) {
        if (tile.hit && tile.ship !== null) {
            return <span className="aimed--hit">
                {targetHitIndicator()}
                </span>
        } else if (tile.hit) {
            return <span className="aimed--miss">
                {targetMissIndicator()}
                </span>
        } else {
            return null;
        }
    }
}

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardTilePropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions,
        }, dispatch)
    }
};

export default connect(null, mapDispatchToProps)(EnemyAgentBoardTile);

