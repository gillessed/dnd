import React, { Component } from 'react'

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
                <a style={{color: '#0000ff'}}
                    key={this.objectNumber++}>
                    {wikiObject.text}
                </a>
            );
        } else if (wikiObject.type === 'text') {
            return wikiObject.value;
        }
        return <p key={this.objectNumber++}>Unidentified Wiki Object: {JSON.stringify(wikiObject)}</p>
    }
}