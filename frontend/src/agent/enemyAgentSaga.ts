import {call, put, takeEvery} from "redux-saga/effects";
import {GameActions} from "../game/gameActions";
import {Action} from "redux-actions";
import {userBoardHitSuccess} from "../board/boardActions";
import {EnemyAgentApi} from "./enemyAgentApi";

let enemyAgentId: number = -1;

export function* takeTurn(gameId: number, attackerId: number): any {
    try {
        const newBoard = yield call(EnemyAgentApi.attack, gameId, attackerId);
        yield put(userBoardHitSuccess(gameId, newBoard));
    } catch (e) {
        console.error(e);
    }
}

export function saveEnemyAgent(id: number) {
    enemyAgentId = id;
}

export function* saveEnemyAgentIdSaga(): any {
    yield takeEvery(GameActions.NEW_GAME_CREATED, (action: Action<any>) => {
        return saveEnemyAgent(action.payload.players[1].id);
    });
}

export function* takeTurnSaga(): any {
    yield takeEvery(GameActions.ACTIVE_PLAYER_UPDATED, (action: Action<any>) => {
        if (action.payload.activePlayerId == enemyAgentId) {
            return takeTurn(action.payload.gameId, enemyAgentId);
        }
    });
}