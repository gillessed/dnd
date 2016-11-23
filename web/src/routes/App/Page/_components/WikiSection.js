import React, { Component } from 'react'
import WikiHeading from './WikiHeading'
import WikiParagraph from './WIkiParagraph'
import WikiUnorederedList from './WIkiUnorderedList'

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired,
        sectionNumber: React.PropTypes.number.isRequired,
        wikiObjects: React.PropTypes.array.isRequired
    };

    constructor(props) {
        super(props);
        this.id = 'section_' + this.props.sectionNumber;
    }

    componentDidMount() {
        $('#' + this.id).visibility({
            onUpdate: (calculations) => {
                this.props.setSectionVisible(
                    this.props.sectionNumber,
                    calculations.onScreen && calculations.height - calculations.pixelsPassed > 70);
            }
        });
    }

    render() {
        this.objectKey = 0;
        return (
            <div id={this.id} style={{marginBottom: '20px'}}>
                <h2 style={{borderBottom: '1px solid black'}}>
                    {this.props.sectionNumber}. {this.props.text}
                </h2>
                {this.props.wikiObjects.map(this.renderSectionObject.bind(this))}
            </div>
        );
    }

    renderSectionObject(wikiObject) {
        if (wikiObject.type == 'heading') {
            return <WikiHeading
                text={wikiObject.text}
                level={wikiObject.level}
                key={this.objectKey++}/>
        } else if (wikiObject.type == 'unorderedList') {
            return <WikiUnorederedList
                wikiObjects={wikiObject.listItems}
                key={this.objectKey++}/>
        } else if (wikiObject.type == 'paragraph') {
            return <WikiParagraph
                wikiObjects={wikiObject.wikiObjects}
                key={this.objectKey++}/>
        }
        return <p key={this.objectKey++}>Unidentified Wiki Object: {JSON.stringify(wikiObject)}</p>
    }
}
