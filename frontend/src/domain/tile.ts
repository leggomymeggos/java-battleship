export class Tile {
    ship: PlacedShip;
    hit: boolean;

    constructor(ship: PlacedShip = null, hit: boolean = false) {
        this.ship = ship;
        this.hit = hit;
    }
}

export type PlacedShip = {
    name: string;
    length: number;
    orientation: string;
}