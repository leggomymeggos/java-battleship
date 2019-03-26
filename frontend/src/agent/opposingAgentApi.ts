import axios from "axios";

export class OpposingAgentApi {
    private static baseUrl = process.env.BASE_URL || "http://example.com";

    public static attack(gameId: number, attackerId: number) {
        return axios.get(`/games/${gameId}/opponent-attack?attackerId=${attackerId}`, {
            baseURL: OpposingAgentApi.baseUrl
        }).then((response) => response.data)
    }
}