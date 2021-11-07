import React, { Component } from 'react';
import NotificationService from '../services/NotificationService';
import UserService from '../services/user.service';
import { useEffect } from 'react';
import { useState } from 'react';
import { Container } from 'react-bootstrap';
import { Button } from 'reactstrap';




function NotificationComponent(){
    
    
    const [notification, setNotification] = useState([]);
    
    const [userToken, setUserToken] = useState("")
    const [message, setMessage] = useState("")
    const [reminders, setReminders] = useState("")
    const [messageReminder, setMessageReminder] = useState("")
    useEffect(()=>{
       
           
            
            const userData = UserService.getCurrentUser();
            setUserToken(userData.token)  
           
               NotificationService
               .getNotifications(userData.token)
               .then((response)=>{
                setNotification(response.data)
                if(response.data.length === 0){
                    setMessage("Brak powiadomień")
                }
                else{
                    setMessage("")
                }




               })
               .catch((error)=>{
                   
                   console.log(error)
               })
             
               NotificationService.getReminders(userData.token)
               .then((response)=>{
                setReminders(response.data)
                console.log(response.data)
                if(response.data.length === 0){
                    setMessageReminder("Brak powiadomień")
                }
                else{
                    setMessageReminder("")
                }
               })
               .catch((error)=>{
                   
                   console.log(error)
               })
              
               
       

    },[])

    function NotificationResponse(flag, walletUserID){
       
        NotificationService
        .responseToNotification(walletUserID, flag ,userToken)
       
        .catch((error)=>{
            
            console.log(error)
        })
        window.location.reload(false);
       
     
    }

        return (
            <Container> 
                {
                    notification.map(
                        notificationTab =>
                        <div key =  {Math.floor(Math.random() * 100000)} >
                            <div className="box-content">
                                <h3 className="text-label">{notificationTab.name}</h3>
                                <div className="separator-line"></div>
                            
                                Właściciel: {notificationTab.owner}
                                <br />
                                Liczba członków: {notificationTab.userListCounter}
                                
                                <br />
                                <Button 
                                    className="card-link center-content btn btn-primary main-button-style"
                                    onClick={function(e){
                                        NotificationResponse(JSON.stringify(true), notificationTab.walletUserId); 
                                    }}>
                                    Zaakceptuj
                                </Button>
                        
                                <Button className="card-link center-content btn btn-primary main-button-style" 
                                    onClick={function(e){
                                        NotificationResponse(JSON.stringify(false), notificationTab.walletUserId);    
                                    }}> 
                                    Odrzuć
                                </Button>
                            </div>
                        </div>      
                    )
                }
                {message}
                {/*
                    reminders.map(
                        reminderTab =>
                        <div key =  {Math.floor(Math.random() * 100000)} >
                            <div className="box-content">
                                <div className="text-label">NIEUREGULOWANA NALEŻNOŚĆ</div>
                                <div className="separator-line"></div>
                                <div className = "center-content error-text">Użytkownik: {reminderTab.sender.name} przypomina Ci o uregulowaniu należności </div>
                                <div>Portfel: {reminderTab.wallet.name}</div> 
                                <br />
                                <div>Wysokość należności: {reminderTab.content}</div> 
                                
                                <br />
                                <Button 
                                    className="card-link center-content btn btn-primary main-button-style"
                                    onClick={function(e){
                                        //NotificationResponse(JSON.stringify(true), reminderTab.walletUserId); 
                                    }}>
                                    Zaakceptuj
                                </Button>
                        
                                <Button className="card-link center-content btn btn-primary main-button-style" 
                                    onClick={function(e){
                                       //NotificationResponse(JSON.stringify(false), reminderTab.walletUserId);    
                                    }}> 
                                    Odrzuć
                                </Button>
                            </div>
                        </div>      
                    )
                                */}
              
            </Container>
        );
    
}

export default NotificationComponent;