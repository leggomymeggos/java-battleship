import axios from "axios";
import {Coordinate} from "../board/boardActions";

export class GameApi {
    private static baseUrl = "http://localhost:8081";

    public static newGame() {
        return axios.get("/games/new", {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }

    static hitBoard(coordinate: Coordinate) {
        return axios.put("/games/0/players/0/hit", coordinate, {
            baseURL: GameApi.baseUrl
        }).then((response) => response.data);
    }

    static fetchWinner() {
        return axios.get("/games/0/winner", {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }
}