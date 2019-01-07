import * as React from "react";
import {Tile} from "../../domain/tile";
import {targetHitIndicator, targetMissIndicator} from "./tileIndicators";
import {Coordinate} from "../boardActions";

interface IBoardTilePropsFromParent {
    tile: Tile,
    gameOver: boolean,
    coordinates: Coordinate,
    agentType: AgentType,
    tileClicked: (coordinate: Coordinate) => void,
}

export type BoardTileProps = IBoardTilePropsFromParent

export default class BoardTile extends React.Component<BoardTileProps> {
    public render() {
        const tile = this.props.tile;
        const coordinate = this.props.coordinates;

        return <div
            className={this.className()}
            onClick={() => {
                if (!tile.hit) {
                    this.props.tileClicked(coordinate)
                }
            }}>
            {BoardTile.tileIndicator(tile)}
        </div>
    }

    private className(): string {
        let className = "board__tile";
        className += " " + this.classNameForCoordinate();
        if (this.props.agentType != AgentType.SELF && (this.props.tile.hit || this.props.gameOver)) {
            className += " clicked";
        }
        return className
    }

    private classNameForCoordinate(): string {
        let tile = this.props.tile;

        if (this.props.agentType == AgentType.SELF && tile.ship != null) {
            return "occupied"
        } else {
            return this.addOcean()
        }
    }

    private addOcean() {
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

export enum AgentType {
    SELF, ENEMY, NONE
}