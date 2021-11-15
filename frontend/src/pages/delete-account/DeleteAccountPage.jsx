import React, { Component } from 'react';
import Header from '../../components/Header';
import { useState } from 'react';
import { Container, Row, Col } from 'reactstrap';
import UserService from '../../services/user.service';
function DeleteAccountPage (){
const [errorMessage, setErrorMessage] = useState("")
const [password, setPassword]=useState("")
const userData = UserService.getCurrentUser();


const handleChangePassword = e =>{
    setPassword(e.target.value)
}

const handleSubmit = e =>{
    e.preventDefault();
    if(window.confirm("Czy jesteś pewny/a, że chcesz usunąć konto? Ten proces jest nieodwracalny.")){
        var formData = new FormData()
        formData.append('password',password)
        UserService.deleteAccount(formData,userData.token)
        .then((response)=>{
            console.log(response.data)
            sessionStorage.removeItem('user')
            window.location.href = "/"
        })
        .catch((error)=>{
            console.log(error)
            setErrorMessage(error.response.data)
        })
    }
}
        return (
            <Container>
                <Header title={"USUWANIE \n KONTA"}/>
                <div className="center-content form-container">
                <Col md={{span: 6 , offset: 3}}className="base-text text-size">
                <h4>Czy na pewno chcesz usunąć konto?</h4>
                    <h6 className="text-label">UWAGA: Proces jest nieodwracalny.</h6>
                <form name="form"
                        method="post"
                        onSubmit={(e)=>handleSubmit(e)}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name"></label>
                            <input
                               required
                                type="password"
                                className="form-control"
                                name="name"
                                placeholder="Podaj hasło..."
                                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                onChange={(e)=>handleChangePassword(e)}
                            />
                            
                        </div>
                   
                 
                        <br/>
                <div className="error-text text-size center-content">
                    {errorMessage}
                </div>
                <br />
                        <button
                            className="btn btn-primary btn-block form-button text-size red-filter"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>{
                                
                               
                            }}
                            >
                            Tak - usuń konto
                        </button>
                </form>
                <br />
                <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "button"
                            onClick={e =>{
                                window.location.href = "/profile"
                               
                            }}
                            >
                            Anuluj
                        </button>
                </Col>
              {/* 
                    <h4>Czy na pewno chcesz usunąć konto?</h4>
                    <h6 className="text-label">UWAGA: Proces jest nieodwracalny.</h6>
                    <br />
                    <input
                                type="text"
                                className="form-control"
                                name="password"
                                placeholder="Podaj hasło"
                                required
                                value=""
                    />
                    <br />
                    <a href="/profile" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Tak-usuń konto</a>
                    <br />
                    <a href="/profile" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Nie-zabierz mnie stąd</a>

              */}   
                </div>
            </Container>
        );
    
}

export default DeleteAccountPage;