import React, { Component } from 'react'
import { Link } from 'react-router'
import './WikiParagraph.scss'

export default class extends Component {

    static propTypes = {
        wikiObjects: React.PropTypes.array.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectKey = 0;
        return <p className='wikiParagraph'> {this.renderWikiObjects()} </p>;
    }

    renderWikiObjects() {
        return this.props.wikiObjects.map(this.renderWikiObject.bind(this));
    }

    renderWikiObject(wikiObject) {
        if (wikiObject.type === 'link') {
            return (
                <Link to={'/app/page/' + wikiObject.target}
                      className={'wikiLink' + (wikiObject.isBroken ? ' broken' : '')}
                      key={this.objectKey++}>
                    {wikiObject.text}
                </Link>
            );
        } else if (wikiObject.type === 'text') {
            return wikiObject.value;
        }
        return <p key={this.objectKey++}>Unidentified Wiki Object: {JSON.stringify(wikiObject)}</p>
    }
}