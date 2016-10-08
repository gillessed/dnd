import { connect } from 'react-redux'
import { login } from '../_reducers/login'

import Login from '../_components/Login'

const mapDispatchToProps = {
    login: (username, password) => login(username, password)
};

const mapStateToProps = (state) => {
    return {
        session: state.globals.session,
        loggingIn: state.login
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Login);
