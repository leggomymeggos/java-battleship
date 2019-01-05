import MockAdapter from 'axios-mock-adapter';
import axios from 'axios';
import {GameApi} from "../gameApi";
import {Coordinate} from "../../board/boardActions";

describe("gameApi", () => {
    let mock: MockAdapter;
    beforeEach(() => {
        mock = new MockAdapter(axios);
    });

    describe("getting a new game", () => {
        it("returns the response data", async () => {
            mock.onGet("http://example.com/games/new")
                .reply(200,  "game!");

            const response = await GameApi.newGame();

            expect(response).toEqual("game!");
        });
    });

    describe("hitting a tile", () => {
        it("returns response data", async () => {
            let coordinate = new Coordinate(1, 2);

            mock.onPut("http://example.com/games/11/attack?attackerId=2", coordinate)
                .reply(200,  "game!");

            const response = await GameApi.attack(11, 2, coordinate);

            expect(response).toEqual("game!");
        });
    });

    describe("fetching winner", () => {
        it("returns response data", async () => {
            mock.onGet("http://example.com/games/1010/winner")
                .reply(200, true);

            const response = await GameApi.fetchWinner(1010);

            expect(response).toEqual(true);
        });
    });

    describe("fetching active player", () => {
        it("returns response data", async () => {
            mock.onGet("http://example.com/games/3829/players/active")
                .reply(200, "active player!");

            const response = await GameApi.fetchActivePlayer(3829);

            expect(response).toEqual("active player!");
        });
    });
});