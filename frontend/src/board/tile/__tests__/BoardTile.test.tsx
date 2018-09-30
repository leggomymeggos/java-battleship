jest.mock("../../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Tile} from "../../../domain/Tile";
import {BoardTile, BoardTileProps} from "../BoardTile";

describe("BoardTile", () => {
    let defaultProps: BoardTileProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            tileHit: jest.fn()
        };

        defaultProps = {
            tile: new Tile(),
            coordinates: {xCoordinate:0, yCoordinate: 0},
            actions: mockActions,
        }
    });

    describe("tiles styling", () => {
        it("rotates by -155 if both coordinates are even", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    xCoordinate: 421,
                    yCoordinate: 153
                }
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find("._155-reverse").exists()).toBeTruthy();
            expect(subject.find("._62").exists()).toBeFalsy();
        });

        it("rotates by -155 if both coordinates are odd", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    xCoordinate: 494,
                    yCoordinate: 1528
                }
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find("._155-reverse").exists()).toBeTruthy();
            expect(subject.find("._62").exists()).toBeFalsy();
        });

        it("rotates by 62 if one coordinate is even and one is odd", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    xCoordinate: 499,
                    yCoordinate: 1528
                }
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find("._62").exists()).toBeTruthy();
            expect(subject.find("._155-reverse").exists()).toBeFalsy();
        });

        it("adds 'aimed--miss' if the tile was hit but contained no ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile(null, true)
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find(".aimed--miss").exists()).toBeTruthy();
            expect(subject.find(".aimed--hit").exists()).toBeFalsy();
        });

        it("adds 'aimed--hit' if the tile was hit and contained a ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile("123", true)
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find(".aimed--hit").exists()).toBeTruthy();
            expect(subject.find(".aimed--miss").exists()).toBeFalsy();
        });

        it("does not add anything if the tile was not hit", () => {
            const props = {
                ...defaultProps
            };

            const subject = shallow(<BoardTile {...props}/>);

            expect(subject.find(".aimed--hit").exists()).toBeFalsy();
            expect(subject.find(".aimed--miss").exists()).toBeFalsy();
        });
    });

    describe("clicking a tile", () => {
        it("calls 'tileHit' with the coordinates", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    xCoordinate: 421,
                    yCoordinate: 153
                }
            };

            const subject = shallow(<BoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(mockActions.tileHit).toHaveBeenCalledWith({
                xCoordinate: 421,
                yCoordinate: 153
            })
        });

        it("does not call 'tileHit' action if the tile has already been hit", () => {
            const props = {
                ...defaultProps,
                tile: new Tile(null, true),
                coordinates: {
                    xCoordinate: 421,
                    yCoordinate: 153
                }
            };

            const subject = shallow(<BoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(mockActions.tileHit).not.toHaveBeenCalled()
        });
    });
});