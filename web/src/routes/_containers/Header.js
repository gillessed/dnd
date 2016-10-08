import { connect } from 'react-redux'

import Header from '../_components/Header'
import * as _ from 'lodash'

const mapStateToProps = (state) => {
    return {
        session: state.globals.session
    }
};

export default connect(mapStateToProps)(Header);
