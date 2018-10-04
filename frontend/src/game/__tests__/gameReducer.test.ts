import gameReducer, {initialState} from "../gameReducer";
import {GameActions, gameWon} from "../gameActions";

describe("game reducer", () => {
    it("has a default state", () => {
        expect(initialState)
            .toEqual({
                winner: false
            });
    });

    it("updates winner when the game is won", () => {
        const gameState = gameReducer(initialState, {type: GameActions.GAME_WON, payload: true});

        expect(gameState.winner).toBeTruthy();
    });
});