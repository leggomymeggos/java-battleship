jest.mock("../../boardActions");

import {shallow} from "enzyme";
import * as React from "react";
import {Tile} from "../../../domain/tile";
import BoardTile, {AgentType, BoardTileProps} from "../BoardTile";

describe("BoardTile", () => {
    let defaultProps: BoardTileProps;
    let mockActions: any;

    beforeEach(() => {
        mockActions = {
            boardHit: jest.fn()
        };

        defaultProps = {
            tile: new Tile(),
            coordinate: {x: 0, y: 0},
            agentType: AgentType.NONE,
            gameOver: false,
            tileClicked: () => {
            }
        }
    });

    describe("tiles styling", () => {
        describe("ocean", () => {
            it("rotates by -155 if both coordinates are odd", () => {
                const props = {
                    ...defaultProps,
                    coordinate: {
                        x: 421,
                        y: 153
                    }
                };

                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find("._155-reverse").exists()).toBeTruthy();
                expect(subject.find("._62").exists()).toBeFalsy();
            });

            it("rotates by -155 if both coordinates are even", () => {
                const props = {
                    ...defaultProps,
                    coordinate: {
                        x: 494,
                        y: 1528
                    }
                };

                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find("._155-reverse").exists()).toBeTruthy();
                expect(subject.find("._62").exists()).toBeFalsy();
            });

            it("rotates by 62 if one coordinate is even and one is odd", () => {
                const props = {
                    ...defaultProps,
                    coordinate: {
                        x: 499,
                        y: 1528
                    }
                };

                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find("._62").exists()).toBeTruthy();
                expect(subject.find("._155-reverse").exists()).toBeFalsy();
            });
        });

        describe("hit indicator", () => {
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

        describe("user agent specific styling", () => {
            let props: BoardTileProps;
            beforeEach(() => {
                props = {
                    ...defaultProps,
                    agentType: AgentType.USER
                };
            });

            it("adds 'occupied' and no ocean if the tile has a ship", () => {
                props = {
                    ...props,
                    tile: new Tile("totally a ship here", false)
                };
                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find(".occupied").exists()).toBeTruthy();
                expect(subject.find(".rotated").exists()).toBeFalsy();
            });

            it("adds ocean if there is not a ship", () => {
                props = {
                    ...props,
                    tile: new Tile(null, false)
                };
                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find(".occupied").exists()).toBeFalsy();
                expect(subject.find(".rotated").exists()).toBeTruthy();
            });
        });

        describe("enemy agent specific styling", () => {
            let props: BoardTileProps;
            beforeEach(() => {
                props = {
                    ...defaultProps,
                    agentType: AgentType.ENEMY
                };
            });

            it("adds 'clicked' if the tile has been hit", () => {
                props = {
                    ...props,
                    tile: new Tile(null, true)
                };
                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find(".clicked").exists()).toBeTruthy();
            });

            it("adds 'clicked' if the game is over", () => {
                props = {
                    ...props,
                    gameOver: true,
                };
                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find(".clicked").exists()).toBeTruthy();
            });

            it("does not add 'clicked' if the game is not over and the tile has not been hit", () => {
                props = {
                    ...props,
                    gameOver: false,
                    tile: new Tile(null, false)
                };
                const subject = shallow(<BoardTile {...props}/>);

                expect(subject.find(".clicked").exists()).toBeFalsy();
            });

            it("does not add 'clicked' for user agent type", () => {
                props = {
                    ...props,
                    agentType: AgentType.USER,
                    tile: new Tile(null, true)
                };
                let subject = shallow(<BoardTile {...props}/>);
                expect(subject.find(".clicked").exists()).toBeFalsy();

                props = {
                    ...props,
                    agentType: AgentType.USER,
                    gameOver: true
                };
                subject = shallow(<BoardTile {...props}/>);
                expect(subject.find(".clicked").exists()).toBeFalsy();
            });
        });
    });

    describe("clicking a tile", () => {
        it("calls tileClicked function", () => {
            const tileClicked = jest.fn();
            const props = {
                ...defaultProps,
                tileClicked
            };

            const subject = shallow(<BoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(tileClicked).toHaveBeenCalled();
        });

        it("does not call tileClicked function if the tile has already been hit", () => {
            const tileClicked = jest.fn();
            const props = {
                ...defaultProps,
                tileClicked,
                tile: new Tile(null, true)
            };

            const subject = shallow(<BoardTile {...props}/>);

            subject.find(".board__tile").get(0).props.onClick();

            expect(tileClicked).not.toHaveBeenCalled();
        });
    });
});
