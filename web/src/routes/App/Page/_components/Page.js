import React, { Component } from 'react'
import { Link } from 'react-router'
import WikiTitle from './WikiTitle'
import WikiSection from '../_containers/WikiSection'
import PageSidebar from '../_containers/PageSidebar'
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
            <div id='pageContainer'
                 style={{ margin: '0 auto' }}>
                {this.renderSidebar()}
                <div id='pageContent'>
                    {this.renderWikiTitle()}
                    {this.renderWikiSections()}
                    {this.renderLoader()}
                </div>
            </div>
        );
    }

    renderSidebar() {
        if (this.props.pageData) {
            return <PageSidebar/>
        } else {
            return null;
        }
    }

    renderWikiTitle() {
        if (this.props.pageData) {
            return <WikiTitle text={this.props.pageData.titleObject.text}/>
        }
    }

    renderWikiSections() {
        if (this.props.pageData) {
            return this.props.pageData.wikiSections.map(this.renderWikiSection.bind(this));
        } else if (!this.props.fetchingPage) {
            return <p>Click <Link to='/app/wiki'>here</Link> to return to the main wiki page.</p>;
        }
    }

    renderWikiSection(wikiSection) {
        return <WikiSection
            text={wikiSection.text}
            wikiObjects={wikiSection.wikiObjects}
            sectionNumber={this.sectionNumber++}
            key={this.objectNumber++}/>
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
