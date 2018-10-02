import {Action, handleActions} from "redux-actions";
import {BoardActions} from "./boardActions";
import {Tile} from "../domain/Tile";
import * as _ from "lodash";
import {GameActions} from "../game/gameActions";

export type BoardState = {
    grid: Tile[][];
}

export const initialState: BoardState = {
    grid: [[]],
};

const boardReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: BoardState, action: Action<any>) => {
        return {
            ...state, grid: action.payload.grid
        }
    },
    [BoardActions.BOARD_HIT_SUCCESS]: (state: BoardState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload
        };
    },
}, initialState);

export default boardReducer;
