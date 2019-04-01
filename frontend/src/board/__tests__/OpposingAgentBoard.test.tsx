jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {OpposingAgentBoard, BoardProps, mapStateToProps} from "../OpposingAgentBoard";
import BoardTile, {AgentType} from "../tile/BoardTile";
import {GameState, GameStatus} from "../../game/gameReducer";
import {Tile} from "../../domain/tile";
import {Agent, AgentState} from "../../domain/agent";

describe("OpposingAgentBoard", () => {
    let defaultProps: BoardProps;

    beforeEach(() => {
        defaultProps = {
            id: 1,
            gameId: 2,
            grid: [[]],
            sunkenShips: [],
            winner: null,
            actions: {
                boardHit: jest.fn()
            }
        }
    });

    it("has a grid", () => {
        const subject = shallow(<OpposingAgentBoard {...defaultProps} />);

        expect(subject.find(".target--board__grid").exists()).toBeTruthy();
    });

    describe("tiles", () => {
        it("renders each tile", () => {
            let tile = new Tile();
            const props = {
                ...defaultProps,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles).toHaveLength(4);
        });

        it("passes coordinates to BoardTile", () => {
            let tile = new Tile();
            const props = {
                ...defaultProps,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.coordinate).toEqual({x: 0, y: 0});
            expect(tiles.get(1).props.coordinate).toEqual({x: 1, y: 0});
            expect(tiles.get(2).props.coordinate).toEqual({x: 0, y: 1});
            expect(tiles.get(3).props.coordinate).toEqual({x: 1, y: 1});
        });

        it("passes agent type to BoardTile", () => {
            const props = {
                ...defaultProps,
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.agentType).toEqual(AgentType.OPPONENT)
        });

        it("sets gameOver to true if there is a winner", () => {
            const props = {
                ...defaultProps,
                winner: new Agent(),
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeTruthy();
        });

        it("sets gameOver to false if there is not a winner", () => {
            const props: BoardProps = {
                ...defaultProps,
                winner: null,
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeFalsy();
        });

        it("calls boardHit action on tile clicked", () => {
            let tile = new Tile();
            const props = {
                ...defaultProps,
                gameId: 123,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            tiles.get(0).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, {x: 0, y: 0});
            tiles.get(1).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, {x: 1, y: 0});
            tiles.get(2).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, {x: 0, y: 1});
            tiles.get(3).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, {x: 0, y: 1});
        });

        it("does not call boardHit action if the game is over", () => {
            const props = {
                ...defaultProps,
                gameId: 123,
                winner: new Agent(),
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            tiles.get(0).props.tileClicked();
            expect(props.actions.boardHit).not.toBeCalled();
        });
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<OpposingAgentBoard {...props} />);

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
        const subject = shallow(<OpposingAgentBoard {...props} />);

        const columnLabels = subject.find(".board__label.column").map((item) => {
            return item.text();
        });

        expect(columnLabels).toContain("A");
        expect(columnLabels).toContain("B");
    });
});

describe("mapStateToProps", () => {
    let opposingAgentReducer: AgentState, gameReducer: GameState;
    beforeEach(() => {
        opposingAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: []
        };
        gameReducer = {
            id: 123,
            winner: null,
            status: GameStatus.NONE
        }
    });

    it("maps grid", () => {
        opposingAgentReducer = {
            id: 0,
            grid: [[new Tile()], [new Tile()]],
            sunkenShips: []
        };
        const props = mapStateToProps({
            opposingAgentReducer,
            gameReducer
        });
        expect(props.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("maps sunken ships", () => {
        opposingAgentReducer = {
            ...opposingAgentReducer,
            sunkenShips: ["battleship", "aircraft carrier"]
        };
        const props = mapStateToProps({
            id: 0,
            opposingAgentReducer,
            gameReducer
        });
        expect(props.sunkenShips).toEqual(["battleship", "aircraft carrier"]);
    });

    it("maps winner", () => {
        const props = mapStateToProps({
            opposingAgentReducer,
            gameReducer: {
                ...gameReducer,
                winner: new Agent()
            }
        });
        expect(props.winner).toBeTruthy();
    });
});
