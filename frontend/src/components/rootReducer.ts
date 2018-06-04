import {combineReducers} from "redux";
import boardReducer from "./board/boardReducer";

export const rootReducer = combineReducers({
    boardReducer: boardReducer
});
