import {shallow} from "enzyme";
import * as React from "react";

import {OpposingAgentData, AgentDataProps, mapStateToProps} from "../OpposingAgentData";
import {GameState} from "../../game/gameReducer";
import {Agent, AgentState} from "../../domain/agent";

describe("Agent Data", () => {
    let props: AgentDataProps;
    beforeEach(() => {
        props = {
            opposingAgentId: 0,
            winner: null
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

    it("taunts the user when the computer win", () => {
        const subject = shallow(<OpposingAgentData {...props} winner={new Agent(12)} opposingAgentId={12}/>);

        expect(subject.find(".agent__opponent--dialogue").text()).toContain("Hahahaha!!! I have defeated you!!!")
    });
});

describe("mapStateToProps", () => {
    let opposingAgentReducer: AgentState;
    let gameReducer: GameState;

    beforeEach(() => {
        opposingAgentReducer = {
            id: 0,
            grid: [],
            sunkenShips: []
        };
        gameReducer = {
            id: 0,
            winner: null,
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
});