import {Action, handleActions} from "redux-actions";
import {GameActions} from "./gameActions";
import {Agent} from "../domain/agent";

export type GameState = {
    id: number;
    winner: Agent;
}

export const initialState: GameState = {
    id: -1,
    winner: null,
};

const gameReducer = handleActions({
    [GameActions.GAME_WON]: (state: GameState, action: Action<any>) => {
        return {
            ...state,
            winner: action.payload
        }
    },
    [GameActions.NEW_GAME_CREATED]: (state: GameState, action: Action<any>) => {
        return {
            ...state,
            id: action.payload.id,
            winner: null
        }
    }
}, initialState);

export default gameReducer;
