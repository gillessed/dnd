import React, { Component } from 'react'
import { Link } from 'react-router'

class Status extends Component {
    static propTypes = {
        status: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectNumber = 0;
        return (
            <div style={{ margin: '0 auto'}}>
                <p>Some status</p>
                {this.renderLoader()}
            </div>
        );
    }

    renderLoader() {
        if (this.props.status.loading) {
            return (
                <div className='ui active dimmer'>
                    <div className='ui large text loader'>Loading</div>
                </div>
            );
        }
    }
}

export default Status;
