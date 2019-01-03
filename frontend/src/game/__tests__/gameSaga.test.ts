jest.mock("../gameApi");

import {Tile} from "../../domain/tile";
import * as gameSaga from "../gameSaga";
import {GameApi} from "../gameApi";
import {call, put} from "redux-saga/effects";
import {Coordinate} from "../../board/boardActions";

describe("fetchGame", () => {
    it("calls api to get game", () => {
        const generator = gameSaga.fetchGame();

        expect(generator.next().value).toEqual(call(GameApi.newGame));
    });

    it("triggers action to set board", () => {
        const generator = gameSaga.fetchGame();

        generator.next();

        expect(generator.next({computerPlayer: {board: {grid: [[new Tile()]]}}}).value).toEqual(put({
            type: "NEW_GAME_CREATED",
            payload: {computerPlayer: {board: {grid: [[new Tile()]]}}}
        }));
    });
});

describe("attack", () => {
    it("calls api to attack", () => {
        let coordinate = new Coordinate(0, 0);
        const generator = gameSaga.attack(123,2, coordinate);

        expect(generator.next().value).toEqual(call(GameApi.attack, 123, 2, coordinate));
    });

    it("triggers action to update board", () => {
        const generator = gameSaga.attack(0, 0, new Coordinate(1, 2));

        generator.next();

        expect(generator.next({grid: [[new Tile()]]}).value).toEqual(put({
            type: "BOARD_HIT_SUCCESS",
            payload: {grid: [[new Tile()]]}
        }));
    });

    it("dispatches action to check for winner", () => {
        const generator = gameSaga.attack(374, 0, new Coordinate(1, 2));

        generator.next();
        generator.next({grid: []});

        expect(generator.next().value).toEqual(put({
            type: "FETCH_WINNER",
            payload: 374
        }));
    });

    it("dispatches action to get active player", () => {
        const generator = gameSaga.attack(456, 0, new Coordinate(1, 2));

        generator.next();
        generator.next({grid: []});
        generator.next();

        expect(generator.next().value).toEqual(put({
            type: "FETCH_ACTIVE_PLAYER",
            payload: 456
        }));
    });
});

describe("checkWinner", () => {
    it("calls api to check winner", () => {
        const generator = gameSaga.checkWinner(123);

        expect(generator.next().value).toEqual(call(GameApi.fetchWinner, 123));
    });

    it("dispatches winner", () => {
        const generator = gameSaga.checkWinner(0);

        generator.next();

        expect(generator.next(true).value).toEqual(put({
            type: "GAME_WON",
            payload: true
        }));
    });

    it("does nothing if there is no winner", () => {
        const generator = gameSaga.checkWinner(0);

        generator.next();

        expect(generator.next(false).value).toBeUndefined();
    });
});

describe("checkActivePlayer", () => {
    it("calls api to get active player", () => {
        const generator = gameSaga.checkActivePlayer(709);

        expect(generator.next().value).toEqual(call(GameApi.fetchActivePlayer, 709));
    });

    it("dispatches active player", () => {
        const generator = gameSaga.checkActivePlayer(0);

        generator.next();

        expect(generator.next(123).value).toEqual(put({
            type: "ACTIVE_PLAYER_UPDATED",
            payload: 123
        }));
    });
});