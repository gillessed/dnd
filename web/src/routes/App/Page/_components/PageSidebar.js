import React from 'react'
import './PageSidebar.scss'

class PageSidebar extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        $('#leftPageRail').sticky({
            context: '.main.text.container',
            offset: 100
        });
    }

    render() {
        this.computeActiveLink();
        return (
<div className='ui left internal rail'>
    <div id='leftPageRail'  className='ui sticky'>
        <div className='ui fixed segment'>
            <h1>Contents</h1>
            <ol className='ui list'>
                {this.props.pageData.wikiSections.map(this.renderListItem.bind(this))}
            </ol>
        </div>
    </div>
</div>
        );
    }

    computeActiveLink() {
        this.activeSection = 1;
        while (!this.props.sectionVisibility[this.activeSection]) {
            this.activeSection++;
        }
    }

    renderListItem(wikiSection) {
        let visible = wikiSection.index == this.activeSection;
        let classes = 'sectionLink' + (visible ? ' active' : ' notActive');
        return (
            <li key={wikiSection.index}>
                <a href='#' className={classes} onClick={() => {this.handleOnClick(wikiSection.index)}}>
                    {wikiSection.text}
                </a>
            </li>
        );
    }

    handleOnClick(sectionIndex) {
        if (sectionIndex != this.activeSection) {
            $('html, body').animate({
                scrollTop: $('#section_' + sectionIndex).offset().top - 70
            }, 200);
            //console.log(sectionIndex);
        }
    }
}

export default PageSidebar;