import React, { Component } from 'react'
import { Link } from 'react-router'

export default class extends Component {

    static propTypes = {
        path: React.PropTypes.string,
        parentPaths: React.PropTypes.array,
        pageData: React.PropTypes.object
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectKey = 0;
        let sections = this.props.path.split('_');
        let link = '';
        let sectionObjects = [];
        let parentPathIndex = 0;
        sections.forEach((section, index) => {
            if (link == '') {
                link = link + section;
            } else {
                link = link + '_' + section;
            }
            if (index < sections.length - 1) {
                sectionObjects.push(this.props.parentPaths[parentPathIndex++]);
                sectionObjects.push('divider');
            } else {
                sectionObjects.push({
                    active: true,
                    title: this.props.pageData.title
                })
            }
        });
        return (
            <div className='ui breadcrumb'>
                {sectionObjects.map(this.renderSection.bind(this))}
            </div>
        );
    }

    renderSection(section) {
        if (section == 'divider') {
            return <i key={this.objectKey++} className='right angle icon divider'/>;
        } else if (section.active) {
            return <div key={this.objectKey++} className='active section'>{section.title}</div>;
        } else {
            console.log(section);

            return (
                <Link key={this.objectKey++}
                         to={'/app/page/' + section.path}
                         className='directoryLink'>
                    {section.title}
                </Link>
            );
        }
    }
}
