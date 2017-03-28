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
<div id='fullPage'>
    <Header/>
    <div className='ui main text very relaxed container bound bottom'>
        <Notifications/>
        {this.props.children}
        {this.renderLoader()}
    </div>
</div>
        );
    }

    renderLoader() {
        if (this.props.reload) {
            return (
                <div className='ui active inverted dimmer'>
                    <div className='ui massive text loader'>Resyncing Pages</div>
                </div>
            );
        }
    }
}

export default CoreLayout;
