import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {newGameCreated} from "./gameActions";
import {Coordinate, boardHitSuccess, BoardActions} from "../board/boardActions";
import {Action} from "redux-actions";

export function* fetchGame(): any {
    try {
        const game = yield call(GameApi.newGame);
        yield put(newGameCreated(game.player.board));
    } catch (e) {
        console.error(e)
    }
}

export function* hitBoard(coordinate: Coordinate): any {
    try {
        const newBoard = yield call(GameApi.hitBoard, coordinate);
        yield put(boardHitSuccess(newBoard));
    } catch (e) {
        console.error(e)
    }
}

export function* fetchGameSaga(): any {
    yield takeEvery(BoardActions.GET_INITIAL_BOARD, fetchGame)
}

export function* hitBoardSaga(): any {
    yield takeEvery(BoardActions.BOARD_HIT, (action: Action<Coordinate>) => {
        return hitBoard(action.payload)
    })
}