import {Action, handleActions} from "redux-actions";
import {GameActions} from "../game/gameActions";
import {BoardActions} from "../board/boardActions";
import {AgentState} from "../domain/agent";

export const initialState: AgentState = {
    id: -1,
    grid: [[]],
    sunkenShips: [],
    recentAttackResult: {},
};


const opposingAgentReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            id: action.payload.playerIds[1]
        }
    },
    [BoardActions.OPPOSING_AGENT_BOARD_FETCHED]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload.grid,
            sunkenShips: action.payload.sunkenShips
        }
    },
    [BoardActions.OPPONENT_BOARD_HIT_SUCCESS]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload.response.board.grid,
            sunkenShips: action.payload.response.board.sunkenShips
        }
    },
    [BoardActions.USER_BOARD_HIT_SUCCESS]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            recentAttackResult: action.payload.response.result
        }
    }
}, initialState);

export default opposingAgentReducer;
