import {Action, handleActions} from "redux-actions";
import {BoardActions} from "./boardActions";
import {Tile} from "../domain/Tile";
import {GameActions} from "../game/gameActions";

export type BoardState = {
    grid: Tile[][];
    sunkenShips: string[];
}

export const initialState: BoardState = {
    grid: [[]],
    sunkenShips: []
};

const targetBoardReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: BoardState, action: Action<any>) => {
        return {
            ...state, grid: action.payload.grid
        }
    },
    [BoardActions.BOARD_HIT_SUCCESS]: (state: BoardState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload.grid,
            sunkenShips: action.payload.sunkenShips,
        };
    },
}, initialState);

export default targetBoardReducer;
