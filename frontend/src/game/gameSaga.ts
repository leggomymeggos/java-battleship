import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {activePlayerUpdated, GameActions, gameWon, newGameCreated} from "./gameActions";
import {BoardActions, Coordinate, opponentBoardHitSuccess} from "../board/boardActions";
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
        yield put(opponentBoardHitSuccess(gameId, newBoard));
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
