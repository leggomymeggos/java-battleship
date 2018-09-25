import * as React from "react";
import {Route} from "react-router";
import {createStore, applyMiddleware} from "redux";
import Board from "./board/Board";
import {rootReducer} from "./rootReducer";
import thunk from "redux-thunk";
import {Provider} from "react-redux";

const store = createStore(rootReducer, applyMiddleware(thunk));

export class Main extends React.Component {
    public render() {
        return <Provider store={store}>
                <Route path="/" component={Board}/>
        </Provider>;
    }
}