jest.mock("../gameApi");

import {Tile} from "../../domain/Tile";
import * as gameSaga from "../gameSaga";
import {GameApi} from "../gameApi";
import {call, put} from "redux-saga/effects";

describe("gameSaga test", () => {
    it("calls api to get game", () => {
        const generator = gameSaga.fetchGame();

        expect(generator.next().value).toEqual(call(GameApi.newGame));
    });

    it("triggers action to set board", () => {
        const generator = gameSaga.fetchGame();

        generator.next();

        expect(generator.next({player: {board: {grid: [[new Tile()]]}}}).value).toEqual(put({type: "NEW_GAME_CREATED", payload: {grid: [[new Tile()]]}}));
    });
});