import { combineReducers } from 'redux';
import Fetcher from '~/src/network/networker';

// ------------------------------------
// Constants
// ------------------------------------
const SET_USER = 'SET_USER';
const SET_USER_ERROR = 'SET_USER_ERROR';
const SET_USER_LOADING = 'SET_USER_LOADING';

// ------------------------------------
// Actions
// ------------------------------------
const setUser = (userId, user) => {
    return {
        type: SET_USER,
        userId,
        user
    }
};

const setUserError = (userId, error) => {
    return {
        type: SET_USER_ERROR,
        userId,
        error
    }
};

const setUserLoading = (userId, loading) => {
    return {
        type: SET_USER_LOADING,
        userId,
        loading
    }
};

const fetchUser = (userId) => {
    return (dispatch) => {
        dispatch(setUserLoading(userId, true));
        return Fetcher.sessionFetch(`/user/roles/${userId}`, {
            method: 'GET'
        }).then((json) => {
            dispatch(setUserLoading(userId, false));
            dispatch(setUserError(userId, null));
            dispatch(setUser(userId, json));
        }).catch((error) => {
            dispatch(setUserLoading(userId, false));
            dispatch(setUser(userId, null));
            dispatch(setUserError(userId, error.message));
        });
    }
};

export const actions = {
    fetchUser,
    setUser,
    setUserError,
    setUserLoading
};

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
    [SET_USER] : (state, action) => {
        let user = Object.assign(
            getUser(state, action.userId),
            {content: action.user});
        return Object.assign({},
            state,
            {[action.userId]: user});
    },
    [SET_USER_ERROR] : (state, action) => {
        let user = Object.assign(
            getUser(state, action.userId),
            {error: action.error});
        return Object.assign({},
            state,
            {[action.userId]: user});
    },
    [SET_USER_LOADING] : (state, action) => {
        let user = Object.assign(
            getUser(state, action.userId),
            {loading: action.loading});
        return Object.assign({},
            state,
            {[action.userId]: user});
    }
};

function getUser(state, userId) {
    let user = state[userId];
    if (!user) {
        user = {
            content: null,
            loading: false,
            error: null
        }
    }
    return user;
}

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = {};
export default (state = initialState, action) => {
    const handler = ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state
};
