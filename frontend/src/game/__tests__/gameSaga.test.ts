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

        expect(generator.next({player: {board: {grid: [[new Tile()]]}}}).value).toEqual(put({
            type: "NEW_GAME_CREATED",
            payload: {grid: [[new Tile()]]}
        }));
    });
});

describe("hitBoard", () => {
    it("calls api to hit board", () => {
        let coordinate = new Coordinate(0, 0);
        const generator = gameSaga.hitBoard(coordinate);

        expect(generator.next().value).toEqual(call(GameApi.hitBoard, coordinate));
    });

    it("triggers action to update board", () => {
        const generator = gameSaga.hitBoard(new Coordinate(1, 2));

        generator.next();

        expect(generator.next({grid: [[new Tile()]]}).value).toEqual(put({
            type: "BOARD_HIT_SUCCESS",
            payload: [[new Tile()]]
        }));
    });
});