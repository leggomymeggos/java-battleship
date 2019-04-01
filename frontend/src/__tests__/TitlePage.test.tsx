jest.mock("../game/gameActions");

import {shallow} from "enzyme";
import * as React from "react";
import {createNewGame} from "../game/gameActions";
import {TitlePage, mapDispatchToProps, TitlePageProps} from "../TitlePage";

describe("TitlePage", () => {
    let defaultProps: TitlePageProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            createNewGame: jest.fn()
        };
        defaultProps = {
            actions: mockActions
        }
    });

    it("has a title", () => {
        const subject = shallow(<TitlePage {...defaultProps} />);

        expect(subject.find('.title-page__title').text()).toEqual("Battleship")
    });

    it("has a button for a new game", () => {
        const subject = shallow(<TitlePage {...defaultProps} />);

        expect(subject.find('.title-page__new-game').text()).toEqual("New Game")
    });

    it("creates a new game when the 'new game' button is clicked", () => {
        const subject = shallow(<TitlePage {...defaultProps} />);

        subject.find('.title-page__new-game').simulate('click');

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
