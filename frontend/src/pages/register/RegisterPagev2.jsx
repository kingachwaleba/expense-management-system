import React from 'react';
import { useEffect } from 'react';
import UserService from '../../services/user.service';
import { useHistory } from 'react-router';
import { useState } from 'react';
import { User } from '../../models/user';
import {  RegisterUserHolder } from '../../models/helpers/registerUserHolder';
import Header from '../../components/Header';


function RegisterPagev2 (){

    const history = useHistory();
    const [email, setEmail] = useState("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessageString, setErrorMessageString] = useState([]);
    


    useEffect(() => {
        if (UserService.getCurrentUser()) {
            history.push('/home');
        }
        else{

        }


      });

      function doRegister (event) {
        event.preventDefault();
        if (!login || !password || !email || !confirmPassword){
            return;
        }
        const user = new User('',login,email,password)
        const registerUserHolder = new RegisterUserHolder(user, confirmPassword)
        if((password == confirmPassword) && (document.getElementById("confirmStatute").checked)){
            UserService
                .register(registerUserHolder)
                .then((response) => {
                    event.target.reset();
                    history.push('/login');
                    window.location.reload();
                })
                .catch((error)=>{
                    setErrorMessageString(error.response.data)
                })
        } 
        else if(password != confirmPassword){
            setErrorMessageString("Hasła nie są identyczne.")
        }
        else if(!document.getElementById("confirmStatute").checked){
            setErrorMessageString("Musisz zaakceptować regulamin.")
        }         
     }

   

        return (
            <div>
                 <Header title='Zarejestruj się' />
                <div className = "form-container">

                    <form
                            name="form"
                            method="post"
                            onSubmit={doRegister}>
                            <div className={'form-group'}>
                                <label className="form-label text-size"  htmlFor="login">Login* </label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="login"
                                    required
                                    value={login}
                                    pattern = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\d.-]{4,45}$"
                                    title = "Login może zawierać litery, cyfry i znak -, powinien zaczynać się literą."
                                    onChange={(e)=>setLogin(e.target.value)}
                                />     
                            </div>

                            <div className={'form-group'}>
                                <label className="form-label text-size" htmlFor="email">Email* </label>
                                <input
                                    type="email"
                                    className="form-control"
                                    name="email"
                                    placeholder=""
                                    required
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                                
                            </div>

                            <div className={'form-group'}>
                                <label className="form-label text-size" htmlFor="Password">Hasło* </label>
                                <input
                                    type="password"
                                    className="form-control"
                                    name="password"
                                    placeholder=""
                                    required
                                    value={password}
                                    pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                    //pattern = ""
                                    title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                                
                            </div>
                            <div className={'form-group'}>
                                <label className="form-label text-size" htmlFor="ConfirmPassword">Powtórz hasło* </label>
                                <input
                                    type="password"
                                    className="form-control"
                                    name="ConfirmPassword"
                                    placeholder=""
                                    required
                                    value={confirmPassword}
                                    //pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                    //title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                />
                                
                            </div>
                            <h5> * - pola wymagane.</h5>
                            <br />
                            <div>
                                <input type="checkbox" id="confirmStatute" name="confirmStatute"/>
                                        
                                <label for="confirmStatute" className="text-size"> <h5> - Przeczytałem/am i akceptuję warunki regulaminu</h5></label>
                            </div>


                           <div className="error-text text-size">
                               <br />
                               {errorMessageString}
                               
                           </div>
                            
                            <br></br>
                            <button className="btn btn-primary btn-block form-button main-button-style text-size">
                                Zarejestruj
                            </button>
                            
                        </form>
                        <div className = "center-content">
                            <a href="/login" className="card-link href-text center-content text-size">Mam już konto, zaloguj</a>
                        </div>
                    
                    </div>
            </div>
        );
    
}

export default RegisterPagev2;