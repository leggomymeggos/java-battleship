jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Board, BoardProps, mapDispatchToProps, mapStateToProps} from "../Board";
import {getInitialBoard} from "../boardActions";
import {BoardState} from "../boardReducer";
import {Tile} from "../../domain/Tile";
import {GameState} from "../../game/gameReducer";
import {Player} from "../../agent/Player";

describe("Board", () => {
    let defaultProps: BoardProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            getInitialBoard: jest.fn()
        };
        defaultProps = {
            grid: [[]],
            sunkenShips: [],
            winner: null,
            actions: mockActions,
        }
    });

    it("has a grid", () => {
        const subject = shallow(<Board {...defaultProps} />);

        expect(subject.find(".board__grid").exists()).toBeTruthy();
    });

    it("renders each tile", () => {
        let tile = new Tile();
        const props = {
            ...defaultProps,
            grid: [[tile, tile], [tile, tile]],
        };
        const subject = shallow(<Board {...props} />);

        const tiles = subject.find("Connect(BoardTile)");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<Board {...props} />);

        const rowLabels = subject.find(".board__label.row").map((item) => {
            return item.text();
        });

        expect(rowLabels).toContain("1");
        expect(rowLabels).toContain("2");
        expect(rowLabels).toContain("3");
    });

    it("has coordinate column labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<Board {...props} />);

        const columnLabels = subject.find(".board__label.column").map((item) => {
            return item.text();
        });

        expect(columnLabels).toContain("A");
        expect(columnLabels).toContain("B");
    });

    it("gets a new board from the backend", () => {
        shallow(<Board {...defaultProps}/>);

        expect(mockActions.getInitialBoard).toHaveBeenCalled();
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
    let boardReducer: BoardState, gameReducer: GameState;
    beforeEach(() => {
        boardReducer = {
            grid: [],
            sunkenShips: []
        };
        gameReducer = {
            winner: null,
            humanPlayer: null,
            computerPlayer: null
        }
    });

    it("maps grid", () => {
        boardReducer = {
            grid: [[new Tile()], [new Tile()]],
            sunkenShips: []
        };
        const props = mapStateToProps({
            boardReducer,
            gameReducer
        });
        expect(props.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("maps sunken ships", () => {
        boardReducer = {
            ...boardReducer,
            sunkenShips: ["battleship", "aircraft carrier"]
        };
        const props = mapStateToProps({
            boardReducer,
            gameReducer
        });
        expect(props.sunkenShips).toEqual(["battleship", "aircraft carrier"]);
    });

    it("maps winner", () => {
        const props = mapStateToProps({
            boardReducer,
            gameReducer: {
                ...gameReducer,
                winner: new Player()
            }
        });
        expect(props.winner).toBeTruthy();
    });
});