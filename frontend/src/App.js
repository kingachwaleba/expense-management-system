import React from 'react';
import './App.css';
import {Route, Switch} from 'react-router-dom';
import {BrowserRouter as Router} from 'react-router-dom';
import {LoginPage} from "./pages/login/LoginPage";
import {RegisterPage} from "./pages/register/RegisterPage";
import {HomePage} from "./pages/home/HomePage";
import AuthGuard from './guards/AuthGuard';
import UserService from "./services/user.service";

class App extends React.Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            username: undefined,
            login: false
        };
    }

    componentDidMount() {
        const user = UserService.getCurrentUser();

        if (user) {
            this.setState({
                login: true,
                username: user.login
            });

            console.log(user);
        }
    }

    logOut() {
        UserService.logOut();
        this.props.history.push('/login');
        window.location.reload();
    }

    render() {
        return (
            <Router>
                <div>
                    {this.state.login &&
                    <nav className="navbar navbar-expand navbar-dark bg-dark">
                        <a className="navbar-brand" href="https://reactjs.org">
                            React
                        </a>
                        <div className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/home">
                                    <span className="fa fa-home"/>
                                    Home
                                </a>
                            </li>
                        </div>

                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/profile">
                                    <span className="fa fa-user"/>
                                    {this.state.username}
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="#" onClick={this.logOut}>
                                    <span className="fa fa-sign-out"/>
                                    LogOut
                                </a>
                            </li>
                        </div>
                    </nav>
                    }
                </div>
                <div>
                    {!this.state.login &&
                    <nav className="navbar navbar-expand navbar-dark bg-dark">
                        <a className="navbar-brand" href="https://reactjs.org">
                            React
                        </a>
                        <div className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/home">
                                    <span className="fa fa-home"/>
                                    Home
                                </a>
                            </li>
                        </div>
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/register">
                                    <span className="fa fa-user-plus"/>
                                    &nbsp;
                                    Register
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/login">
                                    <span className="fa fa-sign-in"/>
                                    Login
                                </a>
                            </li>
                        </div>
                    </nav>
                    }
                </div>
                <div className="container">
                    <Switch>
                        <Route exact path="/login" component={LoginPage}/>
                        <Route exact path="/register" component={RegisterPage}/>
                        <AuthGuard path="/home" component={HomePage}/>
                    </Switch>
                </div>
            </Router>
        );
    }
}

export default App;
