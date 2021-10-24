import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';
import { Button, Form, FormGroup, Input, Label, Row, Col } from "reactstrap";
import Header from '../../components/Header';

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
        if (!user.login || !user.password || !user.email) {
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
                 <Header title='Zarejestruj się' />
                <div className="form-container">
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
                            <label className="form-label text-size"  htmlFor="login">Login: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="login"
                                placeholder=""
                                required
                                value={user.login}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                A valid login is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label text-size" htmlFor="Password">Hasło: </label>
                            <input
                                type="password"
                                className="form-control"
                                name="password"
                                placeholder=""
                                required
                                value={user.password}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label text-size" htmlFor="email">Email: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="email"
                                placeholder=""
                                required
                                value={user.email}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                A valid email is required.
                            </div>
                        </div>
                        <br></br>
                        <br></br>
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            onClick={() => this.setState({submitted: true})}
                            
                            disabled={loading}>
                            Zarejestruj
                        </button>
                        
                    </form>
                    <div className = "center-content">
                         <a href="/login" className="card-link href-text center-content text-size">Mam już konto, zaloguj</a>
                    </div>
                   
                </div>
            </div>
        );
    }
}

export {RegisterPage};