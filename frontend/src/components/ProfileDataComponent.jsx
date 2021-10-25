import React, { Component, useCallback } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import UserService from '../services/user.service';
import { useState } from 'react';
import { useEffect } from 'react';
import AccountService from '../services/AccountService';
import NotificationService from '../services/NotificationService';

const delay = (ms) => new Promise((resolve) => window.setTimeout(resolve, ms));
function ProfileDataComponent (){

  
   
    const [accountInfo, setAccountInfo] = useState([]);
 
   let accountData = [];
   let token = undefined;
 
    function setData(data){
        if(data)
        setAccountInfo(data)
        else console.log("NIE DZIAŁA")
    }
    

    useEffect(()=>{

        const userData = UserService.getCurrentUser();
        read();
  
    },[])

    const read = async()=>{
        const userData = UserService.getCurrentUser();
        AccountService
        .getProfileInfo(userData.token)
        .then((response)=>{
            accountData = response.data;
            setAccountInfo(accountData);
        })
        .catch((error)=>{  
            console.log(error)
        })

        
    }
    
        return (
            <Container className="container content-center">
               
                <Col>
                    <div className="profile-picture" />
                            
                   
                    <div className="base-text text-size box-content">
                        <p>Login: {accountInfo.login}</p>
                        <p>Email: {accountInfo.email}</p>
                        <p>Liczba porfeli: {accountInfo.walletsNumber}</p>
                        <p>Saldo: {accountInfo.userBalance}</p>
                        <a href="/edit-profile" className="card-link center-content btn btn-primary" id="mainbuttonstyle">Edytuj profil</a>
                    </div>
                </Col>
                

                
                
                
            </Container>
        );
    
}

export default ProfileDataComponent;