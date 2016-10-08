import React, { Component } from 'react'

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired,
        reference: React.PropTypes.string.isRequired,
        sectionNumber: React.PropTypes.number.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <h2 style={{borderBottom: '1px solid black'}}>
                {this.props.sectionNumber}. {this.props.text}
            </h2>
        );
    }
}