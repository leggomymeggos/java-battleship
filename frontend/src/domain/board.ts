import {Tile} from "./tile";

export class Board {
    grid: any[][];
    sunkenShips: String[];

    constructor(grid: any[][],
                sunkenShips: String[]) {
        this.grid = grid;
        this.sunkenShips = sunkenShips;
    }
}