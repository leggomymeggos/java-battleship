import {call, put, takeEvery} from "redux-saga/effects";
import {GameApi} from "./gameApi";
import {newGameCreated} from "./gameActions";

export function* fetchGame(): any {
    try {
        const game = yield call(GameApi.newGame);
        yield put(newGameCreated(game.board));
    } catch (e) {
        console.error(e)
    }
}

export function* fetchGameSaga(): any {
    yield takeEvery("GET_INITIAL_BOARD", fetchGame)
}
