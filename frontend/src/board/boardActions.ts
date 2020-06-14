import {createAction} from "redux-actions";
import {Tile} from "../domain/tile";
import {Board} from "../domain/board";

export const BoardActions = {
    OPPONENT_BOARD_HIT_SUCCESS: "OPPONENT_BOARD_HIT_SUCCESS",
    USER_BOARD_HIT_SUCCESS: "USER_BOARD_HIT_SUCCESS",
    BOARD_HIT: "BOARD_HIT",
    FETCH_USER_AGENT_BOARD: "FETCH_USER_AGENT_BOARD",
    USER_AGENT_BOARD_FETCHED: "USER_AGENT_BOARD_FETCHED",
    FETCH_OPPOSING_AGENT_BOARD: "FETCH_OPPOSING_AGENT_BOARD",
    OPPOSING_AGENT_BOARD_FETCHED: "OPPOSING_AGENT_BOARD_FETCHED"
};

export class Coordinate {
    column: number;
    row: number;

    constructor(column: number, row: number) {
        this.column = column;
        this.row = row;
    }
}

export const boardHit = createAction(BoardActions.BOARD_HIT, (
    gameId: number,
    attackerId: number,
    coordinates: Coordinate
) => {
    return {gameId, attackerId, coordinates}
});

export const opponentBoardHitSuccess =
    createAction(BoardActions.OPPONENT_BOARD_HIT_SUCCESS, (
        gameId: number, response: { result: { hitType?: string, shipName?: string }, board: { grid: Tile[][], sunkenShips: string[] } }
    ) => {
        return {gameId, response}
    });

export const userBoardHitSuccess = createAction(BoardActions.USER_BOARD_HIT_SUCCESS, (gameId: number, response: { result: string, board: { grid: Tile[][], sunkenShips: string[]; } }) => {
    return {gameId, response}
});

export const fetchUserAgentBoard =
    createAction(BoardActions.FETCH_USER_AGENT_BOARD, (
        gameId: number, playerId: number
        ) => {
            return {gameId, playerId}
        }
    );

export const fetchOpposingAgentBoard =
    createAction(BoardActions.FETCH_OPPOSING_AGENT_BOARD, (
        gameId: number, playerId: number
        ) => {
            return {gameId, playerId}
        }
    );

export const opposingAgentBoardFetched =
    createAction(BoardActions.OPPOSING_AGENT_BOARD_FETCHED, (board: Board) => {
        return {
            grid: board.grid,
            sunkenShips: board.sunkenShips
        }
    });

export const userAgentBoardFetched =
    createAction(BoardActions.USER_AGENT_BOARD_FETCHED, (board: Board) => {
        return {
            grid: board.grid,
            sunkenShips: board.sunkenShips
        }
    });

export default {
    fetchUserAgentBoard,
    fetchOpposingAgentBoard,
    opponentBoardHitSuccess,
    userBoardHitSuccess,
    boardHit
}