import React, { Component } from 'react'

export default class extends Component {

    static propTypes = {
        path: React.PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        var sections = this.props.path.split('_');
        var link = '';
        var sectionObjects = [];
        sections.forEach((section, index) => {
            if (link == '') {
                link = link + section;
            } else {
                link = link + '_' + section;
            }
            if (index < sections.length - 1) {
                sectionObjects.push({
                    section,
                    link
                });
                sectionObjects.push('divider');
            } else {
                sectionObjects.push({
                    section,
                    link,
                    active: true
                })
            }
        });
        return (
            <div className='ui breadcrumb'>
                {sectionObjects.map(this.renderSection)}
            </div>
        );
    }

    renderSection(section) {
        if (section == 'divider') {
            return <i className='right angle icon divider'/>;
        } else if (section.active) {
            return <div className='active section'>{section.section}</div>;
        } else {
            return <a className='section'>{section.section}</a>;
        }
    }

}
