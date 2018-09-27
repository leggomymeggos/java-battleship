import {createAction} from "redux-actions";

export const BoardActions = {
    GET_INITIAL_BOARD: "GET_INITIAL_BOARD",
    TILE_HIT: "TILE_HIT"
};

export class Coordinates {
    xCoordinate: number;
    yCoordinate: number;
}

export const getInitialBoard =
    createAction(BoardActions.GET_INITIAL_BOARD);

export const tileHit = createAction(BoardActions.TILE_HIT, (coordinates: Coordinates) => coordinates);

export default {
    getInitialBoard,
    tileHit
}