import {handleActions} from "redux-actions";
import {BoardActions} from "./boardActions";
import {Tile} from "../domain/Tile";

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
    }
}, initialState);

export default boardReducer;
