import React, { Component } from 'react'
import WikiTitle from './WikiTitle'
import WikiSection from './WikiSection'
import WikiHeading from './WikiHeading'
import WikiParagraph from './WIkiParagraph'
import './Page.scss'

export class Page extends Component {
    static propTypes = {
        fetchingPage: React.PropTypes.bool.isRequired
    };

    constructor(props) {
        super(props);
        this.objectNumber = 0;
        this.sectionNumber = 1;
    }

    render() {
        this.objectNumber = 0;
        this.sectionNumber = 1;
        return (
<div style={{ margin: '0 auto' }}>
    {this.renderWikiObjects()}
    {this.renderLoader()}
</div>
        );
    }

    renderWikiObjects() {
        if (this.props.pageData) {
            return this.props.pageData.wikiObjects.map(this.renderWikiObject.bind(this));
        } else {
            return null;
        }
    }

    renderWikiObject(wikiObject) {
        if (wikiObject.type === 'title') {
            return <WikiTitle
                text={wikiObject.text}
                key={this.objectNumber++}/>
        } else if (wikiObject.type == 'section') {
            return <WikiSection
                text={wikiObject.text}
                reference={wikiObject.ref}
                sectionNumber={this.sectionNumber++}
                key={this.objectNumber++}/>
        } else if (wikiObject.type == 'heading') {
            return <WikiHeading
                text={wikiObject.text}
                level={wikiObject.level}
                key={this.objectNumber++}/>
        } else if (wikiObject.type == 'paragraph') {
            return <WikiParagraph
                wikiObjects={wikiObject.wikiObjects}
                key={this.objectNumber++}/>
        }
        return <p key={this.objectNumber++}>Unidentified Wiki Object: {JSON.stringify(wikiObject)}</p>
    }

    renderLoader() {
        if (!this.props.fetchingPage) {
            return null;
        } else {
            return (
                <div className='ui active dimmer'>
                    <div className='ui large text loader'>Loading</div>
                </div>
            );
        }
    }
}

export default Page;
