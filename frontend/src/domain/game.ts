import {Agent} from "./agent";

export class Game {
    id: number;
    humanPlayer: Agent;
    computerPlayer: Agent;
    activePlayerId: number;
    winner: Agent;

    constructor(id: number = -1,
                humanPlayer: Agent = new Agent(),
                computerPlayer: Agent = new Agent(),
                activePlayerId: number = -1,
                winner: Agent = null
    ) {
        this.id = id;
        this.humanPlayer = humanPlayer;
        this.computerPlayer = computerPlayer;
        this.activePlayerId = activePlayerId;
        this.winner = winner;
    }
}