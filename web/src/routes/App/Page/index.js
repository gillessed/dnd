import { injectReducer } from '~/src/store/reducers'
import { fetchPage, fetchDirectoryContents } from './_reducers/page'

export default (store) => ({
    path: 'page/:name',
    getComponent (nextState, cb) {
        require.ensure([], (require) => {
            const Page = require('./_containers/PageContainer').default
            const reducer = require('./_reducers/page').default;
            injectReducer(store, {key: 'page', reducer});
            cb(null, Page);
        }, 'page/:name')
    },
    onEnter: (nextState) => {
        store.dispatch(fetchPage(nextState.params.name));
    }
});
