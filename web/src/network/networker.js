import { destroySession } from '~/src/store/globals/session'
import { browserHistory } from 'react-router'
import { addErrorNotification } from '~/src/store/globals/notifications'
import { StatusCodeError } from './networkErrors'

class NetworkFetcher {
    _getSession() {
        if (!this.store) {
            return null;
        }
        return this.store.getState().globals.session;
    }

    _failedWithError(error) {
        let dispatch = this.store.dispatch;
        if (error.statusCode == 401) {
            dispatch(destroySession());
            dispatch(addErrorNotification('Failed', 'Your session is invalid. Please log in again.'));
            browserHistory.push('/login');
        } else {
            dispatch(addErrorNotification('Failed', 'There was an error connecting to the server.'));
            browserHistory.push('/app');
        }
        console.warn('Fetch returned with error: ' + error.message);
    }

    rawFetch(path, options) {
        options.headers = Object.assign(
            {
                'Content-Type': 'application/json'
            },
            options.headers);
        return fetch(SERVER_URL + path, options).then((response) => {
            if (response.status != 200) {
                throw new StatusCodeError(response.status);
            }
            return response.json();
        }).catch((error) => {
            this._failedWithError(error);
        });
    }

    sessionFetch(path, options) {
        let session = this._getSession();
        if (!session) {
            return new Promise((resolve, reject) => {
               reject('There is no session');
            });
        }
        options.headers = Object.assign(
            {
                'Content-Type': 'application/json',
                'X-Auth-Token': session.token
            },
            options.headers);
        return fetch(SERVER_URL + path, options).then((response) => {
            if (response.status != 200) {
                throw new StatusCodeError(response.status);
            }
            return response.json();
        }).catch((error) => {
            this._failedWithError(error);
        });
    }
}

export default new NetworkFetcher();
