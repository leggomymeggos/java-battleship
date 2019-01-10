import MockAdapter from 'axios-mock-adapter';
import axios from 'axios';
import {EnemyAgentApi} from "../enemyAgentApi";

describe("enemyAgentApi", () => {
    let mock: MockAdapter;

    beforeEach(() => {
        mock = new MockAdapter(axios);
    });

    describe("attacking the board", () => {
        it("returns response data", async () => {
            mock.onGet("http://example.com/games/123/enemy-attack?attackerId=456")
                .reply(200, "new board!");

            const response = await EnemyAgentApi.attack(123, 456);

            expect(response).toEqual("new board!");
        });
    });
});