import React, { Component }  from 'react'
import Header from '../_containers/Header'
import Notifications from '../_containers/Notifications'
import './CoreLayout.scss'
import '../../styles/core.scss'

class CoreLayout extends Component {
    static propTypes = {
        children : React.PropTypes.element.isRequired
    };

    constructor(props) {
        super(props);
    }
    render() {
        return (
<div>
    <Header/>
    <div className='ui main text container'>
        <Notifications/>
        {this.props.children}
    </div>
</div>
        );
    }
}

export default CoreLayout;
