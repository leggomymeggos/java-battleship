import * as React from "react";
import {Tile} from "../../domain/tile";
import {targetHitIndicator, targetMissIndicator} from "./tileIndicators";
import {Coordinate} from "../boardActions";

interface IBoardTilePropsFromParent {
    tile: Tile,
    gameOver: boolean,
    coordinate: Coordinate,
    agentType: AgentType,
    tileClicked: () => void,
}

export type BoardTileProps = IBoardTilePropsFromParent

export default class BoardTile extends React.Component<BoardTileProps> {
    public render() {
        const tile = this.props.tile;

        return <div
            className={this.className()}
            onClick={() => {
                if (!tile.hit) {
                    this.props.tileClicked()
                }
            }}>
            {BoardTile.tileIndicator(tile)}
        </div>
    }

    private className(): string {
        let className = "board__tile";
        className += " " + this.classNameForCoordinate();
        if (this.props.agentType != AgentType.USER && (this.props.tile.hit || this.props.gameOver)) {
            className += " clicked";
        }
        return className
    }

    private classNameForCoordinate(): string {
        let tile = this.props.tile;

        if (this.props.agentType == AgentType.USER && tile.ship != null) {
            return "ship__" + tile.ship.toLowerCase();
        } else return ""
    }

    private static tileIndicator(tile: Tile) {
        if (tile.hit && tile.ship !== null) {
            return targetHitIndicator();
        } else if (tile.hit) {
            return targetMissIndicator();
        } else {
            return null;
        }
    }
}

export enum AgentType {
    USER, OPPONENT, NONE
}