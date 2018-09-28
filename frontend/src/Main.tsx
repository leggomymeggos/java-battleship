import * as React from "react";
import {Route} from "react-router";
import {createStore, applyMiddleware} from "redux";
import createSagaMiddleware from "redux-saga";
import {Provider} from "react-redux";
import Board from "./board/Board";
import {rootReducer} from "./rootReducer";
import {fetchGameSaga} from "./game/gameSaga";

const sagaMiddleware = createSagaMiddleware();
const store = createStore(rootReducer, applyMiddleware(sagaMiddleware));

sagaMiddleware.run(fetchGameSaga);

export class Main extends React.Component {
    public render() {
        return <Provider store={store}>
            <Route path="/" component={Board}/>
        </Provider>;
    }
}