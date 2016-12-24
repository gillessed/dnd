import React, { Component } from 'react'
import './WikiTitle.scss'

export default class extends Component {

    static propTypes = {
        text: React.PropTypes.string.isRequired,
        status: React.PropTypes.string.isRequired,
        dm: React.PropTypes.bool.isRequired,
        setDm: React.PropTypes.func.isRequired
    };

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <p style={{display: 'flex', alignItems: 'center', marginTop: 30, marginBottom: 30}}>
                <span className='wikiTitle'>
                    {this.props.text}
                </span>
                <span style={{marginLeft: 10}} className='wikiStatus'>
                    {(this.props.status === 'DRAFT') ? ' (Draft)' : ''}
                </span>
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
            </p>
        );
    }
}