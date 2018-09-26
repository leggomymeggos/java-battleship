jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Board, BoardProps, mapDispatchToProps, mapStateToProps} from "../Board";
import {getInitialBoard} from "../boardActions";
import {BoardState} from "../boardReducer";
import {Tile} from "../../domain/Tile";

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
            coordinates: [[new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<Board {...props} />);

        const tiles = subject.find(".board__tile");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            coordinates: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
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
            coordinates: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
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

    describe("tiles styling", () => {
        it("adds 'rotated' to every other square", () => {
            const props = {
                ...defaultProps,
                coordinates: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
            };
            const subject = shallow(<Board {...props} />);

            const tiles = subject.find(".board__tile");
            const classNames = tiles.map((item) => item.props().className);

            expect(classNames).toEqual([
                "board__tile rotated", "board__tile",
                "board__tile", "board__tile rotated",
                "board__tile rotated", "board__tile"
            ]);
        });

        it("adds 'aimed--miss' if a tile with no ship was hit", () => {
            let shotAtTile = new Tile();
            shotAtTile.hit = true;
            shotAtTile.shipId = null;

            const props = {
                ...defaultProps,
                coordinates: [[shotAtTile, new Tile()]]
            };
            const subject = shallow(<Board {...props} />);

            const missedTiles = subject.find(".aimed--miss");
            expect(missedTiles.length).toEqual(1);
            const hitTiles = subject.find(".aimed--hit");
            expect(hitTiles.length).toEqual(0);
        });

        it("adds 'aimed--hit' if a tile with a ship was hit", () => {
            let shotAtTile = new Tile();
            shotAtTile.hit = true;
            shotAtTile.shipId = 1;

            const props = {
                ...defaultProps,
                coordinates: [[shotAtTile, new Tile()]]
            };
            const subject = shallow(<Board {...props} />);

            const missedTiles = subject.find(".aimed--miss");
            expect(missedTiles.length).toEqual(0);
            const hitTiles = subject.find(".aimed--hit");

            expect(hitTiles.length).toEqual(1);
        });
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
            coordinates: [[new Tile()], [new Tile()]]
        };
        const props = mapStateToProps({
            boardReducer
        });
        expect(props.coordinates).toEqual([[new Tile()], [new Tile()]]);
    });
});