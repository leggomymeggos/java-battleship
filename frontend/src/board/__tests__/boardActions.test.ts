import {BoardActions, boardHit, boardHitSuccess} from "../boardActions";
import {Tile} from "../../domain/tile";

describe("boardHit", () => {
    it("returns BOARD_HIT action", () => {
        expect(boardHit(123, {
            x: 1,
            y: 2
        })).toEqual({
            type: BoardActions.BOARD_HIT,
            payload: {
                gameId: 123,
                coordinates: {
                    x: 1,
                    y: 2
                }
            }
        });
    });
});

describe("boardHitSuccess", () => {
    it("returns BOARD_HIT_SUCCESS action", () => {
        expect(boardHitSuccess({grid: [[new Tile()]], sunkenShips: ["one"]})).toEqual({
            type: BoardActions.BOARD_HIT_SUCCESS,
            payload: {grid: [[new Tile()]], sunkenShips: ["one"]}
        });
    });
});
