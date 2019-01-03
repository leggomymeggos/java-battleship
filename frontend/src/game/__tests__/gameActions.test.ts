import actions, {GameActions} from "../gameActions";
import {Tile} from "../../domain/tile";
import {Agent} from "../../domain/agent";
import {Game} from "../../domain/game";

describe("createNewGame", () => {
    it("returns NEW_GAME action", () => {
        expect(actions.createNewGame()).toEqual({
            type: GameActions.NEW_GAME,
        });
    });
});

describe("newGameCreated", () => {
    it("returns NEW_GAME_CREATED action", () => {
        let game = new Game();
        expect(actions.newGameCreated(game)).toEqual({
            type: GameActions.NEW_GAME_CREATED,
            payload: game
        });
    });
});

describe("fetchWinner", () => {
    it("returns FETCH_WINNER action", () => {
<<<<<<< HEAD
        expect(actions.fetchWinner(303)).toEqual({type: GameActions.FETCH_WINNER, gameId: 303})
=======
        expect(actions.fetchWinner(303)).toEqual({type: GameActions.FETCH_WINNER, payload: 303})
>>>>>>> More than one game can be played at once! Across tabs/browsers, that is
    });
});

describe("gameWon", () => {
    it("returns GAME_WON action", () => {
        const player = new Agent(1, {grid: [[new Tile()]], sunkenShips: [""]});
        expect(actions.gameWon(player)).toEqual({
            type: GameActions.GAME_WON,
            payload: player
        });
    });
});

describe("fetchActivePlayer", () => {
    it("returns FETCH_ACTIVE_PLAYER action", () => {
        expect(actions.fetchActivePlayer(42)).toEqual({
            type: GameActions.FETCH_ACTIVE_PLAYER,
<<<<<<< HEAD
            gameId: 42
=======
            payload: 42
>>>>>>> More than one game can be played at once! Across tabs/browsers, that is
        })
    });
});

describe("activePlayerUpdated", () => {
    it("returns ACTIVE_PLAYER_UPDATED action", () => {
        expect(actions.activePlayerUpdated(123)).toEqual({
            type: GameActions.ACTIVE_PLAYER_UPDATED,
            payload: 123
        })
    });
});
