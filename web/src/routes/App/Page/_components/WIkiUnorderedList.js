import React, { Component } from 'react'
import { Link } from 'react-router'
import WikiListItem from './WIkiListItem'

export default class extends Component {

    static propTypes = {
        wikiObjects: React.PropTypes.array.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectKey = 0;
        return <div className='ui bulleted list wikiUnorderedList'> {this.renderWikiObjects()} </div>;
    }

    renderWikiObjects() {
        return this.props.wikiObjects.map(this.renderWikiObject.bind(this));
    }

    renderWikiObject(wikiObject) {
        return (
            <WikiListItem
                wikiObjects={wikiObject.wikiObjects}
                key={this.objectKey++}/>
        );
    }
}
