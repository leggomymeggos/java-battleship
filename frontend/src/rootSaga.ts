import {all} from "redux-saga/effects";
import {fetchGameSaga, hitBoardSaga} from "./game/gameSaga";

export default function* rootSaga() {
    yield all([
        fetchGameSaga(),
        hitBoardSaga(),
    ])
}