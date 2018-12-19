import {createAction} from "redux-actions";
import {Tile} from "../domain/Tile";

export const BoardActions = {
    BOARD_HIT_SUCCESS: "BOARD_HIT_SUCCESS",
    BOARD_HIT: "BOARD_HIT"
};

export class Coordinate {
    x: number;
    y: number;

    constructor(x: number, y: number) {
        this.x = x;
        this.y = y;
    }
}

export const boardHit = createAction(BoardActions.BOARD_HIT, (coordinates: Coordinate) => coordinates);

export const boardHitSuccess =
    createAction(BoardActions.BOARD_HIT_SUCCESS, (board: { grid: Tile[][], sunkenShips: string[] }) => board);

export default {
    boardHitSuccess,
    boardHit
}