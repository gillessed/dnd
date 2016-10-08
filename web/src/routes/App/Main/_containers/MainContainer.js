import { connect } from 'react-redux'
import Main from '../_components/Main'

const mapStateToProps = (state) => ({
    session: state.globals.session
});

export default connect(mapStateToProps)(Main);
