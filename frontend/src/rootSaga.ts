import {all} from "redux-saga/effects";
import {checkWinnerSaga, fetchGameSaga, attackSaga, fetchActivePlayerSaga} from "./game/gameSaga";
import {saveEnemyAgentIdSaga, takeTurnSaga} from "./agent/enemyAgentSaga";

export default function* rootSaga() {
    yield all([
        fetchGameSaga(),
        attackSaga(),
        checkWinnerSaga(),
        fetchActivePlayerSaga(),
        saveEnemyAgentIdSaga(),
        takeTurnSaga()
    ])
}