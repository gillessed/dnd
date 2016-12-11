import React, { Component } from 'react'
import { IndexLink, Link } from 'react-router'
import './Header.scss'
import HomeIcon from '../_assets/home_icon.png'
import SearchBar from '~/src/components/SearchBar';

class Header extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        $('.fixed.menu .container .dropdown.item').dropdown({
            action: 'hide',
            onChange: () => {
                this.showSidebar();
            }
        }).dropdown('setting', 'onShow', () => {
            this.hideSidebar()
        }).dropdown('setting', 'onHide', () => {
            this.showSidebar()
        });
    }

    render() {
        return (
<div className='ui fixed borderless menu'>
    <div className='ui container'>
        <div className='ui pointing link dropdown header item'>
            <i className='content icon'/>
            <span className='text'>Tabletop</span>
            <div className='menu' id="mainDropdown">
                <IndexLink to={this.props.session ? '/app' : '/'} className='item'>
                    <i className='home icon'/>
                    Home
                </IndexLink>
                <Link to='/app/wiki' className='item'>
                    <i className='world icon'/>
                    Wiki
                </Link>
                <Link to='/app/creator' className='item'>
                    <i className='user icon'/>
                    Charater Creator
                </Link>
                <a href={SERVER_ASSET_URL + "/karyus-full.png"} className='item'>
                    <i className='map icon'/>
                    World Map
                </a>
            </div>
        </div>

        <div className='right menu item'>
            <SearchBar customId='headerSearchBar'/>
        </div>
    </div>
</div>
        );
    }

    hideSidebar() {
        $('#leftPageRail').css('z-index', '-1');
    }

    showSidebar() {
        $('#leftPageRail').css('z-index', '1');
    }
}

export default Header;
