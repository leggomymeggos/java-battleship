import {all} from "redux-saga/effects";
import {checkWinnerSaga, newGameSaga, attackSaga, fetchActivePlayerSaga} from "./game/gameSaga";
import {saveOpposingAgentIdSaga, takeTurnSaga} from "./agent/opposingAgentSaga";

export default function* rootSaga() {
    yield all([
        newGameSaga(),
        attackSaga(),
        checkWinnerSaga(),
        fetchActivePlayerSaga(),
        saveOpposingAgentIdSaga(),
        takeTurnSaga()
    ])
}