import React, { Component } from 'react';

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired,
        level: React.PropTypes.number.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        if (this.props.level == 1) {
            return <h3>{this.props.text}</h3>;
        } else if (this.props.level == 2) {
            return <h4>{this.props.text}</h4>;
        } else if (this.props.level == 3) {
            return <h5>{this.props.text}</h5>;
        } else if (this.props.level == 4) {
            return <h6>{this.props.text}</h6>;
        } else {
            throw Error('level ' + this.props.level + ' does not exist')
        }
    }
}