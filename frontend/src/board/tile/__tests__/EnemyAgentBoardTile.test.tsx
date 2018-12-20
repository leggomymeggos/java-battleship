jest.mock("../../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Tile} from "../../../domain/Tile";
import {EnemyAgentBoardTile, BoardTileProps} from "../EnemyAgentBoardTile";

describe("EnemyAgentBoardTile", () => {
    let defaultProps: BoardTileProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            boardHit: jest.fn()
        };

        defaultProps = {
            winner: false,
            tile: new Tile(),
            coordinates: {x:0, y: 0},
            actions: mockActions,
        }
    });

    describe("tiles styling", () => {
        it("rotates by -155 if both coordinates are even", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 421,
                    y: 153
                }
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            expect(subject.find("._155-reverse").exists()).toBeTruthy();
            expect(subject.find("._62").exists()).toBeFalsy();
        });

        it("rotates by -155 if both coordinates are odd", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 494,
                    y: 1528
                }
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

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

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            expect(subject.find("._62").exists()).toBeTruthy();
            expect(subject.find("._155-reverse").exists()).toBeFalsy();
        });

        it("adds 'aimed--miss' if the tile was hit but contained no ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile(null, true)
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            expect(subject.find(".aimed--miss").exists()).toBeTruthy();
            expect(subject.find(".aimed--hit").exists()).toBeFalsy();
        });

        it("adds 'aimed--hit' if the tile was hit and contained a ship", () => {
            const props = {
                ...defaultProps,
                tile: new Tile("123", true)
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            expect(subject.find(".aimed--hit").exists()).toBeTruthy();
            expect(subject.find(".aimed--miss").exists()).toBeFalsy();
        });

        it("does not add anything if the tile was not hit", () => {
            const props = {
                ...defaultProps
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            expect(subject.find(".aimed--hit").exists()).toBeFalsy();
            expect(subject.find(".aimed--miss").exists()).toBeFalsy();
        });
    });

    describe("clicking a tile", () => {
        it("calls 'boardHit' with the coordinates", () => {
            const props = {
                ...defaultProps,
                coordinates: {
                    x: 421,
                    y: 153
                }
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(mockActions.boardHit).toHaveBeenCalledWith({
                x: 421,
                y: 153
            })
        });

        it("does not call 'boardHit' action if the tile has already been hit", () => {
            const props = {
                ...defaultProps,
                tile: new Tile(null, true),
                coordinates: {
                    x: 421,
                    y: 153
                }
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(mockActions.boardHit).not.toHaveBeenCalled()
        });

        it("does not call 'boardHit' action if the game has been won", () => {
            const props = {
                ...defaultProps,
                winner: true,
                tile: new Tile(null, false),
                coordinates: {
                    x: 421,
                    y: 153
                }
            };

            const subject = shallow(<EnemyAgentBoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(mockActions.boardHit).not.toHaveBeenCalled()
        });
    });
});