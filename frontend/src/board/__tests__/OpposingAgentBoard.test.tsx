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
            winnerId: null,
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

            expect(tiles.get(0).props.coordinate).toEqual({column: 0, row: 0});
            expect(tiles.get(1).props.coordinate).toEqual({column: 1, row: 0});
            expect(tiles.get(2).props.coordinate).toEqual({column: 0, row: 1});
            expect(tiles.get(3).props.coordinate).toEqual({column: 1, row: 1});
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
                winnerId: 123,
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeTruthy();
        });

        it("sets gameOver to false if the winnerId is null", () => {
            const props: BoardProps = {
                ...defaultProps,
                winnerId: null,
                grid: [[new Tile()]],
            };
            const subject = shallow(<OpposingAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeFalsy();
        });

        it("sets gameOver to false if the winnerId is -1", () => {
            const props: BoardProps = {
                ...defaultProps,
                winnerId: -1,
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
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {column: 0, row: 0});
            tiles.get(1).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {column: 1, row: 0});
            tiles.get(2).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {column: 0, row: 1});
            tiles.get(3).props.tileClicked();
            expect(props.actions.boardHit).toBeCalledWith(123, 789, {column: 0, row: 1});
        });

        it("does not call boardHit action if the game is over", () => {
            const props = {
                ...defaultProps,
                gameId: 123,
                winnerId: 123,
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

        const rowLabels = subject.find({className: 'row'});

        expect(rowLabels.get(0).props.value).toEqual(1);
        expect(rowLabels.get(1).props.value).toEqual(2);
        expect(rowLabels.get(2).props.value).toEqual(3);
    });

    it("has coordinate column labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<OpposingAgentBoard {...props} />);

        const columnLabels = subject.find({className: 'column'});

        expect(columnLabels.get(0).props.value).toEqual('A');
        expect(columnLabels.get(1).props.value).toEqual('B');
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
            winnerId: null,
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
                winnerId: 123
            }
        });
        expect(props.winnerId).toBeTruthy();
    });
});
