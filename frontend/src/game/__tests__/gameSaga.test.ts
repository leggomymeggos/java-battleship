jest.mock("../gameApi");

import {Tile} from "../../domain/Tile";
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
        const generator = gameSaga.attack(2, coordinate);

        expect(generator.next().value).toEqual(call(GameApi.attack, 2, coordinate));
    });

    it("triggers action to update board", () => {
        const generator = gameSaga.attack(0, new Coordinate(1, 2));

        generator.next();

        expect(generator.next({grid: [[new Tile()]]}).value).toEqual(put({
            type: "BOARD_HIT_SUCCESS",
            payload: {grid: [[new Tile()]]}
        }));
    });

    it("dispatches action to check for winner", () => {
        const generator = gameSaga.attack(0, new Coordinate(1, 2));

        generator.next();
        generator.next({grid: []});

        expect(generator.next().value).toEqual(put({
            type: "FETCH_WINNER"
        }));
    });
});

describe("checkWinner", () => {
    it("calls api to check winner", () => {
        const generator = gameSaga.checkWinner();

        expect(generator.next().value).toEqual(call(GameApi.fetchWinner));
    });

    it("dispatches winner", () => {
        const generator = gameSaga.checkWinner();

        generator.next();

        expect(generator.next(true).value).toEqual(put({
            type: "GAME_WON",
            payload: true
        }));
    });

    it("does nothing if there is no winner", () => {
        const generator = gameSaga.checkWinner();

        generator.next();

        expect(generator.next(false).value).toBeUndefined();
    });
});