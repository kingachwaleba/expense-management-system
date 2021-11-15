import React, { Component, useCallback } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import UserService from '../services/user.service';
import { useState } from 'react';
import { useEffect } from 'react';
import AccountService from '../services/AccountService';
import NotificationService from '../services/NotificationService';
import ImageService from '../services/ImageService';
import picture from '../profile_picture_placeholder.jpg'

function ProfileDataComponent (){

  
    const userData = UserService.getCurrentUser();
    const [accountInfo, setAccountInfo] = useState([]);
    const [displayProfilePic, setDisplayProfilePic] = useState(null);
   let accountData = [];
   let token = undefined;
 
    function setData(data){
        if(data)
        setAccountInfo(data)
        else console.log("NIE DZIAŁA")
    }
    

    useEffect(()=>{
        AccountService
        .getProfileInfo(userData.token)
        .then((response)=>{
            console.log("Account data: ",response.data)
            accountData = response.data;
            setAccountInfo(accountData);

            if(userData.image !== null)
            ImageService.getUserProfileImg(userData.login, userData.token)
            .then((response)=>{
                var profilePic = URL.createObjectURL(response.data)
                console.log(profilePic)
                setDisplayProfilePic(profilePic)
            })
            .catch((error)=>{
                console.log(error)
            })
        })
        .catch((error)=>{  
            console.log(error)
        })
    
  
    },[])

   
    
        return (
            <Container className="container content-center">
               
                <Col>
                    <div className="profile-picture">
                        <img src={userData.image === null ? (picture):(displayProfilePic)} className="profile-img-preview" alt="profilePic" />
                    </div>
                            
                   
                    <div className="base-text text-size box-content">
                        <p>Login: {accountInfo.login}</p>
                        <p>Email: {accountInfo.email}</p>
                        <p>Liczba porfeli: {accountInfo.walletsNumber}</p>
                        <p>Saldo: {accountInfo.userBalance}</p>
                        <a href="/edit-profile" className="card-link center-content btn text-size btn-primary" id="mainbuttonstyle">Edytuj profil</a>
                    </div>
                </Col>
                

                
                
                
            </Container>
        );
    
}

export default ProfileDataComponent;