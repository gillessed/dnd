import { combineReducers } from 'redux';
import Fetcher from '~/src/network/networker';

// ------------------------------------
// Constants
// ------------------------------------
export const SET_RELOADING = 'SET_RELOADING';

// ------------------------------------
// Actions
// ------------------------------------
export const setReloading = (reloading) => {
    return {
        type: SET_RELOADING,
        payload: { reloading }
    }
};

const reloadAll = () => {
    return (dispatch) => {
        dispatch(setReloading(true));
        return Fetcher.sessionFetch('/page/reload-all', {
            method: 'GET',
        }).then(() => {
            dispatch(setReloading(false));
            location.reload();
        }).catch(() => {
            dispatch(setReloading(false));
        });
    }
};

export const actions = {
    setReloading,
    reloadAll,
};

// ------------------------------------
// Action Handlers
// ------------------------------------
const ACTION_HANDLERS = {
    [SET_RELOADING]: (_, action) => {
        return action.payload.reloading;
    },
};

// ------------------------------------
// Reducer
// ------------------------------------
const initialState = null;
export default (state = initialState, action) => {
    const handler = ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state
};
