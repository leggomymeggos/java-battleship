import {handleActions} from "redux-actions";
import {BoardActions} from "./boardActions";

export type BoardState = {
    coordinates: any[][];
}

export const initialState: BoardState = {
    coordinates: [[]],
};

const boardReducer = handleActions({
   [BoardActions.GET_INITIAL_BOARD]: (state: BoardState) => {
       return {...state, coordinates: [[1, 2, 3], [1, 2, 3], [1, 2, 3]]}
   }
}, initialState);

export default boardReducer;
