import axios from "axios";
import {Board} from "../domain/board";

export const fetchBoard = async (gameId: number, playerId: number): Promise<Board> => {
    return axios.get(`/api/games/${gameId}/players/${playerId}/board`)
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        })
}
