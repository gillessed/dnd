import { combineReducers } from 'redux';
import notifications from './notifications';
import session from './session';
import users from './users';

export default combineReducers({
    notifications,
    session,
    users
});