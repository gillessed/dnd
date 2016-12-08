import { connect } from 'react-redux'

import WikiPath from '../_components/WikiPath'

const mapDispatchToProps = {
};

const mapStateToProps = (state) => {
    return {
        pageData: state.page.pageData ? state.page.pageData.page : null,
        path: state.page.pageData ? state.page.pageData.path : null,
        parentPaths: state.page.pageData ? state.page.pageData.parentPaths : null
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(WikiPath);
