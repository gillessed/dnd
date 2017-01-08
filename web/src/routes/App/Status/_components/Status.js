import React, { Component } from 'react'
import { Link } from 'react-router'

class Status extends Component {
    static propTypes = {
        status: React.PropTypes.object.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        this.objectNumber = 0;
        return (
            <div style={{ margin: '0 auto'}}>
                <h1>Wiki Status</h1>
                {this.renderPageTable()}
                {this.renderLinkTable()}
                {this.renderLoader()}
            </div>
        );
    }

    renderPageTable() {
        if (!this.props.status.content) {
            return;
        }
        return (
            <table className='ui celled table'>
                <thead>
                    <th>Page Status</th>
                    <th>Count</th>
                </thead>
                <tbody>
                <tr>
                    <td>Draft</td>
                    <td>{this.props.status.content.draftPageCount}</td>
                </tr>
                <tr>
                    <td>Published</td>
                    <td>{this.props.status.content.publishedPageCount}</td>
                </tr>
                <tr>
                    <td style={{fontWeight: 'bold'}}>Total</td>
                    <td style={{fontWeight: 'bold'}}>{this.props.status.content.totalPageCount}</td>
                </tr>
                </tbody>
            </table>
        );
    }

    renderLinkTable() {
        if (!this.props.status.content) {
            return null;
        }
        return (
            <table className='ui celled table'>
                <thead>
                <th>Link Status</th>
                <th>Count</th>
                </thead>
                <tbody>
                <tr>
                    <td>Broken</td>
                    <td>{this.props.status.content.brokenLinkCount}</td>
                </tr>
                <tr>
                    <td style={{fontWeight: 'bold'}}>Total</td>
                    <td style={{fontWeight: 'bold'}}>{this.props.status.content.linkCount}</td>
                </tr>
                </tbody>
            </table>
        );
    }

    renderLoader() {
        if (this.props.status.loading) {
            return (
                <div className='ui active dimmer'>
                    <div className='ui large text loader'>Loading</div>
                </div>
            );
        }
    }
}

export default Status;
