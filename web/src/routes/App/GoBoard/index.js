export default (store) => ({
    path: 'go',
    getComponent (nextState, cb) {
        require.ensure([], (require) => {
            const GoBoardPage = require('./_components/GoBoardPage').default;
            cb(null, GoBoardPage);
        })
    }
});
