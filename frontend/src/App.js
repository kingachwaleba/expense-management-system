import React from 'react';
import './App.css';
import {Router, Route, Switch} from 'react-router-dom';
import {createBrowserHistory} from 'history';
import {LoginPage} from "./pages/login/LoginPage";
import {RegisterPage} from "./pages/register/RegisterPage";
import {HomePage} from "./pages/home/HomePage";
import AuthGuard from './guards/AuthGuard';

class App extends React.Component {


    constructor(props, context) {
        super(props, context);

        this.state = {
            history: createBrowserHistory(),
        };
    }

    render() {
        const {history} = this.state;

        return (
            <Router history={history}>
                <div className="container">
                    <Switch>
                        <Route exact path="/login" component={LoginPage}/>
                        <Route exact path="/register" component={RegisterPage}/>
                        <AuthGuard path="/home" component={HomePage}/>
                        <Route exact path="/home" component={HomePage}/>
                    </Switch>
                </div>
            </Router>
        );
    }
}

export default App;
