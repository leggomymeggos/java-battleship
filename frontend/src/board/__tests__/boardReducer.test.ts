import boardReducer, {initialState} from "../boardReducer";
import {BoardActions} from "../boardActions";
import {Tile} from "../../domain/Tile";

describe("board reducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            coordinates: [[]]
        });
    });

    it("handles GET_INITIAL_BOARD action", () => {
        const state = boardReducer(initialState, {type: BoardActions.GET_INITIAL_BOARD});

        expect(state.coordinates).toEqual([
            [new Tile(1), new Tile(), new Tile(), new Tile(2), new Tile(2), new Tile(2), new Tile(2), new Tile(), new Tile(), new Tile()],
            [new Tile(1), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(), new Tile(), new Tile(4), new Tile(4), new Tile(4), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
            [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(5), new Tile(5)],
        ]);
    });

    it("updates the hit tile", () => {
        const prevState = {
            coordinates: [
                [new Tile(), new Tile()],
                [new Tile(), new Tile()]
            ]
        };

        const state = boardReducer(prevState, {type: BoardActions.TILE_HIT, payload: {xCoordinate: 1, yCoordinate: 0}});

        expect(state.coordinates[0][1].hit).toBeTruthy();
    });
});