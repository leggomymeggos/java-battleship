import {createNewGame, fetchWinner, GameActions, gameWon, newGameCreated} from "../gameActions";
import {Tile} from "../../domain/Tile";
import {Agent} from "../../agent/Agent";

describe("createNewGame", () => {
    it("returns NEW_GAME action", () => {
        expect(createNewGame()).toEqual({
            type: GameActions.NEW_GAME,
        });
    });
});

describe("newGameCreated", () => {
    it("returns NEW_GAME_CREATED action", () => {
        expect(newGameCreated({computerPlayer: new Agent(), humanPlayer: new Agent()})).toEqual({
            type: GameActions.NEW_GAME_CREATED,
            payload: {computerPlayer: new Agent(), humanPlayer: new Agent()}
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
        const player = new Agent(1, {grid: [[new Tile()]], sunkenShips: [""]});
        expect(gameWon(player)).toEqual({
            type: GameActions.GAME_WON,
            payload: player
        });
    });
});