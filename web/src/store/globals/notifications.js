// ------------------------------------
// Constants
// ------------------------------------
export const ADD_NOTIFICATION = 'ADD_NOTIFICATION';
export const CLEAR_NOTIFICATION = 'CLEAR_NOTIFICATION';
export const Levels = {
    ERROR: 'ERROR',
    WARNING: 'WARNING',
    INFO: 'INFO',
    SUCCESS: 'SUCCESS'
};

// ------------------------------------
// Actions
// ------------------------------------
export function addErrorNotification (header, message) {
    return {
        type: ADD_NOTIFICATION,
        payload: {
            message,
            header,
            type: Levels.ERROR
        }
    };
}

export function addWarningNotification (header, message) {
    return {
        type: ADD_NOTIFICATION,
        payload: {
            message,
            header,
            type: Levels.WARNING
        }
    };
}

export function addInfoNotification (header, message) {
    return {
        type: ADD_NOTIFICATION,
        payload: {
            message,
            header,
            type: Levels.INFO
        }
    };
}

export function addSuccessNotification (header, message) {
    return {
        type: ADD_NOTIFICATION,
        payload: {
            message,
            header,
            type: Levels.SUCCESS
        }
    };
}

export function clearNotification (notification) {
    return {
        type: CLEAR_NOTIFICATION,
        payload: notification
    };
}

export const actions = {
    addErrorNotification,
    addWarningNotification,
    addInfoNotification,
    addSuccessNotification,
    clearNotification
};

// ------------------------------------
// Action Handlers
// ------------------------------------
const NOTIFICATION_ACTION_HANDLERS = {
    [ADD_NOTIFICATION]: (state, action) => {
        let newState = Array.from(state);
        newState.push(action.payload);
        return newState;
    },
    [CLEAR_NOTIFICATION]: (state, action) => {
        let newState = Array.from(state);
        let index = state.indexOf(action.payload);
        if (index >= 0) {
            newState.splice(index, 1);
        }
        return newState;
    }
};

// ------------------------------------
// Reducer
// ------------------------------------
const intialState = [];
export default (state = intialState, action) => {
    const handler = NOTIFICATION_ACTION_HANDLERS[action.type];
    return handler ? handler(state, action) : state
};
