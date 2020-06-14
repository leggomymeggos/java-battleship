import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {activePlayerUpdated, GameActions, gameWon, newGameCreated} from "./gameActions";
import {BoardActions, Coordinate, fetchUserAgentBoard, fetchOpposingAgentBoard, opponentBoardHitSuccess} from "../board/boardActions";
import {Action} from "redux-actions";

export function* newGame(): any {
    try {
        const game = yield call(GameApi.newGame);
        yield put(newGameCreated(game));
        yield put(fetchUserAgentBoard(game.id, game.playerIds[0]));
        yield put(fetchOpposingAgentBoard(game.id, game.playerIds[1]));
    } catch (e) {
        console.error(e);
    }
}

export function* attack(gameId: number, attackingPlayerId: number, coordinate: Coordinate): any {
    try {
        const attackResult = yield call(GameApi.attack, gameId, attackingPlayerId, coordinate);
        yield put(opponentBoardHitSuccess(gameId, attackResult));
    } catch (e) {
        console.error(e)
    }
}

export function* checkWinner(gameId: number): any {
    try {
        const winner = yield call(GameApi.fetchWinner, gameId);
        if (!winner.winnerId) {
            return;
        }
        yield put(gameWon(winner.winnerId));
    } catch (e) {
        console.error(e)
    }
}

export function* checkActivePlayer(gameId: number): any {
    try {
        const activePlayerId = yield call(GameApi.fetchActivePlayer, gameId);
        yield put(activePlayerUpdated({gameId, activePlayerId}))
    } catch (e) {
        console.error(e)
    }
}

export function* newGameSaga(): any {
    yield takeEvery(GameActions.NEW_GAME, newGame);
}

export function* attackSaga(): any {
    yield takeEvery(BoardActions.BOARD_HIT, (action: Action<any>) => {
        return attack(action.payload.gameId, action.payload.attackerId, action.payload.coordinates);
    });
}

export function* fetchActivePlayerSaga(): any {
    yield takeEvery([BoardActions.OPPONENT_BOARD_HIT_SUCCESS, BoardActions.USER_BOARD_HIT_SUCCESS], (action: Action<any>) => {
        return checkActivePlayer(action.payload.gameId);
    });
}

export function* checkWinnerSaga(): any {
    yield takeEvery([BoardActions.OPPONENT_BOARD_HIT_SUCCESS, BoardActions.USER_BOARD_HIT_SUCCESS], (action: Action<any>) => {
        return checkWinner(action.payload.gameId);
    })
}
