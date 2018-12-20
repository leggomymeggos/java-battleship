import {Tile} from "../domain/Tile";

export class Agent {
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

export type AgentState = {
    id: number;
    grid: Tile[][];
    sunkenShips: string[];
}
