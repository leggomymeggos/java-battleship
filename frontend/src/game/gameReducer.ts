import {Action, handleActions} from "redux-actions";
import {GameActions} from "./gameActions";
import {Agent} from "../agent/Agent";

export type GameState = {
    winner: Agent;
    humanPlayer: Agent;
    computerPlayer: Agent;
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
