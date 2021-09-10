import React from 'react';
import './App.css';
import {Route, Switch} from 'react-router-dom';
import {BrowserRouter as Router} from 'react-router-dom';
import {LoginPage} from "./pages/login/LoginPage";
import {RegisterPage} from "./pages/register/RegisterPage";
import {HomePage} from "./pages/home/HomePage";
import {StartPage} from "./pages/start/StartPage";
import {ProfilePage} from "./pages/profile/ProfilePage";
import {CreateWalletPage} from "./pages/create-wallet/CreateWalletPage";
import StatutesPage from "./pages/statutes/StatutesPage";
import EditProfilePage from "./pages/edit-profile/EditProfilePage";
import DeleteAccountPage from "./pages/delete-account/DeleteAccountPage";
import ChangePasswordPage from './pages/change-password/ChangePasswordPage';
import AddMembersPage from './pages/add-members/AddMembersPage';
import ChatPage from './pages/chat/ChatPage';
import EditUsersPage from './pages/edit-users-list/EditUsersPage';
import AddExpensePage from './pages/add-expense/AddExpensePage';
import ExpenseDetailPage from './pages/expense-detail/ExpenseDetailPage';
import CreateListPage from './pages/Create-list/CreateListPage';
import ListDetailPage from './pages/list-detail/ListDetailPage';
import StatisticsPage from './pages/wallet-stats/StatisticsPage';
import EditWalletPage
 from './pages/edit-wallet/EditWalletPage';
import WalletPage from './pages/wallet/WalletPage';
import AuthGuard from './guards/AuthGuard';
import UserService from "./services/user.service";
import FooterPage from './components/Footer';

class App extends React.Component {

    constructor(props, context) {
        super(props, context);

        this.state = {
            username: undefined,
            login: false
        }
        this.logOut = this.logOut.bind(this);
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
                                    Profil
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
                                    <img src="./photo_placeholder.png" alt="Picture"></img>                                   
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
                        <Route exact path="/statutes" component={StatutesPage}/>


                        <AuthGuard path="/create-wallet" component={CreateWalletPage}/>
                        <AuthGuard path="/edit-profile" component={EditProfilePage}/>
                        <AuthGuard path="/delete-account" component={DeleteAccountPage}/>
                        <AuthGuard path="/change-password" component={ChangePasswordPage}/>
                        <AuthGuard path="/wallet" component={WalletPage}/>
                        <AuthGuard path="/add-members" component={AddMembersPage}/>
                        <AuthGuard path="/chat" component={ChatPage}/>
                        <AuthGuard path="/edit-users-list" component={EditUsersPage}/>
                        <AuthGuard path="/add-expense" component={AddExpensePage}/>
                        <AuthGuard path="/expense-detail" component={ExpenseDetailPage}/>
                        <AuthGuard path="/create-list" component={CreateListPage}/>
                        <AuthGuard path="/list-detail" component={ListDetailPage}/>
                        <AuthGuard path="/wallet-stats" component={StatisticsPage}/>
                        <AuthGuard path="/edit-wallet" component={EditWalletPage}/>

                        <AuthGuard path="/home" component={HomePage}/>
                        <AuthGuard path="/profile" component={ProfilePage}/>
                        <AuthGuard path="/start" component={HomePage}/>
                        <AuthGuard path="/" component={HomePage}/>
                        <AuthGuard path="/login" component={HomePage}/>
                        <AuthGuard path="/register" component={HomePage}/>
                        
                    </Switch>
                </div>
            </Router>
            <FooterPage/>
            </div>
        );
    }
}

export default App;
