import React from 'react'
import { IndexLink, Link } from 'react-router'

export const HomeView = () => (
    <div>
        <h1 className='ui header'>Welcome to my DnD page!</h1>
        <p> This site contains some helpful pages for dungeons and dragons and my upcoming campaign!</p>
        <Link to='/login' className='ui primary button'>Login</Link>
    </div>
);

export default HomeView;
