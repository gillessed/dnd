/**
 * This function will create a reducer and a set of actions for the template
 * {
 *      content: null,
 *      loading: false,
 *      error: null
 *  }
 * and will produce the following three actions:
 *   set[ObjectName](object)
 *   setLoading(loading)
 *   setError(error)
 * which should be used for anything that might have to be loaded asynchronously.
 */
export function createAsyncObjectReducer(objectName) {
    const underscoreUppercasedName = objectName.replace(/([A-Z])/g, function ($1) {
        return "_" + $1;
    }).toUpperCase();
    const firstUppercaseName = objectName.charAt(0).toUpperCase() + objectName.slice(1);
    let reducer = {
        actionTypes: {},
        actions: {}
    };
    const OBJECT_ACTION_TYPE = `SET_${underscoreUppercasedName}`;
    reducer.actionTypes[OBJECT_ACTION_TYPE] = OBJECT_ACTION_TYPE;
    reducer.actions['set' + firstUppercaseName] = (object) => {
        return (dispatch) => {
            dispatch({
                type: OBJECT_ACTION_TYPE,
                object
            });
        };
    };
    const LOADING_ACTION_TYPE = `SET_${underscoreUppercasedName}_LOADING`;
    reducer.actionTypes[LOADING_ACTION_TYPE] = LOADING_ACTION_TYPE;
    reducer.actions.setLoading = (loading) => {
        return (dispatch) => {
            dispatch({
                type: LOADING_ACTION_TYPE,
                loading
            });
        };
    };
    const ERROR_ACTION_TYPE = `SET_${underscoreUppercasedName}_ERROR`;
    reducer.actionTypes[ERROR_ACTION_TYPE] = ERROR_ACTION_TYPE;
    reducer.actions.setError = (error) => {
        return (dispatch) => {
            dispatch({
                type: ERROR_ACTION_TYPE,
                error
            });
        };
    };
    const INITIAL_STATE = {
        content: null,
        loading: false,
        error: null
    };
    reducer[objectName] = (state = INITIAL_STATE, action) => {
        switch (action.type) {
            case OBJECT_ACTION_TYPE:
                return Object.assign({}, state, {content: action.object});
            case LOADING_ACTION_TYPE:
                return Object.assign({}, state, {loading: action.loading});
            case ERROR_ACTION_TYPE:
                return Object.assign({}, state, {error: action.error});
            default:
                return state;
        }
    };
    return reducer;
}
