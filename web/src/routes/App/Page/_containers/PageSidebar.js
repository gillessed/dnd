import { connect } from 'react-redux'

import PageSidebar from '../_components/PageSidebar'

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData,
        fetchingPage: state.page.fetchingPage,
        sectionVisibility: state.page.sectionVisibility
    }
};

export default connect(mapStateToProps)(PageSidebar);
