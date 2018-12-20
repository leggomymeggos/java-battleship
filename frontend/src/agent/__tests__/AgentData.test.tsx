import {shallow} from "enzyme";
import * as React from "react";

import {AgentData, AgentDataProps, mapStateToProps} from "../AgentData";
import {GameState} from "../../game/gameReducer";
import {Agent} from "../Agent";

describe("Agent Data", () => {
    let props: AgentDataProps;
    beforeEach(() => {
        props = {
            winner: null
        }
    });

    it("asks if the player is ready to play", () => {
        const subject = shallow(<AgentData {...props}/>);

        expect(subject.find(".agent__enemy--dialogue").text()).toContain("Hi! Ready to play?")
    });

    it("has an avatar for the computer", () => {
        const subject = shallow(<AgentData {...props}/>);

        expect(subject.find(".agent__enemy--avatar").exists()).toBeTruthy();

    });

    it("tells the player when they win", () => {
        const subject = shallow(<AgentData {...props} winner={new Agent()}/>);

        expect(subject.find(".agent__enemy--dialogue").text()).toContain("Oh no! You defeated me!")
    });
});

describe("mapStateToProps", () => {
    it("maps winner", () => {
        let gameReducer: GameState = {
            winner: null,
            humanPlayer: null,
            computerPlayer: null,
        };
        let props = mapStateToProps({
            gameReducer
        });
        expect(props.winner).toBeFalsy();

        gameReducer = {
            winner: new Agent(),
            humanPlayer: null,
            computerPlayer: null,
        };
        props = mapStateToProps({
            gameReducer
        });
        expect(props.winner).toBeTruthy();
    });
});