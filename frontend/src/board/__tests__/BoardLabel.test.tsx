import {shallow} from "enzyme";
import * as React from "react";
import {BoardLabel} from "../BoardLabel";

describe("BoardLabel", () => {
    it("renders the className", () => {
        let subject = shallow(<BoardLabel className={'row'} value={''}/>);
        expect(subject.find('.board__label').hasClass('row')).toBeTruthy();
        expect(subject.find('.board__label').hasClass('column')).toBeFalsy();

        subject = shallow(<BoardLabel className={'column'} value={''}/>);
        expect(subject.find('.board__label').hasClass('row')).toBeFalsy();
        expect(subject.find('.board__label').hasClass('column')).toBeTruthy();
    });

    it("renders the value", () => {
        let subject = shallow(<BoardLabel className={'column'} value={'ABC'}/>);
        expect(subject.find('.board__label--value').text()).toContain('ABC');

        subject = shallow(<BoardLabel className={'column'} value={123}/>);
        expect(subject.find('.board__label--value').text()).toContain(123);
    });
});
