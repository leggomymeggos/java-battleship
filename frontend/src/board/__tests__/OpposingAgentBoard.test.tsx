jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {BoardProps, mapStateToProps, OpposingAgentBoard} from "../OpposingAgentBoard";
import BoardTile, {AgentType} from "../tile/BoardTile";
import {GameState, GameStatus} from "../../game/gameReducer";
import {Tile} from "../../domain/tile";
import {Agent, AgentState} from "../../domain/agent";

describe("OpposingAgentBoard", () => {
    let defaultProps: BoardProps;

    beforeEach(() => {
        defaultProps = {
            gameId: 2,
            grid: [[]],
            attackerId: 0,
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
                attackerId: 789,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            tiles.get(0).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {x: 0, y: 0});
            tiles.get(1).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {x: 1, y: 0});
            tiles.get(2).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {x: 0, y: 1});
            tiles.get(3).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {x: 0, y: 1});
        });

        it("does not call boardHit action if the game is over", () => {
            const props = {
                ...defaultProps,
                gameId: 123,
                winner: new Agent(),
                attackerId: 456,
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
    let opposingAgentReducer: AgentState,
        userAgentReducer: AgentState,
        gameReducer: GameState,
        state: any;

    beforeEach(() => {
        opposingAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: [],
            recentAttackResult: {}
        };
        userAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: [],
            recentAttackResult: {}
        };
        gameReducer = {
            id: 123,
            winner: null,
            status: GameStatus.NONE
        };
        state = {
            gameReducer,
            userAgentReducer,
            opposingAgentReducer
        }
    });

    it("maps grid", () => {
        opposingAgentReducer = {
            id: 0,
            grid: [[new Tile()], [new Tile()]],
            sunkenShips: [],
            recentAttackResult: {}
        };
        const props = mapStateToProps({
            ...state,
            opposingAgentReducer,
        });
        expect(props.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("maps attacker id", () => {
        userAgentReducer = {
            ...userAgentReducer,
            id: 123,
        };
        const props = mapStateToProps({
            ...state,
            userAgentReducer,
        });
        expect(props.attackerId).toEqual(123);
    });

    it("maps winner", () => {
        const props = mapStateToProps({
            ...state,
            gameReducer: {
                ...gameReducer,
                winner: new Agent()
            }
        });
        expect(props.winner).toBeTruthy();
    });
});
