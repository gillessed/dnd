import { destroySession } from '~/src/store/globals/session'
import { browserHistory } from 'react-router'
import { addErrorNotification } from '~/src/store/globals/notifications'
import { NetworkError } from './networkErrors'

class Networker {

    // setStore() is called after store creation and hydration in configureStore.js
    // No network calls should be made before this.
    setStore = (store) => {
        this.store = store;
    };

    fetch(path, options) {
        if (!this.store) {
            return Promise.reject(new Error('There is no store set in the networker. Has configureStore.js called setStore?'));
        }
        options.headers = Object.assign({
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }, options.headers);
        return this._performFetch(path, options, false);
    }

    sessionFetch(path, options) {
        if (!this.store) {
            return Promise.reject(new Error('There is no store set in the networker. Has configureStore.js called setStore?'));
        }
        let session = this._getSession();
        if (!session) {
            return Promise.reject(new Error('There is no current active session.'));
        }
        options.headers = Object.assign({
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'X-Auth-Token': session.token
        }, options.headers);
        return this._performFetch(path, options, true);
    }

    _performFetch(path, options, isSessioned) {
        return fetch(this._getServerUrl() + path, options).then((response) => {
            let contentType = response.headers.get('content-type');
            if (contentType && contentType.indexOf('application/json') !== -1) {
                return response.json().then((json) => {
                    if (response.status >= 200 && response.status <= 299) {
                        return Promise.resolve(json);
                    } else {
                        return Promise.reject(new NetworkError({statusCode: response.status, errorObject: json}));
                    }
                });
            } else if (!(response.status >= 200 && response.status <= 299)) {
                return Promise.reject(new NetworkError({statusCode: response.status}));
            } else {
                return Promise.resolve(response);
            }
        }).catch((error) => {
            if (isSessioned && error.statusCode === 403) {
                this.store.dispatch(destroySession());
            }
            this._failedWithError(error);
            console.warn('Error during network call', error);
            return Promise.reject(error);
        });
    }

    _failedWithError(error) {
        let dispatch = this.store.dispatch;
        if (error.errorObject) {
            if (error.errorObject.redirect) {
                dispatch(addErrorNotification('Error', error.errorObject.errorMessage));
                browserHistory.push(error.errorObject.redirect);
            } else {
                dispatch(addErrorNotification('Error', 'There was an error connecting to the server.'));
                browserHistory.push('/app');
            }
        }
    }

    _getServerUrl() {
        return SERVER_API_URL;
    }

    _getSession() {
        if (!this.store) {
            return null;
        }
        return this.store.getState().globals.session
    }
}

export default new Networker();
