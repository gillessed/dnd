import { combineReducers } from 'redux';
import notifications from './notifications';
import session from './session';
import users from './users';
import reload from './reload';

export default combineReducers({
    notifications,
    session,
    users,
    reload,
});