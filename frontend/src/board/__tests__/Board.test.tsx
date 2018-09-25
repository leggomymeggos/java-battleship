jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Board, BoardProps, mapDispatchToProps, mapStateToProps} from "../Board";
import {getInitialBoard} from "../boardActions";
import {BoardState} from "../boardReducer";

describe("Board", () => {
    let defaultProps: BoardProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            getInitialBoard: jest.fn()
        };
        defaultProps = {
            coordinates: [[]],
            actions: mockActions,
        }
    });

    it("has a grid", () => {
        const subject = shallow(<Board {...defaultProps} />);

        expect(subject.find(".board__grid").exists()).toBeTruthy();
    });

    it("has a tile for each coordinate", () => {
        const props = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4]],
        };
        const subject = shallow(<Board {...props} />);

        const tiles = subject.find(".board__tile");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4], [5, 6]],
        };
        const subject = shallow(<Board {...props} />);

        const rowLabels = subject.find(".board__label--row").map((item) => {
            return item.text();
        });

        expect(rowLabels).toContain("1");
        expect(rowLabels).toContain("2");
        expect(rowLabels).toContain("3");
    });

    it("has coordinate column labels", () => {
        const props = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4], [5, 6]],
        };
        const subject = shallow(<Board {...props} />);

        const columnLabels = subject.find(".board__label--column").map((item) => {
            return item.text();
        });

        expect(columnLabels).toContain("A");
        expect(columnLabels).toContain("B");
    });

    it("gets a new board from the backend", () => {
        shallow(<Board {...defaultProps}/>);

        expect(mockActions.getInitialBoard).toHaveBeenCalled();
    });

    it("adds 'rotated' to every other square", () => {
        const props = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4], [5, 6]],
        };
        const subject = shallow(<Board {...props} />);

        const tiles = subject.find(".board__tile");
        const classNames = tiles.map((item) => item.props().className);

        expect(classNames).toEqual([
            "board__tile", "board__tile rotated",
            "board__tile rotated", "board__tile",
            "board__tile", "board__tile rotated"
        ]);
    });
});

describe("mapDispatchToProps", () => {
    it("maps getInitialBoard action", () => {
        const dispatch = jest.fn();
        const props = mapDispatchToProps(dispatch);

        props.actions.getInitialBoard();

        expect(getInitialBoard).toHaveBeenCalled()
    });
});

describe("mapStateToProps", () => {
    it("maps coordinates", () => {
        const boardReducer: BoardState = {
            coordinates: [[1], [2]]
        };
        const props = mapStateToProps({
            boardReducer
        });
        expect(props.coordinates).toEqual([[1], [2]]);
    });
});