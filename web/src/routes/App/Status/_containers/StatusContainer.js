import { connect } from 'react-redux'

import Status from '../_components/Status'

const mapStateToProps = (state) => {
    return {
        status: state.status
    }
};

export default connect(mapStateToProps)(Status);
