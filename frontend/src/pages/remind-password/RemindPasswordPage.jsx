import React from 'react'
import Header from '../../components/Header'
import { Container, Row, Col } from 'reactstrap';
import { useState } from 'react';
import UserService from '../../services/user.service';

function RemindPasswordPage() {
    const [errorMessage, setErrorMessage] = useState([])
    const [email, setEmail] = useState("")



    const handleChange =(e)=>{
        console.log(e.target.value)
        setEmail(e.target.value)
        setErrorMessage("")
    }

    const handleSubmit = (e) =>{
        e.preventDefault();
        var formData = new FormData()
        formData.append('email',email)
        UserService.forgotPassword(formData)
        .then((response)=>{
            console.log(response.data)
            console.log("Wysłano maila")
            window.alert(response.data)
            window.location.href="/login"
        })
        .catch((error)=>{
            console.log(error.response.data)
           
                 setErrorMessage(error.response.data.toString())
            
        
        })
    }
    return (
        <Container>
             <Header title={"Przypomnij \n hasło:"}/>

             <Col md ="5" className="box-content text-size">
                   <form name="form"
                        method="post"
                        onSubmit={(e)=>handleSubmit(e)}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Podaj adres email:</label>
                            <input
                               
                                type="email"
                                className="form-control"
                                name="name"
                                title="Na podany adres zostanie wysłany email z linkiem do zresetowania hasła."
                                minLength="1"
                                maxLength="45"
                                onChange={(e)=>handleChange(e)}
                            />
                            
                        </div>
                <div className="error-text text-size">
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
                            Wyślij link
                        </button>
                </form>
                    <br />
                    <div className="center-content">
                        <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/login"
                        }}>Anuluj</button>
                    </div>
                    </Col>
        </Container>
    )
}

export default RemindPasswordPage
