import { connect } from 'react-redux'

import RightPageSidebar from '../_components/RightPageSidebar'

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData ? state.page.pageData.page : null,
        directoryEntries: state.page.pageData ? state.page.pageData.directoryEntries : null,
        pagePath: state.page.pageData ? state.page.pageData.path : null,
        fetchingPage: state.page.fetchingPage
    }
};

export default connect(mapStateToProps)(RightPageSidebar);
