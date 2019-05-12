import {createAction} from "redux-actions";
import {Tile} from "../domain/tile";

export const BoardActions = {
    OPPONENT_BOARD_HIT_SUCCESS: "OPPONENT_BOARD_HIT_SUCCESS",
    USER_BOARD_HIT_SUCCESS: "USER_BOARD_HIT_SUCCESS",
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

export const boardHit = createAction(BoardActions.BOARD_HIT, (
    gameId: number,
    attackerId: number,
    coordinates: Coordinate
) => {
    return {gameId, attackerId, coordinates}
});

export const opponentBoardHitSuccess =
    createAction(BoardActions.OPPONENT_BOARD_HIT_SUCCESS, (gameId: number, board: { grid: Tile[][], sunkenShips: string[] }) => {
        return {gameId, board}
    });

export const userBoardHitSuccess = createAction(BoardActions.USER_BOARD_HIT_SUCCESS, (gameId: number, board: { grid: Tile[][], sunkenShips: string[]; }) => {
    return {gameId, board}
});

export default {
    opponentBoardHitSuccess,
    userBoardHitSuccess,
    boardHit
}