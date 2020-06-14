import {GameActions} from "../../game/gameActions";
import {Tile} from "../../domain/tile";
import {BoardActions} from "../../board/boardActions";
import opposingAgentReducer, {initialState} from "../opposingAgentReducer";
import {AgentState} from "../../domain/agent";

describe("opposingAgentReducer", () => {
    it("has an initial state", () => {
        expect(initialState).toEqual({
            id: -1,
            grid: [[]],
            sunkenShips: [],
            recentAttackResult: {}
        });
    });

    it("handles NEW_GAME_CREATED action", () => {
        const state = opposingAgentReducer(initialState, {
            type: GameActions.NEW_GAME_CREATED,
            payload: {
                playerIds: [ 789, 123 ],
            }
        });

        expect(state.id).toEqual(123);
    });

    describe("OPPONENT_BOARD_HIT_SUCCESS", () => {
        it("updates the hit tile", () => {
            const prevState = {
                grid: [
                    [new Tile(), new Tile()],
                    [new Tile(), new Tile()]
                ]
            };

            let board = {grid: [[new Tile()]]};
            const state = opposingAgentReducer(prevState, {
                type: BoardActions.OPPONENT_BOARD_HIT_SUCCESS,
                payload: {
                    response: {
                        result: 'MISS',
                        board
                    }
                }
            });

            expect(state.grid).toEqual(board.grid);
        });

        it("updates sunken ships", () => {
            const prevState: AgentState = {
                id: 1,
                grid: [[]],
                sunkenShips: [],
                recentAttackResult: {}
            };

            let board = {grid: [[new Tile()]], sunkenShips: ["the battleship"]};
            const state = opposingAgentReducer(prevState, {
                type: BoardActions.OPPONENT_BOARD_HIT_SUCCESS,
                payload: {
                    response: {
                        result: 'SUNK',
                        board
                    }
                }
            });

            expect(state.sunkenShips).toEqual(["the battleship"]);
        });

        it("does not update id", () => {
            const prevState: AgentState = {
                id: 1,
                grid: [[]],
                sunkenShips: [],
                recentAttackResult: {}
            };

            let board = {id: 456, grid: [[new Tile()]], sunkenShips: ["the battleship"]};
            const state = opposingAgentReducer(prevState, {
                type: BoardActions.OPPONENT_BOARD_HIT_SUCCESS,
                payload: {
                    response: {
                        result: 'HIT',
                        board
                    }
                }
            });

            expect(state.id).toEqual(1);
        });
    });
});
