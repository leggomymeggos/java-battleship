import boardReducer, {BoardState, initialState} from "../boardReducer";
import {BoardActions} from "../boardActions";
import {Tile} from "../../domain/Tile";
import {GameActions} from "../../game/gameActions";

describe("board reducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            grid: [[]],
            sunkenShips: []
        });
    });

    it("handles NEW_GAME_CREATED action", () => {
        const state = boardReducer(initialState, {
            type: GameActions.NEW_GAME_CREATED,
            payload: {grid: [[new Tile()], [new Tile()]], sunkenShips: ["none yet"]}
        });

        expect(state.grid).toEqual([[new Tile()], [new Tile()]]);
        expect(state.sunkenShips).toEqual([]);
    });

    describe("BOARD_HIT_SUCCESS", () => {
        it("updates the hit tile", () => {
            const prevState = {
                grid: [
                    [new Tile(), new Tile()],
                    [new Tile(), new Tile()]
                ]
            };

            let board = {grid: [[new Tile()]]};
            const state = boardReducer(prevState, {type: BoardActions.BOARD_HIT_SUCCESS, payload: board});

            expect(state.grid).toEqual(board.grid);
        });

        it("updates sunken ships", () => {
            const prevState: BoardState = {
                grid: [[]],
                sunkenShips: []
            };

            let board = {grid: [[new Tile()]], sunkenShips: ["the battleship"]};
            const state = boardReducer(prevState, {type: BoardActions.BOARD_HIT_SUCCESS, payload: board});

            expect(state.sunkenShips).toEqual(["the battleship"]);
        });
    });
});