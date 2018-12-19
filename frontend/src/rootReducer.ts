import {combineReducers} from "redux";
import targetBoardReducer, {BoardState} from "./board/targetBoardReducer";
import gameReducer, {GameState} from "./game/gameReducer";

export type rootState =  {
    boardReducer: BoardState;
    gameReducer: GameState;
}

export const rootReducer = combineReducers({
    boardReducer: targetBoardReducer,
    gameReducer
});
