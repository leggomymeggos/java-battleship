import {Action, handleActions} from "redux-actions";
import {GameActions} from "../game/gameActions";
import {BoardActions} from "../board/boardActions";
import {AgentState} from "../domain/agent";

export const initialState: AgentState = {
    id: -1,
    grid: [[]],
    sunkenShips: []
};


const userAgentReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            id: action.payload.players[0].id,
            grid: action.payload.players[0].board.grid,
            sunkenShips: action.payload.players[0].board.sunkenShips
        }
    },
    [BoardActions.USER_BOARD_HIT_SUCCESS]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload.response.board.grid,
            sunkenShips: action.payload.response.board.sunkenShips
        }
    },
}, initialState);

export default userAgentReducer;
