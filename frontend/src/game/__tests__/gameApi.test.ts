import nock from 'nock';
import {attack, fetchActivePlayer, fetchWinner, newGame} from "../gameApi";
import {Coordinate} from "../../board/boardActions";

describe("gameApi", () => {
    describe("getting a new game", () => {
        it("returns the response data", async () => {
            nock("http://localhost")
                .get("/games/new")
                .reply(200, "game!");

            const response = await newGame();

            expect(response).toEqual("game!");
        });
    });

    describe("hitting a tile", () => {
        it("returns response data", async () => {
            let coordinate = new Coordinate(1, 2);

            nock("http://localhost")
                .put("/games/11/attack?attackerId=2", {...coordinate})
                .reply(200, {id: 123});

            const response = await attack(11, 2, coordinate);

            expect(response).toEqual({id: 123});
        });
    });

    describe("fetching winner", () => {
        it("returns response data", async () => {
            nock("http://localhost")
                .get("/games/1010/winner")
                .reply(200, "true");

            const response = await fetchWinner(1010);

            expect(response).toEqual(true);
        });
    });

    describe("fetching active player", () => {
        it("returns response data", async () => {
            nock("http://localhost")
                .get("/games/3829/players/active")
                .reply(200, "active player!");

            const response = await fetchActivePlayer(3829);

            expect(response).toEqual("active player!");
        });
    });
})