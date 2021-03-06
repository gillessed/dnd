import React, { Component } from 'react'
import { IndexLink, Link } from 'react-router'
import './Header.scss'
import SearchBar from '~/src/components/SearchBar';

class Header extends Component {
    constructor(props) {
        super(props);
        let user = this.props.users[this.props.session.userId];
        let isAdmin = false;
        if (user && user.content) {
            isAdmin = user.content.roles.indexOf('admin') >= 0;
        }
        this.state = {
            isAdmin
        };
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
                <a href={'/karyus-full.png'} className='item'>
                    <i className='map icon'/>
                    World Map
                </a>
                {this.renderAdminSubMenu()}
            </div>
        </div>

        <div className='right menu item'>
            <SearchBar customId='headerSearchBar'/>
        </div>
    </div>
</div>
        );
    }

    renderAdminSubMenu = () => {
        if (this.state.isAdmin) {
            return [
                <div key={1} className='divider'/>,
                <div key={2} className='item'>
                    <i className='dropdown icon'/>
                    <span className='text'>Admin</span>
                    <div className='menu'>
                        <a href={'/app/status'} className='item'>
                            <i className='bar chart icon'/>
                            Status
                        </a>
                        <a className='item' onClick={this.reloadAllPages}>
                            <i className='refresh icon'/>
                            Reload All
                        </a>
                    </div>
                </div>,
            ];
        }
    }

    reloadAllPages = () => {
        this.props.reloadAll();
    }

    hideSidebar() {
        $('#leftPageRail').css('z-index', '-1');
    }

    showSidebar() {
        $('#leftPageRail').css('z-index', '1');
    }
}

export default Header;
