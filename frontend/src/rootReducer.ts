import {combineReducers} from "redux";
import gameReducer, {GameState} from "./game/gameReducer";
import enemyAgentReducer from "./agent/enemyAgentReducer";
import playerAgentReducer from "./agent/playerAgentReducer";
import {AgentState} from "./domain/agent";

export type rootState = {
    gameReducer: GameState;
    enemyAgentReducer: AgentState;
    playerAgentReducer: AgentState;
}

export const rootReducer = combineReducers({
    gameReducer,
    enemyAgentReducer,
    playerAgentReducer,
});
