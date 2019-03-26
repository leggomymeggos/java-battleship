import {all} from "redux-saga/effects";
import {checkWinnerSaga, fetchGameSaga, attackSaga, fetchActivePlayerSaga} from "./game/gameSaga";
import {saveOpposingAgentIdSaga, takeTurnSaga} from "./agent/opposingAgentSaga";

export default function* rootSaga() {
    yield all([
        fetchGameSaga(),
        attackSaga(),
        checkWinnerSaga(),
        fetchActivePlayerSaga(),
        saveOpposingAgentIdSaga(),
        takeTurnSaga()
    ])
}