import {BoardActions, getInitialBoard, tileHit} from "../boardActions";

describe("getInitialBoard", () => {
    it("returns GET_INITIAL_BOARD action", () => {
        expect(getInitialBoard()).toEqual({
            type: BoardActions.GET_INITIAL_BOARD,
        });
    });

});

describe("tileHit", () => {
    it("returns TILE_HIT action", () => {
        expect(tileHit(1, 2)).toEqual({
            type: BoardActions.TILE_HIT,
            payload: {
                xCoordinate: 1,
                yCoordinate: 2
            }
        });
    });
});
