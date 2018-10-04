import {combineReducers} from "redux";
import boardReducer, {BoardState} from "./board/boardReducer";
import gameReducer, {GameState} from "./game/gameReducer";

export type rootState =  {
    boardReducer: BoardState;
    gameReducer: GameState;
}

export const rootReducer = combineReducers({
    boardReducer,
    gameReducer
});
