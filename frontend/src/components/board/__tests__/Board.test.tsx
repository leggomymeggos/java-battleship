import {shallow} from "enzyme";
import * as React from "react";
import {Board, BoardProps} from "../Board";

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
        const props  = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4]],
        };
        const subject = shallow(<Board {...props} />);

        const tiles = subject.find(".board__tile");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props  = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4], [5, 6]],
        };
        const subject = shallow(<Board {...props} />);

        const rowLabels = subject.find(".board__labels--row");

        expect(rowLabels.text()).toContain("1");
        expect(rowLabels.text()).toContain("2");
        expect(rowLabels.text()).toContain("3");
    });

    it("has coordinate column labels", () => {
        const props  = {
            ...defaultProps,
            coordinates: [[1, 2], [3, 4], [5, 6]],
        };
        const subject = shallow(<Board {...props} />);

        const columnLabels = subject.find(".board__labels--column");

        expect(columnLabels.text()).toContain("A");
        expect(columnLabels.text()).toContain("B");
    });

    it("gets a new board from the backend", () => {
        shallow(<Board {...defaultProps}/>);

        expect(mockActions.getInitialBoard).toHaveBeenCalled();
    });
});
