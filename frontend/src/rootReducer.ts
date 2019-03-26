import {combineReducers} from "redux";
import gameReducer, {GameState} from "./game/gameReducer";
import opposingAgentReducer from "./agent/opposingAgentReducer";
import userAgentReducer from "./agent/userAgentReducer";
import {AgentState} from "./domain/agent";

export type rootState = {
    gameReducer: GameState;
    opposingAgentReducer: AgentState;
    userAgentReducer: AgentState;
}

export const rootReducer = combineReducers({
    gameReducer,
    opposingAgentReducer,
    userAgentReducer,
});
