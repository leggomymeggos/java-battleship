export class Tile {
    shipId: number;
    hit: boolean;

    constructor(shipId: number = null, hit: boolean = false) {
        this.shipId = shipId;
        this.hit = hit;
    }
}