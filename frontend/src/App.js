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
import WalletPage from './pages/wallet/WalletPage';
import AuthGuard from './guards/AuthGuard';
import UserService from "./services/user.service";
import FooterPage from './components/Footer';
import EditExpensePage from './pages/edit-expense/EditExpensePage';
import { Container, Navbar, Nav, Row, Col } from 'react-bootstrap';
import EditShoppingListName from './pages/edit-shopping-list-name/EditShoppingListName';
import RemindPasswordPage from './pages/remind-password/RemindPasswordPage';
import picture from '../src/profile_picture_placeholder.jpg'

import ImageService from './services/ImageService';
import ResetPasswordPage from './pages/reset-password/ResetPasswordPage';
class App extends React.Component {

    constructor(props, context) {
        super(props, context);
        const user = UserService.getCurrentUser();
        this.state = {
            username: "",
            login: false,
            userImage: "",
            profileImgUrl: null,
            userToken: "user.token"
        }
        console.log("App.js user data:" ,user)
        this.logOut = this.logOut.bind(this);
    }

    componentDidMount() {
        const user = UserService.getCurrentUser();
        
        if (user) {
            this.setProfilePicHelper(user.login)
            this.setState({
                login: true,
                username: user.login,
                userImage: user.image,
                userToken: user.token
            });
            
            
            console.log(user);
        }
    }

    logOut() {
      
        UserService.logOut();
        
        window.location.href='/login'
        window.location.reload();
        
    }

    setProfilePicHelper(login){
        const user = UserService.getCurrentUser();
        console.log("img url:", user.image)
        console.log("login:", user.login)
        if(user.image === null){
           // return (<img src={picture} className="profile-img-preview" alt="profilePic"/>)
           this.setState({profileImgUrl: picture})
        }
        else{
            ImageService.getUserProfileImg(user.login, user.token)
            .then((response)=>{
                var profilePic = URL.createObjectURL(response.data)
            
                this.setState({profileImgUrl: profilePic})
              
            })
            .catch((error)=>{
                console.log(error)
         
            })
           
        }
        
    }

    render() {
        return (
            <div id="content">
            <Router>
                <div>
                    {this.state.login &&
                    <Navbar className="navbar navbar-expand navbar-dark width-100">
                        <Container>
                            
                            <Navbar.Brand className="navbarBrand" id="bootstrap-overrides" href="/home">eSakwa</Navbar.Brand>
                            <Nav className="content-right">
                            <Nav.Link className="text-size base-text" href="/home">Portfele</Nav.Link>
                            <Nav.Link className="text-size base-text" href="/profile"> Profil </Nav.Link>
                            <Nav.Link className="text-size base-text" onClick={this.logOut}> Wyloguj </Nav.Link>
                            <Nav.Link href="/profile">
                                {<img src={this.state.profileImgUrl} className="icons-size profile-img-preview" alt="Picture"></img>
                                    //this.setProfilePicHelper()
                                }
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
                        <Navbar.Brand className="navbar-brand" id="bootstrap-overrides" href="/start">eSakwa</Navbar.Brand>
                        <Nav className="content-right">
                        <Nav.Link className="btn btn-primary main-button-style text-size base-text"href="/login">Zaloguj się</Nav.Link>
                        <Nav.Link className="text-size base-text" >|</Nav.Link>
                        <Nav.Link className="text-size base-text" href="/register">Zarejestruj się</Nav.Link>
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
                        <Route exact path="/remind-password" component={RemindPasswordPage}/>
                        <Route exact path="/account/reset-password" render={(props)=><ResetPasswordPage {...props}/>}/>

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
                        <AuthGuard path="/edit-expense" component={EditExpensePage}/>
                        <AuthGuard path="/create-list" component={CreateListPage}/>
                        <AuthGuard path="/list-detail" component={ListDetailPage}/>
                        <AuthGuard path="/wallet-stats" component={StatisticsPage}/>        
                        <AuthGuard path="/edit-wallet" component={EditWalletPage}/>
                        <AuthGuard path="/edit-shopping-list-name" component={EditShoppingListName}/>
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
