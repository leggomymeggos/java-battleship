jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {TargetBoard, BoardProps, mapStateToProps} from "../TargetBoard";
import {BoardState} from "../targetBoardReducer";
import {Tile} from "../../domain/Tile";
import {GameState} from "../../game/gameReducer";
import {Player} from "../../agent/Player";

describe("TargetBoard", () => {
    let defaultProps: BoardProps;

    beforeEach(() => {
        defaultProps = {
            grid: [[]],
            sunkenShips: [],
            winner: null,
        }
    });

    it("has a grid", () => {
        const subject = shallow(<TargetBoard {...defaultProps} />);

        expect(subject.find(".board__grid").exists()).toBeTruthy();
    });

    it("renders each tile", () => {
        let tile = new Tile();
        const props = {
            ...defaultProps,
            grid: [[tile, tile], [tile, tile]],
        };
        const subject = shallow(<TargetBoard {...props} />);

        const tiles = subject.find("Connect(TargetBoardTile)");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<TargetBoard {...props} />);

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
        const subject = shallow(<TargetBoard {...props} />);

        const columnLabels = subject.find(".board__label.column").map((item) => {
            return item.text();
        });

        expect(columnLabels).toContain("A");
        expect(columnLabels).toContain("B");
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