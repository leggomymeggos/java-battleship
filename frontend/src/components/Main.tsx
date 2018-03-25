import * as React from "react";
import {Route} from "react-router";
import {Board} from "./board/Board";

export class Main extends React.Component {
    public render() {
        return <div>
            <div>
                <Route path="/" component={Board}/>
            </div>
        </div>;
    }
}