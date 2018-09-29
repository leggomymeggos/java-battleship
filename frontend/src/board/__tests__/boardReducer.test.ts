import boardReducer, {initialState} from "../boardReducer";
import {BoardActions} from "../boardActions";
import {Tile} from "../../domain/Tile";
import {GameActions} from "../../game/gameActions";

describe("board reducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            grid: [[]]
        });
    });

    it("handles NEW_GAME_CREATED action", () => {
        const state = boardReducer(initialState, {type: GameActions.NEW_GAME_CREATED, payload: {grid: [[new Tile()], [new Tile()]]}});

        expect(state.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("updates the hit tile", () => {
        const prevState = {
            grid: [
                [new Tile(), new Tile()],
                [new Tile(), new Tile()]
            ]
        };

        const state = boardReducer(prevState, {type: BoardActions.TILE_HIT, payload: {xCoordinate: 1, yCoordinate: 0}});

        expect(state.grid[0][1].hit).toBeTruthy();
    });
});