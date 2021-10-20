import React from 'react';
import './App.css';
import {Route, Switch} from 'react-router-dom';
import {BrowserRouter as Router} from 'react-router-dom';
import LoginPagev2 from "./pages/login/LoginPagev2";
import RegisterPagev2 from "./pages/register/RegisterPagev2";
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
import EditWalletPage from './pages/edit-wallet/EditWalletPage';
import ErrorPage from './pages/error/ErrorPage';
import { useHistory } from 'react-router';
import WalletPage from './pages/wallet/WalletPage';
import AuthGuard from './guards/AuthGuard';
import UserService from "./services/user.service";
import FooterPage from './components/Footer';
import { Container, Navbar, Nav } from 'react-bootstrap';

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
                    <Navbar className="navbar navbar-expand navbar-dark width-100">
                        <Container>
                            <Navbar.Brand className="navbarBrand" href="/home">eSakwa</Navbar.Brand>
                            <Nav className="content-right">
                            <Nav.Link href="/home">Portfele</Nav.Link>
                            <Nav.Link href="/profile"> Profil </Nav.Link>
                            <Nav.Link onClick={this.logOut}> Wyloguj </Nav.Link>
                            <Nav.Link href="/profile">
                                <img src="./photo_placeholder.png" alt="Picture"></img> 
                            </Nav.Link>
                            </Nav>
                        </Container>
                    </Navbar>
                    }
                </div>
                <div>
                    {!this.state.login &&
                     
                   <Navbar className="navbar-dark">
                        <Container>
                        <Navbar.Brand className="navbarBrand" href="/start">eSakwa</Navbar.Brand>
                        <Nav className="content-right">
                        <Nav.Link className="btn btn-primary main-button-style"href="/login">Zaloguj się</Nav.Link>
                        <Nav.Link>|</Nav.Link>
                        <Nav.Link href="/register">Zarejestruj się</Nav.Link>
                        </Nav>
                        </Container>
                    </Navbar>
  
               
                    }
                </div>
                <div className="container">
                    <Switch>

                        


                        <Route exact path="/" component={StartPage}/>
                       

                        <Route exact path="/login" component={LoginPagev2}/>
                        <Route exact path="/register" component={RegisterPagev2}/>
                        <Route exact path="/statutes" component={StatutesPage}/>
                        <Route exact path="/error-page" component={ErrorPage}/>

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
                        <AuthGuard  path="/register" component={HomePage}/>
                        
                        
                    </Switch>
                </div>
            </Router>
            <FooterPage/>
            </div>
        );
    }
}

export default App;
