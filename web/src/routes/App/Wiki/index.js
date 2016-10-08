export default (store) => ({
    path: 'wiki',
    getComponent (nextState, cb) {
        require.ensure([], (require) => {
            const Wiki = require('./_containers/WikiContainer').default;
            cb(null, Wiki);
        }, 'wiki')
    }
});
