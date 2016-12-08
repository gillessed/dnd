import React, { Component } from 'react'
import { Link } from 'react-router'
import WikiTitle from './WikiTitle'
import WikiSection from '../_containers/WikiSection'
import WikiPath from '../_containers/WikiPathContainer'
import LeftPageSidebar from '../_containers/LeftPageSidebar'
import RightPageSidebar from '../_containers/RightPageSidebar'
import './Page.scss'

export class Page extends Component {
    static propTypes = {
        fetchingPage: React.PropTypes.bool.isRequired,
        pageData: React.PropTypes.object
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
                 style={{ margin: '0 auto'}}>
                {this.renderLeftSidebar()}
                {this.renderRightSidebar()}
                <div id='pageContent'>
                    {this.renderWikiPath()}
                    {this.renderWikiTitle()}
                    {this.renderWikiSections()}
                    {this.renderLoader()}
                </div>
            </div>
        );
    }

    renderWikiPath() {
        if (this.props.pagePath) {
            return <WikiPath/>
        } else {
            return null;
        }
    }

    renderLeftSidebar() {
        if (this.props.pageData) {
            return <LeftPageSidebar/>
        } else {
            return null;
        }
    }

    renderRightSidebar() {
        if (this.props.pageData) {
            return <RightPageSidebar/>
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
