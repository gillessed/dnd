import React, { Component } from 'react'

export class Login extends Component {

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
    <h1 className='ui header'>Login</h1>
    <p> Login to access my awesome awesome DnD resources.</p>
    <div className='ui segment'>
        <form className='ui form'>
            <div className='ui field'>
                <label>Username</label>
                <div className='ui input'>
                    <input type='text'
                           className='ui input'
                           placeholder='Username'
                           onChange={this.handleUsernameChange.bind(this)}
                           onKeyPress={this.handleKeyPress.bind(this)}/>
                </div>
            </div>
            <div className='ui field'>
                <label>Password</label>
                <div className='ui input'>
                    <input type='password'
                           className='ui input'
                           placeholder='Password'
                           onChange={this.handlePasswordChange.bind(this)}
                           onKeyPress={this.handleKeyPress.bind(this)}/>
                </div>
            </div>
            <div className={'ui primary button'
             + (this.state.enableLogin ? '' : ' disabled')
             + (this.props.loggingIn ? ' loading' : '')}
                 onClick={this.handleLogin.bind(this)}>
                Login
            </div>
        </form>
    </div>
</div>
        );
    }

    handleUsernameChange(event) {
        let enableLogin = event.target.value.length > 0 && this.state.password.length > 0;
        this.setState({
            username: event.target.value,
            enableLogin
        });
    }

    handlePasswordChange(event) {
        let enableLogin = this.state.username.length > 0 && event.target.value.length > 0;
        this.setState({
            password: event.target.value,
            enableLogin
        });
    }

    handleKeyPress(event) {
        if (event.key === 'Enter') {
            this.handleLogin();
        }
    }

    handleLogin() {
        this.props.login(this.state.username, this.state.password);
    }
}

export default Login;
