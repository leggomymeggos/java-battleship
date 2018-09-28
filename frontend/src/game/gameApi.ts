import axios from "axios";

export class GameApi {
    public static newGame() {
        return axios.get("/games/new", {
            baseURL: "http://localhost:8081"
        }).then((response) => response.data);
    }
}