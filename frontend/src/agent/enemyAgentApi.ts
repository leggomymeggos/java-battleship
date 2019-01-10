import axios from "axios";

export class EnemyAgentApi {
    private static baseUrl = process.env.BASE_URL || "http://example.com";

    public static attack(gameId: number, attackerId: number) {
        return axios.get(`/games/${gameId}/enemy-attack?attackerId=${attackerId}`, {
            baseURL: EnemyAgentApi.baseUrl
        }).then((response) => response.data)
    }
}