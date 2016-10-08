import React, { Component } from 'react'
import { Link } from 'react-router'

export class Main extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
<div>
    <h1 className='ui header'>Home</h1>
    <p> Welcome to the main page of the application.</p>
    <Link to='/app/wiki'>Wiki</Link>
</div>
        );
    }
}

export default Main;
