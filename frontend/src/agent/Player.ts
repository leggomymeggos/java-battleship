import {Tile} from "../domain/Tile";

export class Player {
    id: number;
    board: {
        grid: Tile[][];
        sunkenShips: string[];
    };

    constructor(id: number = -1, board: {
        grid: Tile[][];
        sunkenShips: string[];
    } = {grid: [], sunkenShips: []}) {
        this.id = id;
        this.board = board;
    }
}