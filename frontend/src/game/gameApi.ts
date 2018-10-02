import axios from "axios";
import {Coordinate} from "../board/boardActions";

export class GameApi {
    public static newGame() {
        return axios.get("/games/new", {
            baseURL: "http://localhost:8081"
        }).then((response) => response.data);
    }

    static hitBoard(coordinate: Coordinate) {
        return axios.put("/games/0/players/0/hit", coordinate,{
            baseURL: "http://localhost:8081"
        }).then((response) => response.data);

    }
}