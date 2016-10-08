import React, { Component } from 'react'
import { IndexLink, Link } from 'react-router'
import './Header.scss'
import HomeIcon from '../_assets/home_icon.png'
import SearchBar from '~/src/components/SearchBar';

class Header extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
<div className='ui fixed borderless menu'>
    <div className='ui container'>
        <IndexLink to={this.props.session ? '/app' : '/'} className='header item'>
            <img className='logo'
                 src={HomeIcon}/>
            Home
        </IndexLink>
        <Link to='/app/wiki' className='header item'>
            Wiki
        </Link>
        <Link to='/app/creator' className='header item'>
            Charater Creator
        </Link>

        <div className='right menu header item'>
            <SearchBar customId='headerSearchBar'/>
        </div>
    </div>
</div>
        );
    }
}

export default Header;
