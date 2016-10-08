import { combineReducers } from 'redux';
import { browserHistory } from 'react-router';

// ------------------------------------
// Constants
// ------------------------------------
export const CREATE_SESSION = 'CREATE_SESSION';
export const DESTROY_SESSION = 'DESTROY_SESSION';

// ------------------------------------
// Actions
// ------------------------------------
export const createSession = (userId, token) => {
    return {
        type: CREATE_SESSION,
        payload: { userId, token }
    }
};

export const destroySession = () => {
    return {
        type: DESTROY_SESSION,
        payload: {}
    }
};

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
    [CREATE_SESSION] : (state, action) => {
        return {
            userId: action.payload.userId,
            token: action.payload.token
        };
    },
    [DESTROY_SESSION] : (state, action) => {
        return null;
    }
};

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = null;
export default (state = initialState, action) => {
    const handler = ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state
};
