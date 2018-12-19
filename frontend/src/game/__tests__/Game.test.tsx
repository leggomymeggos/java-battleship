jest.mock("../gameActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Game, GameProps, mapDispatchToProps} from "../Game";
import {createNewGame} from "../gameActions";

describe("Game", () => {
    let defaultProps: GameProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            createNewGame: jest.fn()
        };
        defaultProps = {
            actions: mockActions
        }
    });

    it("gets a new board from the backend", () => {
        shallow(<Game {...defaultProps}/>);

        expect(mockActions.createNewGame).toHaveBeenCalled();
    });
});

describe("mapDispatchToProps", () => {
    it("maps createNewGame action", () => {
        const dispatch = jest.fn();
        const props = mapDispatchToProps(dispatch);

        props.actions.createNewGame();

        expect(createNewGame).toHaveBeenCalled()
    });
});

