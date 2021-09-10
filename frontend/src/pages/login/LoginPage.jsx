import React from 'react';
import UserService from '../../services/user.service';
import {User} from '../../models/user';
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
                    this.setState({error: "Logowanie nie powiodło się! Proszę sprawdź login/hasło ponownie"});
                }
            );
    };

    render() {
        return (
            <div className="login-page">
                 <Header title='Zaloguj się' />
                <div className="form-container">
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
                                placeholder=""
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
                                placeholder=""
                                required
                                value={this.state.password}
                                onChange={this.changeHandler}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                            
                        </div>
                          
                       
                        {
                            this.state.error && (
                                <alert color="danger">
                                    {this.state.error}
                                </alert>
                            )
                        }
                        <div className="center-content" >
                    <a href="/remindpassword" className="card-link href-text">Nie pamietam hasła, przypomnij hasło.</a>
                    </div>
                    <br></br>
                    <div  className="center-content">
                        <button className="btn btn-primary form-button" id = "mainbuttonstyle" type="submit">
                    Zaloguj się
                    </button>
                    </div>
                    </form>
                    
                    
                    <div className="center-content" >
                  Nie masz konta?
                      <a href="/register" className="card-link href-text  center-content"> Zarejestruj sie!</a> 
                    </div>
                    
                </div>
            </div>
        );
    }
}

export {LoginPage};