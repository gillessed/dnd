import React, { Component } from 'react';
import { Link } from 'react-router'

export default class extends Component {
    render() {
        this.objectKey = 0;
        return (
            <div>
                <h3>{this.props.title}</h3>
                <ul>
                    {this.props.children.map(this.renderIndexItem)}
                </ul>
            </div>
        );
    }

    renderIndexItem = (target) => {
        return (
            <li key={this.objectKey++}>
                <Link
                    to={'/app/page/' + target.target}
                    className={'wikiLink'}>
                    {target.title}
                </Link>
            </li>
        );
    }
}