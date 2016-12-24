import { combineReducers } from 'redux';
import Fetcher from '~/src/network/networker';
import { addErrorNotification } from '~/src/store/globals/notifications';

// ------------------------------------
// Constants
// ------------------------------------
export const FETCHING_PAGE ='FETCHING_PAGE';
export const PAGE_DATA = 'PAGE_DATA';
export const SET_SECTION_VISIBLE = 'SET_SECTION_VISIBLE';

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

export const setSectionVisible = (section, visible) => {
    return {
        type: SET_SECTION_VISIBLE,
        payload: { section, visible }
    }
};

export const fetchPage = (pagePath) => {
    return (dispatch, getState) => {
        dispatch(setFetchingPage());
        dispatch(setPage(null));
        return Fetcher.sessionFetch('/page', {
            method: 'POST',
            body: JSON.stringify({ target: pagePath })
        }).then((json) => {
            json.path = pagePath;
            dispatch(setPage(json));
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

const SECTION_VISIBILITY_HANDLERS = {
    [SET_SECTION_VISIBLE]: (state, action) => {
        let nextState = Object.assign({}, state);
        nextState[action.payload.section] = action.payload.visible;
        return nextState;
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

const initialSectionVisibilityState = {};
const sectionVisibility = (state = initialSectionVisibilityState, action) => {
    const handler = SECTION_VISIBILITY_HANDLERS[action.type];
    return handler ? handler(state, action) : state;
};

export default combineReducers({
    fetchingPage,
    pageData,
    sectionVisibility
});
