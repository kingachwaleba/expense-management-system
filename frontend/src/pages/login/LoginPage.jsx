import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';

class LoginPage extends React.Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            login: "",
            password: "",
            error: ""
        };
    }

    changeHandler = (event) => {
        let nam = event.target.name;
        let val = event.target.value;
        this.setState({[nam]: val});
    };

    doLogin = async (event) => {
        event.preventDefault();

        UserService
            .login(this.state.login,
                this.state.password)
            .then(
                () => {
                    this.props.history.push('/profile');
                    window.location.reload();
                },
                error => {
                    console.log("Login fail: error = { " + error.toString() + " }");
                    this.setState({error: "Can not signin successfully! Please check login/password again"});
                }
            );
    };

    render() {
        return (
            <div className="login-page">
                <div className="card">
                    <div className="header-container">
                        <i className="fa fa-user"/>
                    </div>

                    <form
                        name="form"
                        method="post"
                        onSubmit={this.doLogin}>
                        <div className={'form-group'}>
                            <label htmlFor="username">Login: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="login"
                                placeholder="Login"
                                required
                                value={this.state.login}
                                onChange={this.changeHandler}/>
                            <div className="invalid-feedback">
                                A valid login is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label htmlFor="Password">Password: </label>
                            <input
                                type="password"
                                className="form-control"
                                name="password"
                                placeholder="Password"
                                required
                                value={this.state.password}
                                onChange={this.changeHandler}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>

                        <button type="submit">
                            Login
                        </button>
                        {
                            this.state.error && (
                                <alert color="danger">
                                    {this.state.error}
                                </alert>
                            )
                        }
                    </form>
                    <a href="/register" className="card-link">Create New Account!</a>
                </div>
            </div>
        );
    }
}

export {LoginPage};