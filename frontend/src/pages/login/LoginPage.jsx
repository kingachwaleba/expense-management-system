import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';
import './loginpage.css';
import Header from '../../components/Header';
class LoginPage extends React.Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            email: "",
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
            .login(this.state.email,
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
                 <Header title='Zaloguj się' />
                <div className="login-container">
                    <div className="header-container">
                        <i className="fa fa-user"/>
                    </div>

                    <form
                        name="form"
                        method="post"
                        onSubmit={this.doLogin}>
                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="username">Email: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="email"
                                placeholder="Login"
                                required
                                value={this.state.email}
                                onChange={this.changeHandler}/>
                            <div className="invalid-feedback">
                                A valid login is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Password">Hasło: </label>
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
                            <a href="/register" className="card-link href-text">Nie pamietam hasła, przypomnij hasło.</a>
                            <br></br>
                            <button className="btn btn-primary form-button" id = "mainbuttonstyle" type="submit">
                            Zaloguj się
                        </button>
                        </div>
                          
                       
                        {
                            this.state.error && (
                                <alert color="danger">
                                    {this.state.error}
                                </alert>
                            )
                        }
                    </form>
                    <div className="grid-container">
                  Nie masz konta?
                      <a href="/register" className="card-link href-text">Zarejestruj sie!</a> 
                    </div>
                    
                </div>
            </div>
        );
    }
}

export {LoginPage};