import { connect } from 'react-redux'

import Wiki from '../_components/Wiki'

const mapStateToProps = (state) => {
    return {
        session: state.globals.session
    }
};

export default connect(mapStateToProps)(Wiki);
