import {combineReducers} from "redux";
import gameReducer, {GameState} from "./game/gameReducer";
import enemyAgentReducer from "./agent/enemyAgentReducer";
import userAgentReducer from "./agent/userAgentReducer";
import {AgentState} from "./domain/agent";

export type rootState = {
    gameReducer: GameState;
    enemyAgentReducer: AgentState;
    userAgentReducer: AgentState;
}

export const rootReducer = combineReducers({
    gameReducer,
    enemyAgentReducer,
    userAgentReducer,
});
