import {createAction} from "redux-actions";

export const BoardActions = {
    GET_INITIAL_BOARD: "GET_INITIAL_BOARD"
};

export const getInitialBoard =
    createAction(BoardActions.GET_INITIAL_BOARD);

export default {
    getInitialBoard
}