import { connect } from 'react-redux'

import Header from '../_components/Header'
import {actions} from '../../store/globals/reload';

const mapStateToProps = (state) => {
    return {
        session: state.globals.session,
        users: state.globals.users,
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        reloadAll: () => {dispatch(actions.reloadAll())}
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Header);
