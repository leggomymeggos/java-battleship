export class Tile {
    ship: string;
    hit: boolean;

    constructor(ship: string = null, hit: boolean = false) {
        this.ship = ship;
        this.hit = hit;
    }
}