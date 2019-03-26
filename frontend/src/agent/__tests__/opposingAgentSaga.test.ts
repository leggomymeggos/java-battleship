jest.mock("../opposingAgentApi.ts");

import * as opposingAgentSaga from "../opposingAgentSaga";
import {call, put} from "redux-saga/effects";
import {OpposingAgentApi} from "../opposingAgentApi";
import {BoardActions} from "../../board/boardActions";

describe("takeTurn", () => {
    it("calls attack api", () => {
        const generator = opposingAgentSaga.takeTurn(123, 456);

        expect(generator.next().value).toEqual(call(OpposingAgentApi.attack, 123, 456));
    });

    it("triggers action to update board", () => {
        const generator = opposingAgentSaga.takeTurn(123, 456);

        generator.next();

        expect(generator.next("sure did hit the board").value).toEqual(put({
            type: BoardActions.USER_BOARD_HIT_SUCCESS,
            payload: {
                gameId: 123, board: "sure did hit the board"
            }
        }));
    });
});
