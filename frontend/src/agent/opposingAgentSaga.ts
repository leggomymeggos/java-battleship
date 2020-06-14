import {call, put, takeEvery} from "redux-saga/effects";
import {GameActions} from "../game/gameActions";
import {Action} from "redux-actions";
import {userBoardHitSuccess} from "../board/boardActions";
import {OpposingAgentApi} from "./opposingAgentApi";

let opposingAgentId: number = -1;

export function* takeTurn(gameId: number, attackerId: number): any {
    try {
        const newBoard = yield call(OpposingAgentApi.attack, gameId, attackerId);
        yield put(userBoardHitSuccess(gameId, newBoard));
    } catch (e) {
        console.error(e);
    }
}

export function saveOpposingAgent(id: number) {
    opposingAgentId = id;
}

export function* saveOpposingAgentIdSaga(): any {
    yield takeEvery(GameActions.NEW_GAME_CREATED, (action: Action<any>) => {
        return saveOpposingAgent(action.payload.playerIds[1]);
    });
}

export function* takeTurnSaga(): any {
    yield takeEvery(GameActions.ACTIVE_PLAYER_UPDATED, (action: Action<any>) => {
        if (action.payload.activePlayerId === opposingAgentId) {
            return takeTurn(action.payload.gameId, opposingAgentId);
        }
    });
}