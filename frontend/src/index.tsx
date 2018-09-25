import * as React from "react";
import * as ReactDOM from "react-dom";
import './styles/main.scss';

import {BrowserRouter} from "react-router-dom";
import {Main} from "./Main";

ReactDOM.render(
    <BrowserRouter>
        <Main/>
    </BrowserRouter>,
    document.getElementById("root")
);