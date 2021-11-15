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
        if(e.target.files[0].size > 5242880){
           window.alert("Wybrane zdjecie jest za duże! Wybierz zdjęcie poniżej 5MB")
            document.getElementById("insert-photo-button").value = "";
        }
        else
        {
        console.log("Zmiana obrazka")
        setImgHelper(e.target.files[0])
        }
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
          
        })
        .catch((error) => {
            console.log(error.message)
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
                <button className={`card-link center-content btn btn-primary width-100 text-size main-button-style ${imgHelper === null ? ('grey-scale'):('')}`} 
                        type="button"
                        disabled={imgHelper === null}
                        onClick={(e)=>handleSetNewImg()
                           
                        }>
                            Zmień zdjęcie
                </button>
                </Row >
                    <br></br>
                 
                <Row md="2">
                <button className={`card-link center-content btn btn-primary width-100 text-size main-button-style ${userData.image === null ? ('grey-scale'):('')}`} 
                        type="button"
                        disabled={userData.image === null}
                        onClick={(e)=>{
                            console.log("token:", userData.token)
                            ImageService.deleteProfileImg(userData.token)
                            .then((response)=>{
                                console.log("Usunięto zdjecie profilowe", response)
                                userData.image=null

                                sessionStorage.setItem("user", JSON.stringify(userData));
                                window.location.href = "/edit-profile"
                            })
                            .catch((error)=>{
                                console.log(error)
                            })
                          
                        }}>
                            Usuń aktualne zdjęcie
                </button>
                </Row>
                    <br></br>
                  
                <Row md="2">
               <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/change-password"
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