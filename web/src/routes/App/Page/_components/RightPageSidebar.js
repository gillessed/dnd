import React from 'react'
import { Link } from 'react-router'
import './RightPageSidebar.scss'

class RightPageSidebar extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        $('#rightPageRail').sticky({
            context: '.main.text.container',
            offset: 100
        });
    }

    render() {
        this.objectKey = 0;
        return (
<div className='ui internal right rail'>
    <div id='rightPageRail'  className='ui sticky'>
        <div className='ui fixed segment'>
            <h1>Pages</h1>
            <div className='ui relaxed divided list'>
                {this.renderDirectoryLinks()}
            </div>
        </div>
    </div>
</div>
        );
    }

    renderDirectoryLinks() {
        if (this.props.directoryEntries) {
            return this.props.directoryEntries.map(this.renderDirectoryLink.bind(this));
        } else {
            return null;
        }
    }

    renderDirectoryLink(directory) {
        if (this.props.pagePath == directory.path) {
            return (
                <div key={this.objectKey++} className='item'>
                    <i className='ui angle left icon'/>
                    <div className='content directoryItemContent active'>
                        <div className='contentHeader'>{directory.title}</div>
                    </div>
                </div>
            );
        } else {
            return (
                <div key={this.objectKey++} className='item notActive'>
                    <i className='ui angle right icon'/>
                    <Link to={'/app/page/' + directory.path} className='content directoryItemContent notActive'>
                        <div className='contentHeader'>{directory.title}</div>
                    </Link>
                </div>
            );
        }
    }
}

export default RightPageSidebar;
