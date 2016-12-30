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
        this.objectKey = 1;
        return <div className='item wikiParagraph'> {this.renderWikiObjects()} </div>;
    }

    renderWikiObjects() {
        return this.props.wikiObjects.map(this.renderWikiObject.bind(this));
    }

    renderWikiObject(wikiObject) {
        if (wikiObject.type === 'link') {
            return (
                <Link
                        to={'/app/page/' + wikiObject.target}
                        className={'wikiLink' + (wikiObject.target ? '' : ' broken')}
                        key={this.objectKey++}>
                    {wikiObject.displayText}
                </Link>
            );
        } else if (wikiObject.type === 'text') {
            return wikiObject.value;
        }
        return <li key={this.objectKey++}>Unidentified Wiki Object: {JSON.stringify(wikiObject)}</li>
    }
}
