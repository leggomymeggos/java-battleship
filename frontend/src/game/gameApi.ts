import axios from "axios";
import {Coordinate} from "../board/boardActions";

export class GameApi {
    private static baseUrl = process.env.BASE_URL || "http://example.com";

    public static newGame() {
        return axios.get("/games/new", {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }

    static fetchWinner(gameId: number) {
        return axios.get(`/games/${gameId}/winner`, {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }

    static attack(gameId: number, attackingPlayerId: number, coordinate: Coordinate) {
        return axios.put(`/games/${gameId}/attack?attackerId=${attackingPlayerId}`, coordinate, {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data)
            .catch((error) => console.error(`Error attacking board: ${error.message}`));
    }

    static fetchActivePlayer(gameId: number) {
        return axios.get(`/games/${gameId}/players/active`, {
            baseURL: GameApi.baseUrl,
        }).then((response) => response.data);
    }
}