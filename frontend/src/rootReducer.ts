import {combineReducers} from "redux";
import boardReducer from "./board/boardReducer";
import gameReducer from "./game/gameReducer";

export const rootReducer = combineReducers({
    boardReducer,
    gameReducer
});
