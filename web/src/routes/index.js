// We only need to import the modules necessary for initial render
import CoreLayout from './_components/CoreLayout'
import Home from './Home'
import LoginRoute from './Login'
import AppRoute from './App'

export const createRoutes = (store) => ({
    path: '/',
    component: CoreLayout,
    indexRoute: Home,
    childRoutes: [
        LoginRoute(store),
        AppRoute(store)
    ],
    onChange: () => {

    }
});

export default createRoutes;
