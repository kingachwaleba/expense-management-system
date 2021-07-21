import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';
import { Button, Form, FormGroup, Input, Label, Row, Col } from "reactstrap";

class RegisterPage extends React.Component {

    constructor(props, context) {
        super(props, context);

        if (UserService.getCurrentUser()) {
            this.props.history.push('/home');
        }

        this.state = {
            user: new User('', '', '', '', ''),
            submitted: false,
            loading: false,
            errorMessage: '',
        };
    }

    handleChange(e) {
        var {name, value} = e.target;
        var user = this.state.user;

        user[name] = value;
        this.setState({user: user});
    }

    handleRegister(e) {
        e.preventDefault();
        this.setState({submitted: true});
        const {user} = this.state;

        //validate form
        if (!user.login || !user.password || !user.email || !user.image) {
            return;
        }

        this.setState(({loading: true}));
        UserService.register(user)
            .then(data => {
                    this.props.history.push('/login');
                },
                error => {
                    if (error && error.response && error.response.status === 409) {
                        this.setState({
                            errorMessage: 'This login is not available.',
                            loading: false,
                        });
                    } else {
                        this.setState({
                            errorMessage: 'Unexpected error occurred.',
                            loading: false,
                        });
                    }
                });
    }

    render() {
        const {user, submitted, loading, errorMessage} = this.state;
        return (
            <div className="register-page">
                <div className="card">
                    <div className="header-container">
                        <i className="fa fa-user"/>
                    </div>

                    {errorMessage &&
                    <div className="alert alert-danger" role="alert">
                        {errorMessage}
                    </div>
                    }

                    <form
                        name="form"
                        method="post"
                        onSubmit={(e) => this.handleRegister(e)}>
                        <div className={'form-group'}>
                            <label htmlFor="login">Login: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="login"
                                placeholder="Login"
                                required
                                value={user.login}
                                onChange={(e) => this.handleChange(e)}/>
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
                                value={user.password}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label htmlFor="email">Email: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="email"
                                placeholder="Email"
                                required
                                value={user.email}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                A valid email is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label htmlFor="image">Image: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="image"
                                placeholder="image"
                                value={user.image}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                A valid image is required.
                            </div>
                        </div>

                        <button
                            className="btn btn-primary btn-block"
                            onClick={() => this.setState({submitted: true})}
                            disabled={loading}>
                            Sign Up
                        </button>
                    </form>
                    <a href="/login" className="card-link">I have an Account!</a>
                </div>
            </div>
        );
    }
}

export {RegisterPage};