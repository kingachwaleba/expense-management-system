import React from 'react';
import './App.css';
import {Route, Switch} from 'react-router-dom';
import {BrowserRouter as Router} from 'react-router-dom';
import {LoginPage} from "./pages/login/LoginPage";
import {RegisterPage} from "./pages/register/RegisterPage";
import {HomePage} from "./pages/home/HomePage";
import {StartPage} from "./pages/start/StartPage";
import {CreateWalletPage} from "./pages/create-wallet/CreateWalletPage";
import AuthGuard from './guards/AuthGuard';
import UserService from "./services/user.service";
import FooterPage from './components/Footer';

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
        //this.props.history.push('/login');
        window.location.reload();
    }

    render() {
        return (
            <div id="content">
            <Router>
                <div>
                    {this.state.login &&
                    <nav className="navbar navbar-expand navbar-dark">
                        <a className="navbarBrand" href="/start">
                            eSakwa
                        </a>
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <a className="nav-link" href="/home">
                                    <span className="fa fa-home"/>
                                    Portfele
                                </a>
                            </li>
                        

                        
                            <li className="nav-item">
                                <a className="nav-link" href="/profile">
                                    <span className="fa fa-user"/>
                                    {this.state.username}
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/" onClick={this.logOut}>
                                    <span className="fa fa-sign-out"/>
                                    Wyloguj
                                </a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link">
                                    
                                   
                                </a>
                            </li>
                        </div>
                    </nav>
                    }
                </div>
                <div>
                    {!this.state.login &&
                    <nav className="navbar navbar-expand navbar-dark">
                        <a className="navbarBrand" href="/">
                            eSakwa
                        </a>
                        
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                            <a className="btn btn-primary" id = "mainbuttonstyle" href="/login">
                                    <span className="fa fa-sign-in"/>
                                    Zaloguj się
                                </a>
                                
                            </li>
                            <li className="nav-item"> 
                            <a className="nav-link" href="">
                                <span className="fa fa-sign-in"/>

                                    &nbsp;
                                    | 
                         </a>
                            </li>
                            <li className="nav-item">
                            <a className="nav-link" href="/register">
                                    <span className="fa fa-user-plus"/>
                                    &nbsp;
                                    Załóż konto
                                </a>
                            </li>
                        </div>
                    </nav>
                    }
                </div>
                <div className="container">
                    <Switch>
                        <Route exact path="/" component={StartPage}/>
                        <Route exact path="/login" component={LoginPage}/>
                        <Route exact path="/register" component={RegisterPage}/>
                        <AuthGuard path="/create-wallet" component={CreateWalletPage}/>
                        <AuthGuard path="/home" component={HomePage}/>
                    </Switch>
                </div>
            </Router>
            <FooterPage/>
            </div>
        );
    }
}

export default App;
