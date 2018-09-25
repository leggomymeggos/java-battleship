import {BoardActions, getInitialBoard} from "../boardActions";

describe("getInitialBoard", () => {
    it("returns GET_INITIAL_BOARD action", () => {
        expect(getInitialBoard()).toEqual({
            type: BoardActions.GET_INITIAL_BOARD,
        });
    });
});
