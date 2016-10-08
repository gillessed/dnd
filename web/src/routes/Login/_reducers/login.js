import { combineReducers } from 'redux';
import { browserHistory } from 'react-router';
import { addErrorNotification } from '~/src/store/globals/notifications';
import { createSession } from '~/src/store/globals/session';
import Fetcher from '~/src/network/networker';

// ------------------------------------
// Constants
// ------------------------------------
export const LOGGING_IN ='LOGGING_IN';
export const DONE_LOGGING_IN = 'DONE_LOGGING_IN';

// ------------------------------------
// Actions
// ------------------------------------
export const login = (username, password) => {
    return (dispatch, getState) => {
        dispatch(setLoggingIn());
        return Fetcher.rawFetch('/auth', {
            method: 'POST',
            body: JSON.stringify({ username, password })
        }).then((json) => {
            dispatch(createSession(json.userId, json.token));
            browserHistory.push('/app');
            dispatch(doneLoggingIn());
        }).catch(() => {
            dispatch(doneLoggingIn());
        });
    }
};

export const setLoggingIn = () => {
    return {
        type: LOGGING_IN,
        payload: {}
    }
};

export const doneLoggingIn = () => {
    return {
        type: DONE_LOGGING_IN,
        payload: {}
    }
};

// ------------------------------------
// Action Handlers
// ------------------------------------

const ACTION_HANDLERS = {
    [LOGGING_IN]: (state, action) => {
        return true;
    },
    [DONE_LOGGING_IN]: (state, action) => {
        return false;
    }
};

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = false;
export default (state = initialState, action) => {
    const handler = ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state
};
