jest.mock("../boardActions");

import {BoardProps, mapStateToProps, UserAgentBoard} from "../UserAgentBoard";
import {shallow} from "enzyme";
import * as React from "react";
import BoardTile, {AgentType} from "../tile/BoardTile";
import {GameState, GameStatus} from "../../game/gameReducer";
import {Tile} from "../../domain/tile";
import {Agent, AgentState} from "../../domain/agent";

describe("UserAgentBoard", () => {
    let defaultProps: BoardProps;

    beforeEach(() => {
        defaultProps = {
            id: 1,
            gameId: 2,
            grid: [[]],
            sunkenShips: [],
            winner: null
        }
    });

    it("has a grid", () => {
        const subject = shallow(<UserAgentBoard {...defaultProps} />);

        expect(subject.find(".user--board__grid").exists()).toBeTruthy();
    });

    describe("tiles", () => {
        it("renders each tile", () => {
            let tile = new Tile();
            const props = {
                ...defaultProps,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<UserAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles).toHaveLength(4);
        });

        it("passes coordinates to BoardTile", () => {
            let tile = new Tile();
            const props = {
                ...defaultProps,
                grid: [[tile, tile], [tile, tile]],
            };
            const subject = shallow(<UserAgentBoard {...props} />);

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
            const subject = shallow(<UserAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.agentType).toEqual(AgentType.USER)
        });

        it("sets gameOver to true if there is a winner", () => {
            const props = {
                ...defaultProps,
                winner: new Agent(),
                grid: [[new Tile()]],
            };
            const subject = shallow(<UserAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeTruthy();
        });

        it("sets gameOver to false if there is not a winner", () => {
            const props: BoardProps = {
                ...defaultProps,
                winner: null,
                grid: [[new Tile()]],
            };
            const subject = shallow(<UserAgentBoard {...props} />);

            const tiles = subject.find("BoardTile");

            expect(tiles.get(0).props.gameOver).toBeFalsy();
        });
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<UserAgentBoard {...props} />);

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
        const subject = shallow(<UserAgentBoard {...props} />);

        const columnLabels = subject.find({className: 'column'});

        expect(columnLabels.get(0).props.value).toEqual('A');
        expect(columnLabels.get(1).props.value).toEqual('B');
    });
});

describe("mapStateToProps", () => {
    let userAgentReducer: AgentState, gameReducer: GameState;
    beforeEach(() => {
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
        }
    });

    it("maps grid", () => {
        userAgentReducer = {
            id: 0,
            grid: [[new Tile()], [new Tile()]],
            sunkenShips: [],
            recentAttackResult: {}
        };
        const props = mapStateToProps({
            userAgentReducer,
            gameReducer
        });
        expect(props.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("maps sunken ships", () => {
        userAgentReducer = {
            ...userAgentReducer,
            sunkenShips: ["battleship", "aircraft carrier"]
        };
        const props = mapStateToProps({
            id: 0,
            userAgentReducer,
            gameReducer
        });
        expect(props.sunkenShips).toEqual(["battleship", "aircraft carrier"]);
    });

    it("maps winner", () => {
        const props = mapStateToProps({
            userAgentReducer,
            gameReducer: {
                ...gameReducer,
                winner: new Agent()
            }
        });
        expect(props.winner).toBeTruthy();
    });
});
