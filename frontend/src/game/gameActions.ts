import {createAction} from "redux-actions";
import {Tile} from "../domain/Tile";

export const GameActions = {
    NEW_GAME_CREATED: "NEW_GAME_CREATED",
};

export const newGameCreated =
    createAction(GameActions.NEW_GAME_CREATED, (payload: {grid: Tile[][], sunkenShips: string[]}) => payload);
