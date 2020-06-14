import axios from "axios";

export class BoardApi {
    private static baseUrl = process.env.BASE_URL || "http://example.com";

    public static fetchBoard(gameId: number, playerId: number) {
        return axios.get("/games/" + gameId + "/players/" + playerId + "/board", {
            baseURL: BoardApi.baseUrl,
        }).then((response) => response.data);
    }
}