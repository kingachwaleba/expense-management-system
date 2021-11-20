import React, { Component } from 'react';
import { useState } from 'react';
import UserService from '../../services/user.service';
import Header from '../../components/Header';
import { useHistory } from 'react-router';
export default function LoginPagev2 () {

    const history = useHistory();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMassage, setErrorMassage] = useState("");

    function validateForm(){
        return email.length > 0 && password.length > 0;
    }
 
    async function doLogin (event) {
        event.preventDefault();
        UserService
            .login(email, password)
            .then((response)=>{
                event.target.reset();
                history.push('/home');
                window.location.reload();
            })
            .catch((error)=>{
                setErrorMassage(error.response.data);
            })
    };


        return (

            <div>
                 <Header title='Zaloguj się' />
                <div className="form-container">
                    <form
                            name="form"
                            method="post"
                            onSubmit={doLogin}>
                            <div className={'form-group'}>
                                <label className="form-label text-size" htmlFor="Email">Email: </label>
                                    <input
                                        type="email"
                                        className="form-control"
                                        name="email"
                                        placeholder=""
                                        required
                                        value={email}
                                        onChange={(e)=>setEmail(e.target.value)}
                                    />
                              
                            </div>

                            <div className="form-group">
                                <label className="form-label text-size" htmlFor="Password">Hasło: </label>
                                    <input
                                        type="password"
                                        className="form-control"
                                        name="password"
                                        placeholder=""
                                        required
                                        value={password}
                                        onChange={(e)=>setPassword(e.target.value)}
                                    />
                               
                            </div>
                           <div className="error-text text-size center-content">
                           
                               {errorMassage}
                             
                            </div> 
                       
                            <div className="center-content" >
                                    <a href="/remind-password" className="card-link href-text text-size">Nie pamietam hasła, przypomnij hasło.</a>
                            </div>
                            <br></br>
                            <div  className="center-content">
                                    <button className="btn btn-primary form-button main-button-style text-size" type="submit" disabled={!validateForm}>
                                        Zaloguj się
                                    </button>
                            </div>
                        </form>
                </div>

            </div>
        );
    

    }

