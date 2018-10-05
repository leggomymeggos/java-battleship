import axios from "axios";
import {Coordinate} from "../board/boardActions";

export class GameApi {
    private static baseUrl = "http://localhost:8081";

    public static newGame() {
        return axios.get("/games/new", {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }

    static fetchWinner() {
        return axios.get("/games/0/winner", {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }

    static hitBoard(defendingPlayerId: number, coordinate: Coordinate, attackingPlayerId: number) {
        return axios.put(`/games/0/players/${defendingPlayerId}/hit?attackerId=${attackingPlayerId}`, coordinate, {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }
}