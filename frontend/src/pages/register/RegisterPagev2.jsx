import React from 'react';
import { useEffect } from 'react';
import UserService from '../../services/user.service';
import { useHistory } from 'react-router';
import { useState } from 'react';
import { User } from '../../models/user';



function RegisterPagev2 (){

    const history = useHistory();
    const [email, setEmail] = useState("");
    const [login, setLogin] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState([]);
    const [errorMessageString, setErrorMessageString] = useState([]);
    const [isArray, setIsArray] = useState(false);


    useEffect(() => {
        if (UserService.getCurrentUser()) {
            history.push('/home');
        }
        else{

        }


      });

      function doRegister (event) {
        event.preventDefault();
        
        if (!login || !password || !email || !confirmPassword) {
            return;
        }
        
        const user = new User('',login,email,password,confirmPassword)
        if(password == confirmPassword){
            UserService
                .register(user)
                .then((response) => {

                        event.target.reset();
                        history.push('/login');
                        window.location.reload();
                })
                .catch((error)=>{
                    console.log(error.response.data)
                    console.log("From RegisterPage fail, error: " + error.toString());
                    if(Array.isArray(error.response.data)){
                        setErrorMessage(error.response.data);
                        setIsArray(true);
                    }
                    else{
                        setErrorMessageString(error.response.data)
                        setIsArray(false);
                    }
                    
                })
        } 
        else{
            setIsArray(false);
            setErrorMessageString("Hasła nie są identyczne.")
        }
               
    }

   

        return (
            <div>
                <div className = "form-container">

                    <form
                            name="form"
                            method="post"
                            onSubmit={doRegister}>
                            <div className={'form-group'}>
                                <label className="form-label"  htmlFor="login">Login: </label>
                                <input
                                    type="text"
                                    className="form-control"
                                    name="login"
                                    placeholder=""
                                    required
                                    value={login}
                                    pattern = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\d.-]{4,45}$"
                                    title = "Niepoprawny format loginu - może zawierać litery, cyfry i znak -, powinien zaczynać się literą."
                                    onChange={(e)=>setLogin(e.target.value)}
                                />
                                
                            </div>

                            <div className={'form-group'}>
                                <label className="form-label" htmlFor="email">Email: </label>
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
                                <label className="form-label" htmlFor="Password">Hasło: </label>
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
                                <label className="form-label" htmlFor="ConfirmPassword">Powtórz hasło: </label>
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

                           <div>
                               {

                                   isArray ? (
                                        errorMessage.map(
                                            message => 
                                            <div key={message}>
                                                {message}
                                            </div>
                                        )
                                   ) : (
                                        errorMessageString  
                                   )
                                   
                                }
                           </div>
                            
                            <br></br>
                            <button className="btn btn-primary btn-block form-button main-button-style"
                            
                                //onClick={() => this.setState({submitted: true})}
                                
                            >
                                Zarejestruj
                            </button>
                            
                        </form>
                        <div className = "center-content">
                            <a href="/login" className="card-link href-text center-content">Mam już konto, zaloguj</a>
                        </div>
                    
                    </div>
            </div>
        );
    
}

export default RegisterPagev2;