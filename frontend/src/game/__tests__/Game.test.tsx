import {GameStatus} from "../gameReducer";
import {shallow} from "enzyme";
import * as React from "react";
import {Redirect} from "react-router";
import {Game, GameProps, mapStateToProps} from "../Game";
import TargetBoard from "../../board/OpposingAgentBoard";
import AgentData from "../../agent/OpposingAgentData";
import PlayerBoard from "../../board/UserAgentBoard";

describe("Game", () => {
    let defaultProps: GameProps;

    beforeEach(() => {
        defaultProps = {
            status: GameStatus.NONE
        }
    });

    it("redirects to title page when there is no game", () => {
        const subject = shallow(<Game {...defaultProps} />);

        expect(subject.find(Redirect).prop("to")).toEqual("/");
        expect(subject.find(TargetBoard).exists()).toBeFalsy();
        expect(subject.find(AgentData).exists()).toBeFalsy();
        expect(subject.find(PlayerBoard).exists()).toBeFalsy();
    });

    it("displays game information", () => {
        [GameStatus.IN_PROGRESS, GameStatus.GAME_OVER].forEach((status: GameStatus) => {
            const subject = shallow(<Game {...defaultProps} status={status}/>);

            expect(subject.find(TargetBoard).exists()).toBeTruthy();
            expect(subject.find(AgentData).exists()).toBeTruthy();
            expect(subject.find(PlayerBoard).exists()).toBeTruthy();
        });
    });
});

describe("mapStateToProps", () => {
    it("maps game status", () => {
        const props = mapStateToProps({gameReducer: {status: GameStatus.IN_PROGRESS}});

        expect(props.status).toEqual(GameStatus.IN_PROGRESS)
    });
});
