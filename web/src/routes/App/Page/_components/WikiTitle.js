import React, { Component } from 'react'
import './WikiTitle.scss'

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired,
        status: React.PropTypes.string.isRequired,
        dm: React.PropTypes.bool.isRequired,
        setDm: React.PropTypes.func.isRequired,
        isAdmin: React.PropTypes.bool.isRequired,
        onReload: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <div style={{marginTop: 30, marginBottom: 10}}>
                    <span className='wikiTitle'>
                        {this.props.text}
                    </span>
                </div>
                {this.renderAdminBar()}
            </div>
        );
    }

    renderAdminBar() {
        if (this.props.isAdmin) {
            return (
                <div style={{display: 'flex', alignItems: 'center', marginBottom: 30}}>
                    <span style={{marginLeft: 10}} className='wikiStatus'>
                        {(this.props.status === 'DRAFT') ? ' (Draft)' : ''}
                    </span>
                    <button
                        style={{marginLeft: 20}}
                        className='ui red button'
                        onClick={this.props.onReload}>
                        Reload
                    </button>
                    <span style={{flexGrow: 1}}/>
                    <span style={{marginLeft: 30}} className='ui buttons'>
                        <button
                            className={'ui button' + (this.props.dm ? '' : ' active')}
                            onClick={() => this.props.setDm(false)}>
                            Game
                        </button>
                        <button
                            className={'ui button' + (this.props.dm ? ' active' : '')}
                            onClick={() => this.props.setDm(true)}>
                            DM
                        </button>
                    </span>
                </div>
            );
        }
    }
}