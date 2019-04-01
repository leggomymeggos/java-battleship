import gameReducer, {GameStatus, initialState} from "../gameReducer";
import {createNewGame, gameWon, newGameCreated} from "../gameActions";
import {Agent} from "../../domain/agent";
import {Game} from "../../domain/game";

describe("game reducer", () => {
    it("has a default state", () => {
        expect(initialState)
            .toEqual({
                id: -1,
                winner: null,
                status: GameStatus.NONE
            });
    });

    it("updates winner when the game is won", () => {
        let player = new Agent(123);
        const gameState = gameReducer(initialState, gameWon(player));

        expect(gameState.winner).toBe(player);
    });

    it("updates status when the game is won", () => {
        let player = new Agent(123);
        const gameState = gameReducer(initialState, gameWon(player));

        expect(gameState.status).toBe(GameStatus.GAME_OVER);
    });

    it("updates status when new game is requested", () => {
        const gameState = gameReducer(initialState, createNewGame());

        expect(gameState.status).toBe(GameStatus.IN_PROGRESS);
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