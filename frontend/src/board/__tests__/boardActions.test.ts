import {BoardActions, boardHit, boardHitSuccess} from "../boardActions";
import {Tile} from "../../domain/Tile";

describe("boardHit", () => {
    it("returns BOARD_HIT action", () => {
        expect(boardHit({
            x: 1,
            y: 2
        })).toEqual({
            type: BoardActions.BOARD_HIT,
            payload: {
                x: 1,
                y: 2
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
