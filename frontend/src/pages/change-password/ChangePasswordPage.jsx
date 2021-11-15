import React, { Component } from 'react';
import Header from '../../components/Header';
import { Container, Row, Col } from 'react-bootstrap';
import { useState } from 'react';
import UserService from '../../services/user.service';

function ChangePasswordPage (){


const userData = UserService.getCurrentUser()

const [errorMessage, setErrorMessage] = useState("")
const [oldPassword, setOldPassword] = useState("")
const [newPassword, setNewPassword] = useState("")
const [confirmNewPassword, setConfirmNewPassword] = useState("")
const [updatePasswordHolder, setUpdatePasswordHolder] = useState({
    password: "",
    confirmPassword: "",
    oldPassword: ""
})

const handleChangeOldPassword = e =>{
    setOldPassword(e.target.value)
    setErrorMessage("")
}
const handleChangeNewPassword = e =>{
    setNewPassword(e.target.value)
    setErrorMessage("")
}
const handleChangeConfirmNewPassword = e =>{
    setConfirmNewPassword(e.target.value)
    setErrorMessage("")
}
const handleSubmit = e =>{
    e.preventDefault();
    if(newPassword === confirmNewPassword){
        setUpdatePasswordHolder({
            password: newPassword,
            confirmPassword: confirmNewPassword,
            oldPassword: oldPassword
        })

        console.log("updatePasswordHolder:", updatePasswordHolder)
        console.log("userToken:", userData.token)
        UserService.changePassword(updatePasswordHolder, userData.token)
        .then((response) => {
            console.log(response.data)
        })
        .catch((error)=>{
            console.log(error)
            setErrorMessage(error.response.data)
        })
    }
    else{
        setErrorMessage("Podane hasła różnią się od siebie!")
    }
}
        return (
            <Container>
                <Header title={"Zmiana \n hasła"}/>
                <Col md={{span: 6 , offset: 3}}className="base-text text-size">
                <form name="form"
                        method="post"
                        onSubmit={(e)=>handleSubmit(e)}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Stare hasło: </label>
                            <input
                               required
                                type="password"
                                className="form-control"
                                name="name"
                                placeholder="Podaj stare hasło..."
                                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                onChange={(e)=>handleChangeOldPassword(e)}
                            />
                            
                        </div>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nowe hasło: </label>
                            <input
                               required
                                type="password"
                                className="form-control"
                                name="name"
                                placeholder="Podaj nowe hasło..."
                                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                onChange={(e)=>handleChangeNewPassword(e)}
                            />
                            
                        </div>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Powtórz nowe hasło: </label>
                            <input
                               required
                                type="password"
                                className="form-control"
                                name="name"
                                placeholder="Powtórz nowe hasło..."
                                minLength="8"
                                maxLength="50"
                                pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,50}$"
                                title = "Hasło musi zawierać minimum jedną wielką i małą literę oraz cyfrę. Musi zawierać od 8 do 50 znaków."
                                onChange={(e)=>handleChangeConfirmNewPassword(e)}
                            />
                            
                        </div>
                        <br/>
                <div className="error-text text-size center-content">
                    {errorMessage}
                </div>
                <br />
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>{
                                
                               
                            }}
                            >
                            Zapisz zmiany
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
              


                        </Container>
       
        );
    
}

export default ChangePasswordPage;