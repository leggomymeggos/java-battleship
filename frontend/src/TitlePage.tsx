import * as React from 'react';
import {Link} from "react-router-dom";
import gameActions from "./game/gameActions";
import {connect, Dispatch} from "react-redux";
import {bindActionCreators} from "redux";

interface ITitlePagePropsFromActions {
    actions: {
        createNewGame: any
    }
}

export type TitlePageProps = ITitlePagePropsFromActions


export class TitlePage extends React.Component<TitlePageProps> {
    public render() {
        return <div>
            <div className="title-page__title">Battleship</div>
            <Link to="/game" className="title-page__new-game" onClick={this.props.actions.createNewGame}>New Game</Link>
        </div>
    }
}

export const mapDispatchToProps = (dispatch: Dispatch<{}>): ITitlePagePropsFromActions => {
    return {
        actions: bindActionCreators({
            ...gameActions
        }, dispatch)
    }
};

export default connect(null, mapDispatchToProps)(TitlePage);