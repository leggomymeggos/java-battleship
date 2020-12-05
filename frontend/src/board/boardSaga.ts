import {Action} from "redux-actions";
import {call, put, takeEvery} from "redux-saga/effects";
import {BoardActions, opposingAgentBoardFetched, userAgentBoardFetched} from "./boardActions";
import {fetchBoard} from "./boardApi";

export function* fetchOpposingAgentBoard(gameId: number, playerId: number): any {
    try {
        const board = yield call(fetchBoard, gameId, playerId);
        yield put(opposingAgentBoardFetched(board))
    } catch (e) {
        console.error(e);
    }
}

export function* fetchUserAgentBoard(gameId: number, playerId: number): any {
    try {
        const board = yield call(fetchBoard, gameId, playerId);
        yield put(userAgentBoardFetched(board))
    } catch (e) {
        console.error(e);
    }
}

export function* fetchOpposingAgentBoardSaga(): any {
    yield takeEvery(BoardActions.FETCH_OPPOSING_AGENT_BOARD, (action: Action<any>) => {
        return fetchOpposingAgentBoard(action.payload.gameId, action.payload.playerId)
    });
}

export function* fetchUserAgentBoardSaga(): any {
    yield takeEvery(BoardActions.FETCH_USER_AGENT_BOARD, (action: Action<any>) => {
        return fetchUserAgentBoard(action.payload.gameId, action.payload.playerId)
    });
}
