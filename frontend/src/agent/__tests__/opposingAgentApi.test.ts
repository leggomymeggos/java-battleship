import MockAdapter from 'axios-mock-adapter';
import axios from 'axios';
import {OpposingAgentApi} from "../opposingAgentApi";

describe("opposingAgentApi", () => {
    let mock: MockAdapter;

    beforeEach(() => {
        mock = new MockAdapter(axios);
    });

    describe("attacking the board", () => {
        it("returns response data", async () => {
            mock.onGet("http://example.com/games/123/opponent-attack?attackerId=456")
                .reply(200, "new board!");

            const response = await OpposingAgentApi.attack(123, 456);

            expect(response).toEqual("new board!");
        });
    });
});