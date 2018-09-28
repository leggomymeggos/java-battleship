import {GameActions, newGameCreated} from "../gameActions";
import {Tile} from "../../domain/Tile";

describe("newGameCreated", () => {
    it("returns NEW_GAME_CREATED action", () => {
        expect(newGameCreated([[new Tile()]])).toEqual({
            type: GameActions.NEW_GAME_CREATED,
            payload: [[new Tile()]]
        });
    });
});
