import { combineReducers } from 'redux';
import Fetcher from '~/src/network/networker';
import { addErrorNotification } from '~/src/store/globals/notifications';

import { createAsyncObjectReducer } from '../../../../store/creators';

// ------------------------------------
// Composite Actions
// ------------------------------------
export const fetchStatus = () => {
    return (dispatch) => {
        dispatch(actions.setLoading(true));
        dispatch(actions.setStatus(null));
        return Fetcher.sessionFetch('/status', {
            method: 'GET'
        }).then((json) => {
            dispatch(actions.setStatus(json));
            dispatch(actions.setLoading(false));
        }).catch(() => {
            dispatch(actions.setLoading(false));
        });
    }
};

const reducer = createAsyncObjectReducer('status');
export const actions = {...reducer.actions, fetchStatus};
export const actionTypes = reducer.actionTypes;
export const status = reducer.status;