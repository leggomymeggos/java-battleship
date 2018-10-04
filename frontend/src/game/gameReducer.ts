import {Action, handleActions} from "redux-actions";
import {GameActions} from "./gameActions";

export type GameState = {
    winner: boolean;
}

export const initialState: GameState = {
    winner: false
};

const gameReducer = handleActions({
    [GameActions.GAME_WON]: (state: GameState, action: Action<any>) => {
        return {
            ...state,
            winner: action.payload
        }
    }
}, initialState);

export default gameReducer;
