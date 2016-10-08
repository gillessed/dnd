import { connect } from 'react-redux'
import Notifications from '../_components/Notifications'
import { clearNotification } from '~/src/store/globals/notifications'

const mapDispatchToProps = {
    clearNotification: (notification) => clearNotification(notification )
};

const mapStateToProps = (state) => {
    return {
        notifications: state.globals.notifications
    }
};

export default connect(mapStateToProps, mapDispatchToProps)(Notifications);
