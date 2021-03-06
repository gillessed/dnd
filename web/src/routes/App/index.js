import React from 'react'
import Main from './Main'
import WikiRoute from './Wiki'
import PageRoute from './Page'
import StatusRoute from './Status'
import GoRoute from './GoBoard'

export const createRoutes = (store) => ({
    path: 'app',
    component: ({ children }) => (<div>{children}</div>),
    indexRoute: Main,
    childRoutes: [
        WikiRoute(store),
        PageRoute(store),
        StatusRoute(store),
        GoRoute(store),
    ],
    onEnter(nextState, replace, callback) {
        if (!store.getState().globals.session) {
            replace('/login');
        }
        callback();
    }
});

export default createRoutes;
