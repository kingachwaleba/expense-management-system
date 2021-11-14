import React, { Component } from 'react';
import Header from '../../components/Header';
import { Container, Row, Col } from 'reactstrap';
import { useState } from 'react';
import UserService from '../../services/user.service';
import ImageService from '../../services/ImageService';
function EditProfilePage (){
    const userData = UserService.getCurrentUser()
    let listID = '';
    if (sessionStorage && sessionStorage.getItem('listID')) {
        listID = JSON.parse(sessionStorage.getItem('listID'));
    }
    console.log("Userdata:", userData)
    const [imgHelper, setImgHelper] = useState(userData.image)

    const handleImageChange = e =>{
        console.log("Zmiana obrazka")
        setImgHelper(e.target.files[0])
    }
    const handleSetNewImg = e =>{
        var formData = new FormData();
        formData.append('image', imgHelper)
        ImageService.setUserProfileImg(formData, userData.token)
        .then((response)=>{
            console.log("Zdjęcie wysłano do bazy.")
            console.log(response.data)
            userData.image=response.data

            sessionStorage.setItem("user", JSON.stringify(userData));
            /*

            ImageService.setUserProfileImg(response.data, userData.token)
            .then((response)=>{
                console.log("Ustawiono nowe zdjęcie profilowe")
            })
            .catch((error)=>{
                console.log(error)
            })
            */
        })
        .catch((error) => {
            console.log(error)
        })
    }
        return (
            <Container>
                <Header title="Edytuj profil"/>
                <br />
                <br />
                <Col md={{span: 8, offset:4 }}   className="center-content">
                <Row md="2">
                    <input  
                            className="btn btn-primary btn-block form-button "
                            id = "insert-photo-button" 
                            type="file"
                            accept="image/png, image/jpeg, image/jpg" 
                            onChange={(e)=>handleImageChange(e)} 
                    />
                </Row>
                <br />
                <Row md="2">
                <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>handleSetNewImg()
                           
                        }>
                            Zmień zdjęcie
                </button>
                </Row >
                    <br></br>
                 
                <Row md="2">
                <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                          
                        }}>
                            Usuń zdjęcie
                </button>
                </Row>
                    <br></br>
                  
                <Row md="2">
               <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/profile"
                        }}>
                            Zmień hasło
                </button>
                </Row>   
                    <br></br>
             
                <Row md="2">
                <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/delete-account"
                        }}>
                            Usuń konto
                </button>
                </Row>  
                    <br></br>
                
                <Row md="2">
                <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/profile"
                        }}>
                           Anuluj
                </button>   
                </Row>
                </Col>
            </Container>
        );
    
}

export default EditProfilePage;