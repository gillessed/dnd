import { connect } from 'react-redux'

import Page from '../_components/Page'
import { fetchPage } from '../_reducers/page'

const mapDispatchToProps = {
    fetchPage: (pagePath) => fetchPage(pagePath)
};

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData,
        fetchingPage: state.page.fetchingPage
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Page);
