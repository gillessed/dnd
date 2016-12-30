import { connect } from 'react-redux'

import Page from '../_components/Page'
import * as pageReducer from '../_reducers/page'

const mapDispatchToProps = {
    fetchPage: (pagePath) => pageReducer.actions.fetchPage(pagePath),
    reloadPage: (pagePath) => pageReducer.actions.reloadPage(pagePath)
};

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData ? state.page.pageData.page : null,
        pagePath: state.page.pageData ? state.page.pageData.path : null,
        users: state.globals.users,
        session: state.globals.session,
        fetchingPage: state.page.fetchingPage
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Page);
