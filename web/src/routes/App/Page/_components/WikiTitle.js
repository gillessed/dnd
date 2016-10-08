import React, { Component } from 'react'

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        return <h1>{this.props.text}</h1>;
    }
}