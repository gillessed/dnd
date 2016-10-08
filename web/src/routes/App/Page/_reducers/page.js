import { combineReducers } from 'redux';
import Fetcher from '~/src/network/networker';

// ------------------------------------
// Constants
// ------------------------------------
export const FETCHING_PAGE ='FETCHING_PAGE';
export const PAGE_DATA = 'PAGE_DATA';

// ------------------------------------
// Actions
// ------------------------------------
export const setFetchingPage = () => {
    return {
        type: FETCHING_PAGE,
        payload: true
    }
};

export const doneFetchingPage = () => {
    return {
        type: FETCHING_PAGE,
        payload: false
    }
};

export const setPage = (page) => {
    return {
        type: PAGE_DATA,
        payload: page
    }
};

export const fetchPage = (pagePath) => {
    return (dispatch, getState) => {
        dispatch(setFetchingPage());
        return Fetcher.sessionFetch('/page', {
            method: 'POST',
            body: JSON.stringify({ page: pagePath })
        }).then((json) => {
            dispatch(setPage(json.page));
            dispatch(doneFetchingPage());
        }).catch(() => {
            dispatch(doneFetchingPage());
        });
    }
};

// ------------------------------------
// Action Handlers
// ------------------------------------
const FETCHING_PAGE_ACTION_HANDLERS = {
    [FETCHING_PAGE]: (state, action) => {
        return action.payload;
    }
};

const PAGE_DATA_ACTION_HANDLERS = {
    [PAGE_DATA]: (state, action) => {
        return action.payload;
    }
};

// ------------------------------------
// Reducer
// ------------------------------------
const initialFetchingPageState = false;
const fetchingPage = (state = initialFetchingPageState, action) => {
    const handler = FETCHING_PAGE_ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state;
};

const initialPageDataState = null;
const pageData = (state = initialPageDataState, action) => {
    const handler = PAGE_DATA_ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state;
};

export default combineReducers({
    fetchingPage,
    pageData
});
