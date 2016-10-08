import { injectReducer } from '~/src/store/reducers'

export default (store) => ({
    path: 'login',
    getComponent (nextState, cb) {
        require.ensure([], (require) => {
            const Login = require('./_containers/LoginContainer').default
            const reducer = require('./_reducers/login').default;
            injectReducer(store, {key: 'login', reducer});
            cb(null, Login);
        }, 'login')
    }
});
