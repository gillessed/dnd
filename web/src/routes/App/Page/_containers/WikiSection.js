import { connect } from 'react-redux'

import WikiSection from '../_components/WikiSection'
import * as page from '../_reducers/page'

const mapDispatchToProps = {
    setSectionVisible: (section, visible) => page.actions.setSectionVisible(section, visible)
};

const mapStateToProps = () => {
    return {};
};

export default connect(mapStateToProps, mapDispatchToProps)(WikiSection);
