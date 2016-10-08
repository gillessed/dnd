import React, { Component }  from 'react'

class Notifications extends Component {
    static propTypes = {
        notifications: React.PropTypes.array.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectKey = 0;
        let notificationComponents = this.props.notifications.map(this.renderNotification.bind(this));
        return (
            <div style={{marginBottom: "20px"}}>
                {notificationComponents}
            </div>
        );
    }

    renderNotification(notification) {
        return (
<div key={this.objectKey++} className='ui floating error message'>
    <i className='close icon' onClick={(event) => {this.onClose(event, notification) }}/>
    <div className='header'>
        {notification.header}
    </div>
    <p>{notification.message}</p>
</div>
        );
    }

    onClose(event, notification) {
        $(event.target)
            .closest('.message')
            .transition({
                animation: 'fade',
                duration: '500ms',
                onComplete: () => {
                    this.props.clearNotification(notification);
                }
            });
    }
}

export default Notifications;
