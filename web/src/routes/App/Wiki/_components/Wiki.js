import React, { Component } from 'react'
import { IndexLink, Link } from 'react-router'
import { browserHistory } from 'react-router';
import SearchBar from '~/src/components/SearchBar';

class Wiki extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            enableLogin: false
        }
    }

    render() {
        return (
<div style={{ margin: '0 auto' }}>
    <h1 className='ui header'>Wiki</h1>
    <div className='ui grid'>
        <SearchBar customClasses='sixteen wide column' customId='wikiSearchBar'/>
    </div>
    <div className='ui three column grid'>
        <div className='column'>
            <h2><i className='world icon'/>Places</h2>
            <div className='ui link list'>
                <Link to='/app/page/places' className='item'>Karyus</Link>
                <Link to='/app/page/places_generos_elfora' className='item'>Elfora</Link>
                <Link to='/app/page/places_generos_ippanunga' className='item'>Ippanunga</Link>
            </div>
        </div>
        <div className='column'>
            <h2><i className='user icon'/>People</h2>
            <p>Column Two</p>
        </div>
        <div className='column'>
            <h2><i className='book icon'/>Events</h2>
            <p>Column Three</p>
        </div>
    </div>
</div>
        );
    }
}

export default Wiki;
