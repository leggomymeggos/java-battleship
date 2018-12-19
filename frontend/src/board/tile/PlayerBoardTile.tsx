import * as React from "react";
import {Tile} from "../../domain/Tile";
import {Coordinate} from "../boardActions";

interface IPlayerBoardTileProps {
    tile: Tile,
    coordinates: Coordinate
}

export type PlayerBoardTileProps = IPlayerBoardTileProps;

export class PlayerBoardTile extends React.Component<any> {
    public render() {
        return <div className={this.className()}/>;
    }

    private className(): string {
        let className = "board__tile";

        className += " " + this.classNameForCoordinate(this.props.tile);

        return className
    }

    private classNameForCoordinate(tile: Tile) {
        let {x, y} = this.props.coordinates;
        const rowEven = x % 2 == 0;
        const columnEven = y % 2 == 0;

        let bothEven = columnEven && rowEven;
        let bothOdd = !columnEven && !rowEven;

        if (tile.ship == null) {
            if (bothEven || bothOdd) {
                return "rotated _155-reverse";
            } else {
                return "rotated _62";
            }
        } else {
            if (tile.hit) {
                return "occupied--hit"
            } else {
                return "occupied"
            }
        }
    }
}