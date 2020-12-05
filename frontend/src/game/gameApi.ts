import axios from "axios";
import {Coordinate} from "../board/boardActions";
import {Game} from "../domain/game";
import {Tile} from "../domain/tile";

const baseURI = '/api/games'

export const newGame = async (): Promise<Game> => {
    return await axios.get(`${baseURI}/new`)
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        });
}

export const attack = async (gameId: number, attackingPlayerId: number, coordinate: Coordinate): Promise<{ result: { hitType?: string, shipName?: string }, board: { grid: Tile[][], sunkenShips: string[] } }> => {
    return await axios.put(`${baseURI}/${gameId}/attack?attackerId=${attackingPlayerId}`, coordinate)
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        });
}

export const fetchWinner = async (gameId: number): Promise<{ winnerId: number }> => {
    return await axios.get(`${baseURI}/${gameId}/winner`)
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        })
}

export const fetchActivePlayer = async (gameId: number): Promise<{ gameId: number, activePlayerId: number }> => {
    return await axios.get(`${baseURI}/${gameId}/players/active`)
        .then((response) => response.data)
        .catch((e) => {
            throw new Error(e)
        })
}
