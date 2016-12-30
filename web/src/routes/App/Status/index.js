import { injectReducer } from '~/src/store/reducers'
import * as status from './_reducers/status'

export default (store) => ({
    path: 'status',
    getComponent (nextState, cb) {
        require.ensure([], (require) => {
            const Page = require('./_containers/StatusContainer').default;
            const reducer = require('./_reducers/status').status;
            injectReducer(store, {key: 'status', reducer});
            store.dispatch(status.actions.fetchStatus());
            cb(null, Page);
        })
    }
});
