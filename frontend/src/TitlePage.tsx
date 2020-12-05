import * as React from 'react';
import {Link} from "react-router-dom";
import gameActions from "./game/gameActions";
import {connect} from "react-redux";
import {bindActionCreators, Dispatch} from "redux";

interface ITitlePagePropsFromActions {
    actions: {
        createNewGame: any
    }
}

export type TitlePageProps = ITitlePagePropsFromActions

export class TitlePage extends React.Component<TitlePageProps> {
    public render() {
        return <>
            <div className="title-page">

                <div className="title-page__title">Battleship</div>
                <Link to="/game"
                      className="title-page__new-game"
                      onClick={this.props.actions.createNewGame}>
                    New Game
                </Link>
            </div>
            <p>backend: {process.env.DOMAIN}</p>
            <p>run mode: {process.env.NODE_ENV}</p>
        </>
    }
}

export const mapDispatchToProps = (dispatch: Dispatch<{type: any}>): ITitlePagePropsFromActions => {
    return {
        actions: bindActionCreators({
            ...gameActions
        }, dispatch)
    }
};

export default connect(null, mapDispatchToProps)(TitlePage);