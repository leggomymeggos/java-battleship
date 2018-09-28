import boardReducer, {initialState} from "../boardReducer";
import {BoardActions} from "../boardActions";
import {Tile} from "../../domain/Tile";
import {GameActions} from "../../game/gameActions";

describe("board reducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            coordinates: [[]]
        });
    });

    it("handles NEW_GAME_CREATED action", () => {
        const state = boardReducer(initialState, {type: GameActions.NEW_GAME_CREATED, payload: [[new Tile()], [new Tile()]]});

        expect(state.coordinates).toEqual([[new Tile()], [new Tile()]]);
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