export class Tile {
    ship: String;
    hit: boolean;

    constructor(ship: String = null, hit: boolean = false) {
        this.ship = ship;
        this.hit = hit;
    }
}

export type PlacedShip = {
    name: string;
    length: number;
    orientation: string;
}