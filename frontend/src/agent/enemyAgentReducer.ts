import {Action, handleActions} from "redux-actions";
import {GameActions} from "../game/gameActions";
import {BoardActions} from "../board/boardActions";
import {AgentState} from "../domain/agent";

export const initialState: AgentState = {
    id: -1,
    grid: [[]],
    sunkenShips: []
};


const enemyAgentReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            id: action.payload.computerPlayer.id,
            grid: action.payload.computerPlayer.board.grid,
            sunkenShips: action.payload.computerPlayer.board.sunkenShips
        }
    },
    [BoardActions.BOARD_HIT_SUCCESS]: (state: AgentState, action: Action<any>) => {
        return {
            ...state,
            grid: action.payload.grid,
            sunkenShips: action.payload.sunkenShips
        }
    },
}, initialState);

export default enemyAgentReducer;
