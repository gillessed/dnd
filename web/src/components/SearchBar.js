import React, { Component } from 'react'
import Fetcher from '~/src/network/networker'
import { browserHistory } from 'react-router';

class SearchBar extends Component {
    static propTypes = {
        customClasses: React.PropTypes.string,
        customId: React.PropTypes.string.isRequired
    };

    constructor(props) {
        super(props);
        this.state = {
            searchQuery: ''
        }
    }

    componentDidMount() {
        $('#' + this.props.customId).search({
            apiSettings: {
                responseAsync: (settings, callback) => {
                    Fetcher.sessionFetch('/search', {
                        method: 'POST',
                        body: JSON.stringify({query: settings.urlData.query})
                    }).then((json) => {
                        callback({
                            results: json.searchResults
                        });
                    });
                }
            },
            maxResults: 10,
            minCharacters: 1,
            onSelect: (result) => {
                browserHistory.push('/app/page/' + result.target);
                return true;
            }
        });
    }

    render() {
        return (
<div id={this.props.customId} className={'ui search ' + this.props.customClasses} style={{width: '100%'}}>
    <div className='ui icon input' style={{width: '100%'}}>
        <input
            className='prompt'
            style={{width: '100%'}}
            type='text'
            placeholder='Search...'
            value={this.state.searchQuery}
            onChange={this.handleOnChange.bind(this)}
            onBlur={this.handleOnBlur.bind(this)}/>
        <i className='search icon'/>
    </div>
    <div className='results'></div>
</div>
        );
    }

    handleOnChange(event) {
        this.setState({
            searchQuery: event.target.value
        });
    }

    handleOnBlur() {
        this.setState({
            searchQuery: ''
        });
    }
}

export default SearchBar;
