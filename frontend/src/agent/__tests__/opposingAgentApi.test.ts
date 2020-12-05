import nock from 'nock';
import {attack} from "../opposingAgentApi";

describe("opposingAgentApi", () => {
    describe("attacking the board", () => {
        it("returns response data", async () => {
            nock("http://localhost")
                .get("/games/123/opponent-attack?attackerId=456")
                .reply(200, "new board!");

            const response = await attack(123, 456);

            expect(response).toEqual("new board!");
        });
    });
});