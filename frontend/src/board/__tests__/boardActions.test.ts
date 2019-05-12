import {BoardActions, boardHit, opponentBoardHitSuccess} from "../boardActions";
import {Tile} from "../../domain/tile";

describe("boardHit", () => {
    it("returns BOARD_HIT action", () => {
        expect(boardHit(123,
            456,
            {
                x: 1,
                y: 2
            })).toEqual({
            type: BoardActions.BOARD_HIT,
            payload: {
                gameId: 123,
                attackerId: 456,
                coordinates: {
                    x: 1,
                    y: 2
                }
            }
        });
    });
});

describe("opponentBoardHitSuccess", () => {
    it("returns OPPONENT_BOARD_HIT_SUCCESS action", () => {
        expect(opponentBoardHitSuccess(1, {grid: [[new Tile()]], sunkenShips: ["one"]})).toEqual({
            type: BoardActions.OPPONENT_BOARD_HIT_SUCCESS,
            payload: {
                gameId: 1,
                board: {grid: [[new Tile()]], sunkenShips: ["one"]}
            }
        });
    });
});
