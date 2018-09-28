import {Action, handleActions} from "redux-actions";
import {BoardActions} from "./boardActions";
import {Tile} from "../domain/Tile";
import * as _ from "lodash";
import {GameActions} from "../game/gameActions";

export type BoardState = {
    coordinates: Tile[][];
}

export const initialState: BoardState = {
    coordinates: [[]],
};

const boardReducer = handleActions({
    [GameActions.NEW_GAME_CREATED]: (state: BoardState, action: Action<any>) => {
        return {
            ...state, coordinates: action.payload
        }
    },
    [BoardActions.TILE_HIT]: (state: BoardState, action: Action<any>) => {
        const coordinates = _.cloneDeep(state.coordinates);
        const hitTile = coordinates[action.payload.yCoordinate][action.payload.xCoordinate];
        hitTile.hit = true;
        coordinates[action.payload.yCoordinate][action.payload.xCoordinate] = hitTile;

        return {
            ...state,
            coordinates
        };
    },
}, initialState);

export default boardReducer;
