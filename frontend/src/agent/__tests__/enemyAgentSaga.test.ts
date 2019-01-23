jest.mock("../enemyAgentApi.ts");

import * as enemyAgentSaga from "../enemyAgentSaga";
import {call, put} from "redux-saga/effects";
import {EnemyAgentApi} from "../enemyAgentApi";
import {BoardActions} from "../../board/boardActions";

describe("takeTurn", () => {
    it("calls attack api", () => {
        const generator = enemyAgentSaga.takeTurn(123, 456);

        expect(generator.next().value).toEqual(call(EnemyAgentApi.attack, 123, 456));
    });

    it("triggers action to update board", () => {
        const generator = enemyAgentSaga.takeTurn(123, 456);

        generator.next();

        expect(generator.next("sure did hit the board").value).toEqual(put({
            type: BoardActions.USER_BOARD_HIT_SUCCESS,
            payload: {
                gameId: 123, board: "sure did hit the board"
            }
        }));
    });
});
