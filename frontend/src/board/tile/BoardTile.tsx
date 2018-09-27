import * as React from "react";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";
import boardActions, {Coordinates} from "../boardActions";
import {Tile} from "../../domain/Tile";
import {hitIndicator, missIndicator} from "../../domain/tileIndicators";

export interface IBoardTilePropsFromActions {
    actions: {
        tileHit: any;
    };
}

export interface IBoardTilePropsFromParent {
    tile: Tile,
    coordinates: Coordinates
}

export type BoardTileProps = IBoardTilePropsFromParent & IBoardTilePropsFromActions;

export class BoardTile extends React.Component<BoardTileProps, {}> {

    public render() {
        const tile = this.props.tile;
        return <div className={this.className()}
                    onClick={() => {
                        if (tile.hit) {
                            return;
                        }
                        this.props.actions.tileHit(this.props.coordinates);
                    }}>
            {BoardTile.tileIndicator(tile)}
        </div>;
    }

    private className(): string {
        let className = "board__tile";

        if (this.props.tile.hit) {
            className += " clicked";
        }

        className += " " + this.classNameForCoordinate();
        return className
    }

    private classNameForCoordinate() {
        let {xCoordinate, yCoordinate} = this.props.coordinates;
        const rowEven = xCoordinate % 2 == 0;
        const columnEven = yCoordinate % 2 == 0;

        let bothEven = columnEven && rowEven;
        let bothOdd = !columnEven && !rowEven;

        if (bothEven || bothOdd) {
            return "rotated _155-reverse";
        } else {
            return "rotated _62";
        }
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
}

export const mapDispatchToProps = (dispatch: Dispatch<{}>): IBoardTilePropsFromActions => {
    return {
        actions: bindActionCreators({
            ...boardActions
        }, dispatch)
    }
};

export default connect(null, mapDispatchToProps)(BoardTile);

