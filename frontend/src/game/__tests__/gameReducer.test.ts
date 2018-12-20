import gameReducer, {initialState} from "../gameReducer";
import {gameWon} from "../gameActions";
import {Agent} from "../../agent/Agent";

describe("game reducer", () => {
    it("has a default state", () => {
        expect(initialState)
            .toEqual({
                winner: null,
                humanPlayer: null,
                computerPlayer: null
            });
    });

    it("updates winner when the game is won", () => {
        let player = new Agent(123);
        const gameState = gameReducer(initialState, gameWon(player));

        expect(gameState.winner).toBe(player);
    });
});