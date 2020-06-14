import {all} from "redux-saga/effects";
import {checkWinnerSaga, newGameSaga, attackSaga, fetchActivePlayerSaga} from "./game/gameSaga";
import {fetchOpposingAgentBoardSaga, fetchUserAgentBoardSaga} from "./board/boardSaga";
import {saveOpposingAgentIdSaga, takeTurnSaga} from "./agent/opposingAgentSaga";

export default function* rootSaga() {
    yield all([
        newGameSaga(),
        fetchOpposingAgentBoardSaga(),
        fetchUserAgentBoardSaga(),
        attackSaga(),
        checkWinnerSaga(),
        fetchActivePlayerSaga(),
        saveOpposingAgentIdSaga(),
        takeTurnSaga(),
    ])
}