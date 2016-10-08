import { combineReducers } from 'redux';
import notifications from './notifications';
import session from './session';

export default combineReducers({
    notifications,
    session
});