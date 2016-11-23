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
        if(error.errorObject) {
            dispatch(addErrorNotification('Error', error.errorObject.errorMessage));
            browserHistory.push(error.errorObject.redirect);
        } else {
            dispatch(addErrorNotification('Error', 'There was an error connecting to the server.'));
            browserHistory.push('/app');
        }
    }

    rawFetch(path, options) {
        options.headers = Object.assign(
            {
                'Content-Type': 'application/json'
            },
            options.headers);
        return fetch(SERVER_URL + path, options).then((response) => {
            if (response.status != 200 && response.status != 500 && response.status != 401) {
                throw new StatusCodeError({statusCode: response.status});
            }
            return response.json();
        }).then((json) => {
            if (json.errorType) {
                throw new StatusCodeError({errorObject: json});
            }
            return new Promise((resolve) => resolve(json));
        }).catch((error) => {
            this._failedWithError(error);
            throw error;
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
            if (response.status != 200 && response.status != 500 && response.status != 401) {
                throw new StatusCodeError({statusCode: response.status});
            }
            return response.json();
        }).then((json) => {
            if (json.errorType) {
                throw new StatusCodeError({errorObject: json});
            }
            return new Promise((resolve) => resolve(json));
        }).catch((error) => {
            this._failedWithError(error);
            throw error;
        });
    }
}

export default new NetworkFetcher();
