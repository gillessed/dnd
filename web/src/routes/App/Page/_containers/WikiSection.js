import { connect } from 'react-redux'

import WikiSection from '../_components/WikiSection'
import { setSectionVisible } from '../_reducers/page'

const mapDispatchToProps = {
    setSectionVisible: (section, visible) => setSectionVisible(section, visible)
};

const mapStateToProps = () => {
    return {};
};

export default connect(mapStateToProps, mapDispatchToProps)(WikiSection);
