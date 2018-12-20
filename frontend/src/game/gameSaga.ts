import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {fetchWinner, GameActions, gameWon, newGameCreated} from "./gameActions";
import {Coordinate, boardHitSuccess, BoardActions} from "../board/boardActions";
import {Action} from "redux-actions";

export function* fetchGame(): any {
    try {
        const game = yield call(GameApi.newGame);
        yield put(newGameCreated(game));
    } catch (e) {
        console.error(e)
    }
}

export function* hitBoard(defendingPlayerId: number, coordinate: Coordinate, attackingPlayerId: number): any {
    try {
        const newBoard = yield call(GameApi.hitBoard, defendingPlayerId, coordinate, attackingPlayerId);
        yield put(boardHitSuccess(newBoard));
        yield put(fetchWinner());
    } catch (e) {
        console.error(e)
    }
}

export function* checkWinner(): any {
    try {
        const winner = yield call(GameApi.fetchWinner);
        if (!winner) {
            return;
        }
        yield put(gameWon(winner));
    } catch (e) {
        console.error(e)
    }
}

export function* fetchGameSaga(): any {
    yield takeEvery(GameActions.NEW_GAME, fetchGame);
}

export function* hitBoardSaga(): any {
    yield takeEvery(BoardActions.BOARD_HIT, (action: Action<any>) => {
        return hitBoard(2, action.payload, 1);
    });
}

export function* checkWinnerSaga(): any {
    yield takeEvery(GameActions.FETCH_WINNER, checkWinner);
}