jest.mock("../../boardActions");

// import {shallow} from "enzyme";
import * as React from "react";
import {Tile} from "../../../domain/tile";
import BoardTile, {AgentType, BoardTileProps} from "../BoardTile";

// describe("BoardTile", () => {
//     let defaultProps: BoardTileProps;
//     let mockActions: any;
//
//     beforeEach(() => {
//         mockActions = {
//             boardHit: jest.fn()
//         };
//
//         defaultProps = {
//             tile: new Tile(),
//             coordinate: {column: 0, row: 0},
//             agentType: AgentType.NONE,
//             gameOver: false,
//             tileClicked: () => {
//             }
//         }
//     });
//
//     describe("tiles styling", () => {
//         describe("hit indicator", () => {
//             it("adds 'aimed__miss--image' if the tile was hit but contained no ship", () => {
//                 const props = {
//                     ...defaultProps,
//                     tile: new Tile(null, true)
//                 };
//
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".aimed__miss--image").exists()).toBeTruthy();
//                 expect(subject.find(".aimed__hit--image").exists()).toBeFalsy();
//             });
//
//             it("adds 'aimed__hit--image' if the tile was hit and contained a ship", () => {
//                 const props = {
//                     ...defaultProps,
//                     tile: new Tile( "ship-name", true)
//                 };
//
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".aimed__hit--image").exists()).toBeTruthy();
//                 expect(subject.find(".aimed__miss--image").exists()).toBeFalsy();
//             });
//
//             it("does not add anything if the tile was not hit", () => {
//                 const props = {
//                     ...defaultProps
//                 };
//
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".aimed__hit--image").exists()).toBeFalsy();
//                 expect(subject.find(".aimed__miss--image").exists()).toBeFalsy();
//             });
//         });
//
//         describe("user agent specific styling", () => {
//             let props: BoardTileProps;
//             beforeEach(() => {
//                 props = {
//                     ...defaultProps,
//                     agentType: AgentType.USER
//                 };
//             });
//
//             it("adds 'ship__' with the ship name if the tile has a ship", () => {
//                 props = {
//                     ...props,
//                     tile: new Tile("super_ship", false)
//                 };
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".ship__super_ship").exists()).toBeTruthy();
//             });
//         });
//
//         describe("opposing agent specific styling", () => {
//             let props: BoardTileProps;
//             beforeEach(() => {
//                 props = {
//                     ...defaultProps,
//                     agentType: AgentType.OPPONENT
//                 };
//             });
//
//             it("adds 'clicked' if the tile has been hit", () => {
//                 props = {
//                     ...props,
//                     tile: new Tile(null, true)
//                 };
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".clicked").exists()).toBeTruthy();
//             });
//
//             it("adds 'clicked' if the game is over", () => {
//                 props = {
//                     ...props,
//                     gameOver: true,
//                 };
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".clicked").exists()).toBeTruthy();
//             });
//
//             it("does not add 'clicked' if the game is not over and the tile has not been hit", () => {
//                 props = {
//                     ...props,
//                     gameOver: false,
//                     tile: new Tile(null, false)
//                 };
//                 const subject = shallow(<BoardTile {...props}/>);
//
//                 expect(subject.find(".clicked").exists()).toBeFalsy();
//             });
//
//             it("does not add 'clicked' for user agent type", () => {
//                 props = {
//                     ...props,
//                     agentType: AgentType.USER,
//                     tile: new Tile(null, true)
//                 };
//                 let subject = shallow(<BoardTile {...props}/>);
//                 expect(subject.find(".clicked").exists()).toBeFalsy();
//
//                 props = {
//                     ...props,
//                     agentType: AgentType.USER,
//                     gameOver: true
//                 };
//                 subject = shallow(<BoardTile {...props}/>);
//                 expect(subject.find(".clicked").exists()).toBeFalsy();
//             });
//         });
//     });
//
//     describe("clicking a tile", () => {
//         it("calls tileClicked function", () => {
//             const tileClicked = jest.fn();
//             const props = {
//                 ...defaultProps,
//                 tileClicked
//             };
//
//             const subject = shallow(<BoardTile {...props}/>);
//
//             subject.find(".board__tile").get(0).props.onClick();
//
//             expect(tileClicked).toHaveBeenCalled();
//         });
//
//         it("does not call tileClicked function if the tile has already been hit", () => {
//             const tileClicked = jest.fn();
//             const props = {
//                 ...defaultProps,
//                 tileClicked,
//                 tile: new Tile(null, true)
//             };
//
//             const subject = shallow(<BoardTile {...props}/>);
//
//             subject.find(".board__tile").get(0).props.onClick();
//
//             expect(tileClicked).not.toHaveBeenCalled();
//         });
//     });
// });
