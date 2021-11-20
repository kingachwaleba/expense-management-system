import React from 'react'
import { Row, Col, Container } from 'react-bootstrap'
import Header from '../../components/Header'
import { useState } from 'react'
import { useLocation } from 'react-router'
import { useParams } from 'react-router'
import UserService from '../../services/user.service'
//const queryString = require('query-string')
function ResetPasswordPage({props}) {
    const location = useLocation()
    const urlParams = new URLSearchParams(location.search)
    const token = urlParams.get('token')

  
    const [errorMessage, setErrorMessage] = useState("")
    const [newPassword, setNewPassword] = useState("")
    const [confirmNewPassword, setConfirmNewPassword] = useState("")
    const [updatePasswordHolder, setUpdatePasswordHolder] = useState({
        token: {token},
        password: "",
        confirmPassword: ""
    })
    //const token = props.match.params
   
    
    console.log(token)

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
            var formData = new FormData()
            formData.append('password', newPassword)
            formData.append('confirmPassword', confirmNewPassword)
            formData.append('token', token)
            console.log("token:", token)
            console.log('Password:', newPassword)
            console.log('Confirm Password', confirmNewPassword )
           console.log("Form data:", formData)
            UserService.resetPassword(formData)
            .then((response) => {
                console.log(response.data)
                window.alert("Hasło zostało zmienione.")
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
             <Header title={"Resetuj hasło"}/>
             <br />
                <Col md={{span: 6 , offset: 3}}className="base-text text-size">
                <form name="form"
                        method="post"
                        onSubmit={(e)=>handleSubmit(e)}
                        >
                      
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
                            Zapisz nowe hasło
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
    )
}

export default ResetPasswordPage
