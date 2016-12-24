import React from 'react'
import './LeftPageSidebar.scss'

class LeftPageSidebar extends React.Component {
    static propTypes = {
        renderDmContent: React.PropTypes.bool.isRequired
    }

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        $('#leftPageRail').sticky({
            context: '.main.text.container',
            offset: 100
        });
        this.scrollToTop(false);
    }

    render() {
        this.computeActiveLink();
        return (
<div className='ui internal left rail'>
    <div id='leftPageRail'  className='ui sticky'>
        <div className='ui fixed segment'>
            <h1>Contents</h1>
            {this.renderToTopButton()}
            <ol className='ui list'>
                {this.renderList()}
            </ol>
        </div>
    </div>
</div>
        );
    }

    renderToTopButton() {
        return (
            <div>
                <i className='ui angle up icon'/>
                <a className='sectionLink notActive' onClick={() => {this.scrollToTop()}}>
                    To Top
                </a>
            </div>
        );
    }

    computeActiveLink() {
        let max = Math.max(...Object.keys(this.props.sectionVisibility));
        this.activeSection = 1;
        while (!this.props.sectionVisibility[this.activeSection] &&
                this.activeSection <= max) {
            this.activeSection++;
        }
    }

    renderList = () => {
        if (this.props.renderDmContent) {
            return this.props.pageData.dmContent.content.map(this.renderListItem);
        } else {
            return this.props.pageData.pageContent.content.map(this.renderListItem)
        }
    }

    renderListItem = (wikiSection) => {
        let visible = wikiSection.index == this.activeSection;
        let classes = 'sectionLink' + (visible ? ' active' : ' notActive');
        return (
            <li key={wikiSection.index}>
                <a className={classes} onClick={() => {this.handleOnClick(wikiSection.index)}}>
                    {wikiSection.text}
                </a>
            </li>
        );
    }

    scrollToTop(animated = true) {
        if (animated) {
            $('html, body').animate({
                scrollTop: 0
            }, 200);
        } else {
            $('html, body').scrollTop(0);
        }
    }

    handleOnClick(sectionIndex) {
        if (sectionIndex != this.activeSection) {
            $('html, body').animate({
                scrollTop: $('#section_' + sectionIndex).offset().top - 70
            }, 200);
        }
    }
}

export default LeftPageSidebar;
