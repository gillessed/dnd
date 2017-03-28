import { connect } from 'react-redux'

import CoreLayout from '../_components/CoreLayout'

const mapStateToProps = (state) => {
    return {
        reload: state.globals.reload,
    }
};

export default connect(mapStateToProps)(CoreLayout);
