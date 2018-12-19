import {createAction} from "redux-actions";
import {Tile} from "../domain/Tile";
import {Player} from "../agent/Player";

export const GameActions = {
    NEW_GAME: "NEW_GAME",
    NEW_GAME_CREATED: "NEW_GAME_CREATED",
    FETCH_WINNER: "FETCH_WINNER",
    GAME_WON: "GAME_WON",
};

export const createNewGame =
    createAction(GameActions.NEW_GAME);

export const newGameCreated =
    createAction(GameActions.NEW_GAME_CREATED, (payload: { grid: Tile[][], sunkenShips: string[]; }) => payload);

export const fetchWinner =
    createAction(GameActions.FETCH_WINNER);

export const gameWon =
    createAction(GameActions.GAME_WON, (payload: Player) => {
        return payload
    });

export default {
    createNewGame,
    newGameCreated,
    fetchWinner,
    gameWon
}