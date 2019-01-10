import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {activePlayerUpdated, fetchActivePlayer, fetchWinner, GameActions, gameWon, newGameCreated} from "./gameActions";
import {Coordinate, boardHitSuccess, BoardActions} from "../board/boardActions";
import {Action} from "redux-actions";

export function* fetchGame(): any {
    try {
        const game = yield call(GameApi.newGame);
        yield put(newGameCreated(game));
    } catch (e) {
        console.error(e);
    }
}

export function* attack(gameId: number, attackingPlayerId: number, coordinate: Coordinate): any {
    try {
        const newBoard = yield call(GameApi.attack, gameId, attackingPlayerId, coordinate);
        yield put(boardHitSuccess(newBoard));
        yield put(fetchWinner(gameId));
        yield put(fetchActivePlayer(gameId));
    } catch (e) {
        console.error(e)
    }
}

export function* checkWinner(gameId: number): any {
    try {
        const winner = yield call(GameApi.fetchWinner, gameId);
        if (!winner) {
            return;
        }
        yield put(gameWon(winner));
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

export function* fetchGameSaga(): any {
    yield takeEvery(GameActions.NEW_GAME, fetchGame);
}

export function* attackSaga(): any {
    yield takeEvery(BoardActions.BOARD_HIT, (action: Action<any>) => {
        return attack(action.payload.gameId, 1, action.payload.coordinates);
    });
}

export function* checkWinnerSaga(): any {
    yield takeEvery(GameActions.FETCH_WINNER, (action: Action<any>) => {
        return checkWinner(action.payload);
    });
}

export function* fetchActivePlayerSaga(): any {
    yield takeEvery(GameActions.FETCH_ACTIVE_PLAYER, (action: Action<any>) => {
        return checkActivePlayer(action.payload);
    });
}