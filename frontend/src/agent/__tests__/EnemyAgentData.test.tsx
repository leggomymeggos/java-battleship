import {shallow} from "enzyme";
import * as React from "react";

import {EnemyAgentData, AgentDataProps, mapStateToProps} from "../EnemyAgentData";
import {GameState} from "../../game/gameReducer";
import {Agent, AgentState} from "../../domain/agent";

describe("Agent Data", () => {
    let props: AgentDataProps;
    beforeEach(() => {
        props = {
            enemyAgentId: 0,
            winner: null
        }
    });

    it("asks if the player is ready to play", () => {
        const subject = shallow(<EnemyAgentData {...props}/>);

        expect(subject.find(".agent__enemy--dialogue").text()).toContain("Hi! Ready to play?")
    });

    it("has an avatar for the computer", () => {
        const subject = shallow(<EnemyAgentData {...props}/>);

        expect(subject.find(".agent__enemy--avatar").exists()).toBeTruthy();

    });

    it("tells the user when they win", () => {
        const subject = shallow(<EnemyAgentData {...props} winner={new Agent(123)} enemyAgentId={456}/>);

        expect(subject.find(".agent__enemy--dialogue").text()).toContain("Oh no! You defeated me!")
    });

    it("taunts the user when the computer win", () => {
        const subject = shallow(<EnemyAgentData {...props} winner={new Agent(12)} enemyAgentId={12}/>);

        expect(subject.find(".agent__enemy--dialogue").text()).toContain("Hahahaha!!! I have defeated you!!!")
    });
});

describe("mapStateToProps", () => {
    let enemyAgentReducer: AgentState;
    let gameReducer: GameState;

    beforeEach(() => {
        enemyAgentReducer = {
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
            enemyAgentReducer
        });
        expect(props.winner).toBeNull();

        let agent = new Agent(2990);
        props = mapStateToProps({
            gameReducer: {
                ...gameReducer,
                winner: agent
            },
            enemyAgentReducer
        });
        expect(props.winner).toEqual(agent);
    });

    it("maps enemyAgentId", () => {
        let props = mapStateToProps({
            gameReducer,
            enemyAgentReducer: {
                ...enemyAgentReducer,
                id: 123
            }
        });

        expect(props.enemyAgentId).toEqual(123);
    });
});