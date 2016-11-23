import { connect } from 'react-redux'

import LeftPageSidebar from '../_components/LeftPageSidebar'

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData ? state.page.pageData.page : null,
        fetchingPage: state.page.fetchingPage,
        sectionVisibility: state.page.sectionVisibility
    }
};

export default connect(mapStateToProps)(LeftPageSidebar);
