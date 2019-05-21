import {shallow} from "enzyme";
import * as React from "react";

import {AgentDataProps, mapStateToProps, OpposingAgentData} from "../OpposingAgentData";
import {GameState, GameStatus} from "../../game/gameReducer";
import {Agent, AgentState} from "../../domain/agent";

describe("Agent Data", () => {
    let props: AgentDataProps;
    beforeEach(() => {
        props = {
            opposingAgentId: 0,
            winner: null,
            attackResult: {}
        }
    });

    it("asks if the player is ready to play", () => {
        const subject = shallow(<OpposingAgentData {...props}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toContain("Hi! Ready to play?")
    });

    it("has an avatar for the computer", () => {
        const subject = shallow(<OpposingAgentData {...props}/>);

        expect(subject.find(".agent__opponent--avatar").exists()).toBeTruthy();

    });

    it("tells the user when they win", () => {
        const subject = shallow(<OpposingAgentData {...props} winner={new Agent(123)} opposingAgentId={456}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toContain("Oh no! You defeated me!")
    });

    it("taunts the user when the computer wins", () => {
        const subject = shallow(<OpposingAgentData {...props} winner={new Agent(12)} opposingAgentId={12}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toContain("Hahahaha!!! I have defeated you!!!")
    });

    it("taunts the user when the computer sinks a ship", () => {
        const subject = shallow(<OpposingAgentData {...props}
                                                   attackResult={{hitType: 'SUNK', shipName: 'BATTLESHIP'}}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toEqual("Hahahaha! I sunk your Battleship!")
    });

    it("taunts the user when the computer hits a ship", () => {
        const subject = shallow(<OpposingAgentData {...props} attackResult={{hitType: 'HIT'}}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toEqual("Yes! A hit!")
    });

    it("says nothing when the computer misses", () => {
        const subject = shallow(<OpposingAgentData {...props} attackResult={{hitType: 'MISS'}}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toEqual("...")
    });
});

describe("mapStateToProps", () => {
    let opposingAgentReducer: AgentState;
    let gameReducer: GameState;

    beforeEach(() => {
        opposingAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: [],
            recentAttackResult: {}
        };
        gameReducer = {
            id: 0,
            winner: null,
            status: GameStatus.NONE
        };
    });

    it("maps winner", () => {
        let props = mapStateToProps({
            gameReducer: {
                ...gameReducer,
                winner: null
            },
            opposingAgentReducer
        });
        expect(props.winner).toBeNull();

        let agent = new Agent(2990);
        props = mapStateToProps({
            gameReducer: {
                ...gameReducer,
                winner: agent
            },
            opposingAgentReducer
        });
        expect(props.winner).toEqual(agent);
    });

    it("maps opposingAgentId", () => {
        let props = mapStateToProps({
            gameReducer,
            opposingAgentReducer: {
                ...opposingAgentReducer,
                id: 123
            }
        });

        expect(props.opposingAgentId).toEqual(123);
    });

    it("maps recentAttackResult", () => {
        let props = mapStateToProps({
            gameReducer,
            opposingAgentReducer: {
                ...opposingAgentReducer,
                recentAttackResult: {
                    shipName: 'BATTLESHIP',
                    hitType: 'MISS'
                }
            }
        });

        expect(props.attackResult).toEqual({
            shipName: 'BATTLESHIP',
            hitType: 'MISS'
        });
    });
});