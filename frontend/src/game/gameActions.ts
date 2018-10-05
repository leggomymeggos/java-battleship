import {createAction} from "redux-actions";
import {Tile} from "../domain/Tile";

export const GameActions = {
    NEW_GAME_CREATED: "NEW_GAME_CREATED",
    FETCH_WINNER: "FETCH_WINNER",
    GAME_WON: "GAME_WON",
};

export const newGameCreated =
    createAction(GameActions.NEW_GAME_CREATED, (payload: { grid: Tile[][], sunkenShips: string[]; }) => payload);

export const fetchWinner =
    createAction(GameActions.FETCH_WINNER);

export const gameWon =
    createAction(GameActions.GAME_WON, (payload: boolean) => {
        return payload
    });
