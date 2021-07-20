import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';

class LoginPage extends React.Component {


    constructor(props, context) {
        super(props, context);

        if (UserService.currentUserValue) {
            this.props.history.push('/home');
        }

        this.state = {
            user: new User('', ''),
            submitted: false,
            loading: false,
            errorMessage: '',
        };
    }

    handleChange(e) {
        const {name, value} = e.target;
        const {user} = this.state;
        user[name] = value;
        this.setState({user: user});
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({submitted: true});
        const {user} = this.state;

        //Validation.
        if (!user.login || !user.password) {
            return;
        }

        this.setState({loading: true});
        UserService.login(user)
            .then(
                data => {
                    this.props.history.push('/home');
                },
                error => {
                    console.log(error);
                    this.setState({
                        errorMessage: 'Login or password is not valid.',
                        loading: false,
                    });
                },
            );
    }

    render() {
        const {user, submitted, loading, errorMessage} = this.state;
        return (
            <div className="login-page">
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
                        onSubmit={(e) => this.handleLogin(e)}
                        noValidate
                        className={submitted ? 'was-validated' : ''}>
                        <div className={'form-group'}>
                            <label htmlFor="username">Login: </label>
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

                        <button
                            className="btn btn-primary btn-block"
                            onClick={() => this.setState({submitted: true})}
                            disabled={loading}>
                            Login
                        </button>
                    </form>
                    <a href="/register" className="card-link">Create New Account!</a>
                </div>
            </div>
        );
    }
}

export {LoginPage};