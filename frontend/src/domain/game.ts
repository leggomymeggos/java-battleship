
export class Game {
    id: number;
    playerIds: number[];
    activePlayerId: number;
    winnerId: number;
    difficulty: string;

    constructor(id: number = -1,
                playerIds: number[] = [],
                activePlayerId: number = -1,
                winnerId: number = -1,
                difficulty: string = "EASY"
    ) {
        this.id = id;
        this.playerIds = playerIds;
        this.activePlayerId = activePlayerId;
        this.winnerId = winnerId;
        this.difficulty = difficulty;
    }
}