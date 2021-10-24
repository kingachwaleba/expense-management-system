import React, { Component, useEffect } from 'react';
import Header from '../../components/Header';
import { useState } from 'react';
import ManageWalletUsersService from '../../services/ManageWalletUsersService';
import UserService from '../../services/user.service';
import { Container, Col, Row } from 'react-bootstrap';
function EditUsersPage () {

    const [walletId, setWalletId] = useState("")
    const [userToken, setUserToken] = useState("")
    const [walletUsers, setWalletUsers] = useState([])
    const [message, setMessage] = useState("")
    useEffect(()=>{
        let walletIdHelper = '';
        if (sessionStorage && sessionStorage.getItem('walletID')) {
           walletIdHelper = JSON.parse(sessionStorage.getItem('walletID'));
          }
         setWalletId(walletIdHelper)
         setUserToken(UserService.getCurrentUser().token)
         //console.log(walletId)
         console.log("User token to " + userToken)
         console.log("Wallet Id to " + walletIdHelper)
         ManageWalletUsersService
            .getWalletUsers(walletIdHelper,UserService.getCurrentUser().token)
            .then((response)=>{
                setWalletUsers(response.data)
                console.log(response.data)
                console.log(walletUsers)
                if(response.data.length === 0){
                    setMessage("Brak użytkowników")
                }
                else{
                    setMessage("")
                }

               })
               .catch((error)=>{
                   
                   console.log(error.response.data)
               })
    },[])

    
        return (
            <Container>
                <Header title={"Edytuj \n członków"}/>
                <Col className="box-content text-size">
                    <h4 className="text-label center-content"> Portfel</h4>
                        <div className="separator-line"></div>
                    {
                    walletUsers.map(
                            user =>
                            <Row key = {user.userId} >
                            
                                <Col md="2">
                                #ProfilePic
                                </Col>
                                <Col className="center-content">
                                    {user.login}
                                </Col> 
                                <Col md="2" className="right-content">
                                    <button className="delete-user-icon icons-size"
                                    onClick={(e)=>{
                                        let walletIdHelper = '';
                                        if (sessionStorage && sessionStorage.getItem('walletID')) {
                                        walletIdHelper = JSON.parse(sessionStorage.getItem('walletID'));
                                        }
                                        if(window.confirm('Użykownik nie  będzie już należeć do tego portfela, kontynuować?')){
                                        ManageWalletUsersService
                                        .deleteWalletUser(walletIdHelper,user.login,UserService.getCurrentUser().token)
                                        .catch((error)=>{
                                            
                                            console.log(error.response.data)
                                        })
                                        window.location.href='/home'
                                        }
                                    }}></button>
                                </Col>
                            <br /><br />
                                
                            
                            </Row>      
                        )}
                
                <Row>
                            <button className="text-size base-text main-button-style"
                                onClick={(e)=>{
                                    let walletIdHelper = '';
                                        if (sessionStorage && sessionStorage.getItem('walletID')) {
                                        walletIdHelper = JSON.parse(sessionStorage.getItem('walletID'));
                                        }
                                    sessionStorage.setItem('walletID',JSON.stringify(walletIdHelper))
                                    window.location.href='/add-members'
                                }}>
                                    Zaproś to portfela
                                </button> 
             </Row>
             </Col>
            </Container>
        );
    
}

export default EditUsersPage;