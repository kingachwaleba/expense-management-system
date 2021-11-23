import React, { Component } from 'react';
import NotificationService from '../services/NotificationService';
import UserService from '../services/user.service';
import { useEffect } from 'react';
import { useState } from 'react';
import { Container, Col } from 'react-bootstrap';
import { Button } from 'reactstrap';




function NotificationComponent(){
    
    
    const [notification, setNotification] = useState([]);
    const [chatNotifications, setChatNotfications] = useState([])
    const [userToken, setUserToken] = useState("")
    const [message, setMessage] = useState("")
    const [reminders, setReminders] = useState("")
    const [messageReminder, setMessageReminder] = useState("")
    const [loading, setLoading] = useState("true")
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
                setLoading(false)
               })
               .catch((error)=>{
                   
                   console.log(error)
               })

               NotificationService.getChatNotifications(userData.token)
               .then((response)=>{
                   setChatNotfications(response.data)
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

    const renderReminders = (remindersData) =>{
        
            return (
                    remindersData.map(
                        reminderTab =>
                        <Col key =  {Math.floor(Math.random() * 100000)} >
                            <div className="box-content">
                                <div className="text-label">! NIEUREGULOWANA NALEŻNOŚĆ !</div>
                                <div className="separator-line"></div>
                                <div className = "center-content error-text">Masz nieuregulowaną należność! </div>
                                <div>Portfel: {reminderTab.wallet.name}</div> 
                            
                                <div>Wysokość należności: {reminderTab.content}</div> 
                             
                                <Button 
                                    className="card-link center-content text-size btn btn-primary main-button-style"
                                    onClick={function(e){
                                       
                                        sessionStorage.setItem('walletID',JSON.stringify(reminderTab.wallet.id))
                                        window.location.href='/wallet'
                                    }}>
                                    Przejdź do portfla
                                </Button>
                        
                                <Button className="card-link text-size center-content btn btn-primary main-button-style" 
                                    onClick={function(e){
                                       NotificationService.deleteNotification(reminderTab.id, userToken)
                                        .then(()=>{
                                            console.log("Usunięto powiadomienie.")
                                            window.location.href = "/profile"
                                        })
                                        .catch((error)=>{
                                            console.log(error)
                                        })  
                                    }}> 
                                    Usuń przypomnienie
                                </Button>
                            </div>
                        </Col>      
                    )
            )
    }

    const renderMessagesNotification = (data) =>{
        
        return (
                data.map(
                    Tab =>
                    <Col key =  {Math.floor(Math.random() * 100000)} >
                        <div className="box-content">
                            <div className="text-label">Nowe wiadomości</div>
                            <div className="separator-line"></div>
                         
                            <div>Portfel: {Tab.wallet.name}</div> 
                        
                            <div>Liczba nowych wiadomości: {Tab.content}</div> 
                         
                            <Button 
                                className="card-link center-content text-size btn btn-primary main-button-style"
                                onClick={function(e){
                                   
                                    sessionStorage.setItem('walletID',JSON.stringify(Tab.wallet.id))
                                    window.location.href='/wallet'
                                }}>
                                Przejdź do portfla
                            </Button>
                    
                            <Button className="card-link text-size center-content btn btn-primary main-button-style" 
                                onClick={function(e){
                                   NotificationService.deleteChatNotification(Tab.id, userToken)
                                    .then(()=>{
                                        console.log("Usunięto powiadomienie.")
                                        window.location.href = "/profile"
                                    })
                                    .catch((error)=>{
                                        console.log(error)
                                    })  
                                }}> 
                                Usuń powiadomienie
                            </Button>
                        </div>
                    </Col>      
                )
        )
}

    const renderInvitations = () => {

    }
        return (
            <Container>
                <div className="box-subcontent ">
                 Zaproszenia
                            <div className="separator-line"></div>
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
                                    className="card-link center-content btn text-size btn-primary main-button-style"
                                    onClick={function(e){
                                        NotificationResponse(JSON.stringify(true), notificationTab.walletUserId); 
                                    }}>
                                    Zaakceptuj
                                </Button>
                        
                                <Button className="card-link center-content text-size btn btn-primary main-button-style" 
                                    onClick={function(e){
                                        NotificationResponse(JSON.stringify(false), notificationTab.walletUserId);    
                                    }}> 
                                    Odrzuć
                                </Button>
                            </div>
                        </div>      
                    )
                }
                <div>
                     {message} 
                </div>
                </div> 
              
                <div className="box-subcontent">

              
               Przypomnienia o długach
                            <div className="separator-line"></div>
                {console.log("Przypomnienia:", reminders)}
                <div>
               {
                   reminders.length === 0 || loading === true ? (<div>Brak przypomnień</div>):(renderReminders(reminders))
               }
                  </div>        
                </div>
                <div className="box-subcontent">
                    Nowe wiadomości
                    <div className="separator-line"></div>
                    {console.log("Przypomnienia:", chatNotifications)}
                    <div>
                    {
                        chatNotifications.length === 0 || loading === true ? (<div>Brak nowych wiadomości</div>):(renderMessagesNotification(chatNotifications))
                    }
                    </div>        
                </div>
            </Container>
        );
    
}

export default NotificationComponent;