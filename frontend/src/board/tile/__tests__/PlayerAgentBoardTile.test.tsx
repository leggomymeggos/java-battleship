import {PlayerAgentBoardTile, PlayerBoardTileProps} from "../PlayerAgentBoardTile";
import {shallow} from "enzyme";
import * as React from "react";
import {Tile} from "../../../domain/tile";

describe("PlayerAgentBoardTile", () => {
    let defaultProps: PlayerBoardTileProps;

    beforeEach(() => {
        defaultProps = {
            tile: new Tile(),
            coordinates: {x: 0, y: 0},
        }
    });

    describe("tiles styling", () => {
        it("rotates by -155 if both coordinate are even", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 421,
                    y: 153
                }
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find("._155-reverse").exists()).toBeTruthy();
            expect(subject.find("._62").exists()).toBeFalsy();
        });

        it("rotates by -155 if both coordinate are odd", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 494,
                    y: 1528
                }
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find("._155-reverse").exists()).toBeTruthy();
            expect(subject.find("._62").exists()).toBeFalsy();
        });

        it("rotates by 62 if one coordinate is even and one is odd", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 499,
                    y: 1528
                }
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find("._62").exists()).toBeTruthy();
            expect(subject.find("._155-reverse").exists()).toBeFalsy();
        });

        it("adds 'occupied' if the tile includes a ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile("this is a ship!", false)
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find(".occupied").exists()).toBeTruthy();
        });

        it("does not add 'occupied' if the tile does not include a ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile(null, false)
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find(".occupied").exists()).toBeFalsy();
        });

        it("adds 'occupied--hit' if the ship was hit", () => {
            const props = {
                ...defaultProps,
                tile: new Tile("this is a ship!", true)
            };

            const subject = shallow(<PlayerAgentBoardTile {...props}/>);

            expect(subject.find(".occupied--hit").exists()).toBeTruthy();
            expect(subject.find(".miss").exists()).toBeFalsy();
        });
    });
});