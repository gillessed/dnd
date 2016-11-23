import { connect } from 'react-redux'

import Page from '../_components/Page'
import { fetchPage } from '../_reducers/page'

const mapDispatchToProps = {
    fetchPage: (pagePath) => fetchPage(pagePath)
};

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData ? state.page.pageData.page : null,
        pagePath: state.page.pageData ? state.page.pageData.path : null,
        fetchingPage: state.page.fetchingPage
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Page);
