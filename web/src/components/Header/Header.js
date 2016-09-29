import React from 'react'
import { IndexLink, Link } from 'react-router'
import './Header.scss'

export const Header = () => (
    <div className='ui fixed inverted menu'>
        <div className='ui container'>
            <IndexLink to='/' className='header item' activeClassName='route--active'>
                DnD
            </IndexLink>
            <Link to='/page' className='header item' activeClassName='route--active'>
                Wiki
            </Link>
            <Link to='/creator' className='header item' activeClassName='route--active'>
                Charater Creator
            </Link>
        </div>
    </div>
)

export default Header
