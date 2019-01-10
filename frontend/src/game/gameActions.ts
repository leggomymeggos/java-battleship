import {createAction} from "redux-actions";
import {Agent} from "../domain/agent";
import {Game} from "../domain/game";

export const GameActions = {
    NEW_GAME: "NEW_GAME",
    NEW_GAME_CREATED: "NEW_GAME_CREATED",
    FETCH_WINNER: "FETCH_WINNER",
    FETCH_ACTIVE_PLAYER: "FETCH_ACTIVE_PLAYER",
    GAME_WON: "GAME_WON",
    ACTIVE_PLAYER_UPDATED: "ACTIVE_PLAYER_UPDATED",
};

export const createNewGame =
    createAction(GameActions.NEW_GAME);

export const newGameCreated =
    createAction(GameActions.NEW_GAME_CREATED, (payload: Game) => payload);

export const fetchWinner =
    createAction(GameActions.FETCH_WINNER, (payload: number) => payload);

export const gameWon =
    createAction(GameActions.GAME_WON, (payload: Agent) => payload);

export const fetchActivePlayer =
    createAction(GameActions.FETCH_ACTIVE_PLAYER, (payload: number) => payload);

export const activePlayerUpdated =
    createAction(GameActions.ACTIVE_PLAYER_UPDATED, (payload: { gameId: number, activePlayerId: number }) => {
        return payload
    });

export default {
    createNewGame,
    newGameCreated,
    fetchWinner,
    fetchActivePlayer,
    activePlayerUpdated,
    gameWon
}
