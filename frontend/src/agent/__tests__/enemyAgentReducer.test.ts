import {GameActions} from "../../game/gameActions";
import {Tile} from "../../domain/Tile";
import {BoardActions} from "../../board/boardActions";
import enemyAgentReducer, {initialState} from "../enemyAgentReducer";
import {AgentState} from "../Agent";

describe("enemyAgentReducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            id: -1,
            grid: [[]],
            sunkenShips: []
        });
    });

    it("handles NEW_GAME_CREATED action", () => {
        const state = enemyAgentReducer(initialState, {
            type: GameActions.NEW_GAME_CREATED,
            payload: {
                computerPlayer: {
                    id: 123, board: {grid: [[new Tile()], [new Tile()]], sunkenShips: ["none yet"]}
                },
                humanPlayer: {
                    id: 789, board: {grid: [[new Tile()], [new Tile()], [new Tile()]], sunkenShips: ["super ships sunk"]}
                }
            }
        });

        expect(state.id).toEqual(123);
        expect(state.grid).toEqual([[new Tile()], [new Tile()]]);
        expect(state.sunkenShips).toEqual(["none yet"]);
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
            const state = enemyAgentReducer(prevState, {type: BoardActions.BOARD_HIT_SUCCESS, payload: board});

            expect(state.grid).toEqual(board.grid);
        });

        it("updates sunken ships", () => {
            const prevState: AgentState = {
                id: 1,
                grid: [[]],
                sunkenShips: []
            };

            let board = {grid: [[new Tile()]], sunkenShips: ["the battleship"]};
            const state = enemyAgentReducer(prevState, {type: BoardActions.BOARD_HIT_SUCCESS, payload: board});

            expect(state.sunkenShips).toEqual(["the battleship"]);
        });

        it("does not update id", () => {
            const prevState: AgentState = {
                id: 1,
                grid: [[]],
                sunkenShips: []
            };

            let board = {id: 456, grid: [[new Tile()]], sunkenShips: ["the battleship"]};
            const state = enemyAgentReducer(prevState, {type: BoardActions.BOARD_HIT_SUCCESS, payload: board});

            expect(state.id).toEqual(1);
        });
    });
});