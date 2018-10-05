import {Action, handleActions} from "redux-actions";
import {GameActions} from "./gameActions";
import {Player} from "../agent/Player";

export type GameState = {
    winner: Player;
    humanPlayer: Player;
    computerPlayer: Player;
}

export const initialState: GameState = {
    winner: null,
    humanPlayer: null,
    computerPlayer: null
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
