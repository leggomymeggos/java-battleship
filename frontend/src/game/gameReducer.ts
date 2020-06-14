import {Action, handleActions} from "redux-actions";
import {GameActions} from "./gameActions";
import {Agent} from "../domain/agent";

export enum GameStatus {
    NONE,
    IN_PROGRESS,
    GAME_OVER
}

export type GameState = {
    id: number;
    winnerId: number;
    status: GameStatus
}

export const initialState: GameState = {
    id: -1,
    winnerId: null,
    status: GameStatus.NONE
};

const gameReducer = handleActions({
    [GameActions.NEW_GAME]: (state: GameState) => {
        return {
            ...state,
            status: GameStatus.IN_PROGRESS
        }
    },
    [GameActions.NEW_GAME_CREATED]: (state: GameState, action: Action<any>) => {
        return {
            ...state,
            id: action.payload.id,
            winnerId: null,
        }
    },
    [GameActions.GAME_WON]: (state: GameState, action: Action<any>) => {
        return {
            ...state,
            winnerId: action.payload,
            status: GameStatus.GAME_OVER
        }
    }
}, initialState);

export default gameReducer;
