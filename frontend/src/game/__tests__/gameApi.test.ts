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
            mock.onGet("http://localhost:8081/games/new")
                .reply(200,  "game!");

            const response = await GameApi.newGame();

            expect(response).toEqual("game!");
        });
    });

    describe("hitting a tile", () => {
        it("returns response data", async () => {
            let coordinate = new Coordinate(1, 2);

            mock.onPut("http://localhost:8081/games/0/players/0/hit", coordinate)
                .reply(200,  "game!");

            const response = await GameApi.hitBoard(coordinate);

            expect(response).toEqual("game!");
        });
    });

    describe("fetching winner", () => {
        it("returns response data", async () => {
            mock.onGet("http://localhost:8081/games/0/winner")
                .reply(200, true);

            const response = await GameApi.fetchWinner();

            expect(response).toEqual(true);
        });
    });
});