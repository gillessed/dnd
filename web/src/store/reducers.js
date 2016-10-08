import { combineReducers } from 'redux'
import locationReducer from './location'
import globals from './globals'

export const makeRootReducer = (asyncReducers) => {
    return combineReducers({
        globals,
        location: locationReducer,
        ...asyncReducers
    })
};

export const injectReducer = (store, { key, reducer }) => {
    store.asyncReducers[key] = reducer;
    store.replaceReducer(makeRootReducer(store.asyncReducers));
};

export default makeRootReducer
