import MockAdapter from 'axios-mock-adapter';
import axios from 'axios';
import {GameApi} from "../gameApi";

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
});