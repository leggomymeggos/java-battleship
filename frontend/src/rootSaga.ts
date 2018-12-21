import {all} from "redux-saga/effects";
import {checkWinnerSaga, fetchGameSaga, attackSaga} from "./game/gameSaga";

export default function* rootSaga() {
    yield all([
        fetchGameSaga(),
        attackSaga(),
        checkWinnerSaga()
    ])
}