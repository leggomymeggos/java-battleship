import {createNewGame, fetchWinner, GameActions, gameWon, newGameCreated} from "../gameActions";
import {Tile} from "../../domain/Tile";
import {Player} from "../../agent/Player";

describe("createNewGame", () => {
    it("returns NEW_GAME action", () => {
        expect(createNewGame()).toEqual({
            type: GameActions.NEW_GAME,
        });
    });
});

describe("newGameCreated", () => {
    it("returns NEW_GAME_CREATED action", () => {
        expect(newGameCreated({grid: [[new Tile()]], sunkenShips: []})).toEqual({
            type: GameActions.NEW_GAME_CREATED,
            payload: {grid: [[new Tile()]], sunkenShips: []}
        });
    });
});

describe("fetchWinner", () => {
    it("returns FETCH_WINNER action", () => {
        expect(fetchWinner()).toEqual({type: GameActions.FETCH_WINNER})
    });
});

describe("gameWon", () => {
    it("returns GAME_WON action", () => {
        const player = new Player(1, {grid: [[new Tile()]], sunkenShips: [""]});
        expect(gameWon(player)).toEqual({
            type: GameActions.GAME_WON,
            payload: player
        });
    });
});