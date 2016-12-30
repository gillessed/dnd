import React from 'react'
import ReactDOM from 'react-dom'
import createStore from './store/createStore'
import AppContainer from './containers/AppContainer'
import Fetcher from './network/networker';
import { persistStore } from 'redux-persist'

// ========================================================
// Promise and Fetch polyfills
// ========================================================
import Promise from 'promise-polyfill';
if (!window.Promise) {
    window.Promise = Promise;
}
import 'whatwg-fetch'

// ========================================================
// Store Instantiation
// ========================================================
const initialState = window.___INITIAL_STATE__;
const store = createStore(initialState);
const persist = persistStore(store, { blacklist: [
    'notifications',
    'sectionVisibility'
] }, () => {
    Fetcher.setStore(store);

    // ========================================================
    // Render Setup
    // ========================================================
    const MOUNT_NODE = document.getElementById('root');

    let render = () => {
        const routes = require('./routes/index').default(store);

        ReactDOM.render(
            <AppContainer store={store} routes={routes} />,
            MOUNT_NODE
        )
    };

    // ========================================================
    // Developer Tools Setup
    // ========================================================
    if (__DEV__) {
        if (window.devToolsExtension) {
            window.devToolsExtension.open()
        }
    }

    // This code is excluded from production bundle
    if (__DEV__) {
        if (module.hot) {
            // Development render functions
            const renderApp = render
            const renderError = (error) => {
                const RedBox = require('redbox-react').default

                ReactDOM.render(<RedBox error={error} />, MOUNT_NODE)
            };

            // Wrap render in try/catch
            render = () => {
                try {
                    renderApp()
                } catch (error) {
                    renderError(error)
                }
            };

            // Setup hot module replacement
            module.hot.accept('./routes/index', () =>
                setImmediate(() => {
                    ReactDOM.unmountComponentAtNode(MOUNT_NODE)
                    render()
                })
            )
        }
    }

    // ========================================================
    // Go!
    // ========================================================
    render();
});

// Enable and refresh to clear redux cache.
//persist.purge();