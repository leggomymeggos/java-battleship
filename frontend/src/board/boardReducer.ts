import {Action, handleActions} from "redux-actions";
import {BoardActions, Coordinates} from "./boardActions";
import {Tile} from "../domain/Tile";
import * as _ from "lodash";

export type BoardState = {
    coordinates: Tile[][];
}

export const initialState: BoardState = {
    coordinates: [[]],
};

const boardReducer = handleActions({
    [BoardActions.GET_INITIAL_BOARD]: (state: BoardState) => {
        return {
            ...state, coordinates: [
                [new Tile(1), new Tile(), new Tile(), new Tile(2), new Tile(2), new Tile(2), new Tile(2), new Tile(), new Tile(), new Tile()],
                [new Tile(1), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(1), new Tile(), new Tile(3), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(), new Tile(), new Tile(4), new Tile(4), new Tile(4), new Tile(), new Tile(), new Tile(), new Tile(), new Tile()],
                [new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(), new Tile(5), new Tile(5)],
            ]
        }
    },
    [BoardActions.TILE_HIT]: (state: BoardState, action: Action<Coordinates>) => {
        const coordinates = _.cloneDeep(state.coordinates);
        const hitTile = coordinates[action.payload.yCoordinate][action.payload.xCoordinate];
        hitTile.hit = true;
        coordinates[action.payload.yCoordinate][action.payload.xCoordinate] = hitTile;

        return {
            ...state,
            coordinates
        };
    }
}, initialState);

export default boardReducer;
