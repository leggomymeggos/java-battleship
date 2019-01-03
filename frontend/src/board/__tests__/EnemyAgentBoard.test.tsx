jest.mock("../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {EnemyAgentBoard, BoardProps, mapStateToProps} from "../EnemyAgentBoard";
import {Tile} from "../../domain/tile";
import {GameState} from "../../game/gameReducer";
import {Agent, AgentState} from "../../domain/agent";

describe("EnemyAgentBoard", () => {
    let defaultProps: BoardProps;

    beforeEach(() => {
        defaultProps = {
            id: 1,
            grid: [[]],
            sunkenShips: [],
            winner: null,
        }
    });

    it("has a grid", () => {
        const subject = shallow(<EnemyAgentBoard {...defaultProps} />);

        expect(subject.find(".target--board__grid").exists()).toBeTruthy();
    });

    it("renders each tile", () => {
        let tile = new Tile();
        const props = {
            ...defaultProps,
            grid: [[tile, tile], [tile, tile]],
        };
        const subject = shallow(<EnemyAgentBoard {...props} />);

        const tiles = subject.find("Connect(EnemyAgentBoardTile)");

        expect(tiles).toHaveLength(4);
    });

    it("has coordinate row labels", () => {
        const props = {
            ...defaultProps,
            grid: [[new Tile(), new Tile()], [new Tile(), new Tile()], [new Tile(), new Tile()]],
        };
        const subject = shallow(<EnemyAgentBoard {...props} />);

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
        const subject = shallow(<EnemyAgentBoard {...props} />);

        const columnLabels = subject.find(".board__label.column").map((item) => {
            return item.text();
        });

        expect(columnLabels).toContain("A");
        expect(columnLabels).toContain("B");
    });
});

describe("mapStateToProps", () => {
    let enemyAgentReducer: AgentState, gameReducer: GameState;
    beforeEach(() => {
        enemyAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: []
        };
        gameReducer = {
            id: 123,
            winner: null
        }
    });

    it("maps grid", () => {
        enemyAgentReducer = {
            id: 0,
            grid: [[new Tile()], [new Tile()]],
            sunkenShips: []
        };
        const props = mapStateToProps({
            enemyAgentReducer,
            gameReducer
        });
        expect(props.grid).toEqual([[new Tile()], [new Tile()]]);
    });

    it("maps sunken ships", () => {
        enemyAgentReducer = {
            ...enemyAgentReducer,
            sunkenShips: ["battleship", "aircraft carrier"]
        };
        const props = mapStateToProps({
            id: 0,
            enemyAgentReducer,
            gameReducer
        });
        expect(props.sunkenShips).toEqual(["battleship", "aircraft carrier"]);
    });

    it("maps winner", () => {
        const props = mapStateToProps({
            enemyAgentReducer,
            gameReducer: {
                ...gameReducer,
                winner: new Agent()
            }
        });
        expect(props.winner).toBeTruthy();
    });
});