import actions, {GameActions} from "../gameActions";
import {Tile} from "../../domain/Tile";
import {Agent} from "../../agent/Agent";

describe("createNewGame", () => {
    it("returns NEW_GAME action", () => {
        expect(actions.createNewGame()).toEqual({
            type: GameActions.NEW_GAME,
        });
    });
});

describe("newGameCreated", () => {
    it("returns NEW_GAME_CREATED action", () => {
        expect(actions.newGameCreated({computerPlayer: new Agent(), humanPlayer: new Agent()})).toEqual({
            type: GameActions.NEW_GAME_CREATED,
            payload: {computerPlayer: new Agent(), humanPlayer: new Agent()}
        });
    });
});

describe("fetchWinner", () => {
    it("returns FETCH_WINNER action", () => {
        expect(actions.fetchWinner()).toEqual({type: GameActions.FETCH_WINNER})
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
        expect(actions.fetchActivePlayer()).toEqual({
            type: GameActions.FETCH_ACTIVE_PLAYER
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