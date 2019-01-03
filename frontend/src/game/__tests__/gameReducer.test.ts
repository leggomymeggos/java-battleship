import gameReducer, {initialState} from "../gameReducer";
import {gameWon, newGameCreated} from "../gameActions";
import {Agent} from "../../domain/agent";
import {Game} from "../../domain/game";

describe("game reducer", () => {
    it("has a default state", () => {
        expect(initialState)
            .toEqual({
                id: -1,
                winner: null
            });
    });

    it("updates winner when the game is won", () => {
        let player = new Agent(123);
        const gameState = gameReducer(initialState, gameWon(player));

        expect(gameState.winner).toBe(player);
    });

    it("updates id when new game is created", () => {
        const gameState = gameReducer(initialState, newGameCreated(new Game(789)));

        expect(gameState.id).toBe(789);
    });

    it("clears winner when new game is created", () => {
        const gameState = gameReducer({id: -1, winner: new Agent()}, newGameCreated(new Game(789)));

        expect(gameState.winner).toBe(null);
    });
});