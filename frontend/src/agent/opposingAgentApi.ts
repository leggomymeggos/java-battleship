import axios from "axios";
import {Tile} from "../domain/tile";

const baseURI = "/api/games"

export const attack = async (gameId: number, attackerId: number): Promise<{ result: string, board: { grid: Tile[][], sunkenShips: string[]; } }> => {
    return axios.get(`${baseURI}/${gameId}/opponent-attack?attackerId=${attackerId}`, {
        baseURL: process.env.DOMAIN || 'http://locahost:8080'
    })
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        })
}