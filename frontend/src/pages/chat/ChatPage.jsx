import React from 'react'
import { Container, Row, Col } from 'react-bootstrap';
import UserService from '../../services/user.service';
import { useEffect } from 'react';
import { useState } from 'react';
import Header from '../../components/Header';
import ChatService from '../../services/ChatService';
import {format} from 'date-fns'
import picture from '../../profile_picture_placeholder.jpg'
import ImageService from '../../services/ImageService';
import { Prev } from 'react-bootstrap/esm/PageItem';
let temp = []
function ChatPage() {
    
    const userData = UserService.getCurrentUser()
    let walletIdHelper = '';    
    if (sessionStorage && sessionStorage.getItem('walletID')) {
       walletIdHelper = JSON.parse(sessionStorage.getItem('walletID'));
      }
    const [isLoading, setIsLoading] = useState(true)
    const [messagesData, setMessagesData] = useState([])
    const [messagesData2, setMessagesData2] = useState([])
    const [message, setMessage] = useState({
        content: ""
    })
    const [arrayHelp, setArrayHelp]=useState([])
    const [profilePicsArray, setProfilePicsArray]= useState([])
    const [picsObj, setPicsObj] = useState({
        login: "",
        pictureUrl: "",
    })
    const [update, setUpdate]=useState(true)
    useEffect(()=>{
        var currentDateTime = format(new Date(), 'yyyy-MM-dd kk:mm:ss')
        const formatCurrentDateTime = currentDateTime.replace(/ /g,'T')
        console.log(formatCurrentDateTime)
        ChatService.getMessages(walletIdHelper, formatCurrentDateTime, userData.token)
        .then((response)=>{
           // console.log(response.data)
            setMessagesData(response.data)
            setIsLoading(false)
        })
        .catch((error)=>{
            console.log(error)
        })
    },[])
    const handleChange=(e)=>{
        console.log(e.target.value)
        setMessage({content: e.target.value})
        document.getElementById("messageInput").placeholder=""
    }
    const handleSendMessage=(e)=>{
        if(message.content.length !== 0){
            ChatService.sendMessage(walletIdHelper, message, userData.token)
            .then((response)=>{
                console.log(response.data)
                document.getElementById("messageInput").value=""
                setMessage({content: ""})
                //window.location.href="/chat"
                document.getElementById("messageInput").placeholder=""
            })
            .then((error)=>{
                console.log(error)
            })

            const timer = setTimeout(()=>{
                refreshChat(e)
                console.log("Refresh!")
            },500)
            return() => clearTimeout(timer)
        }
        else{
            console.log("Pusta wiadomość.")
            document.getElementById("messageInput").placeholder="Wpisz treść"
        }
    }
   
    const handleImageSrc=(img, login)=>{
        if(img===null){
            return picture
        }
        else{
            ImageService.getUserProfileImg(login, userData.token)
            .then((response)=>{
                if(arrayHelp.includes(login)){
                    // console.log("Jest na liscie")
                  //  console.log("Array help: "+ arrayHelp)
                }
                else{
                   // console.log("Brak na liście")
                    //setArrayHelp(prev => [...prev, login])
                }
               // window.location.temp.push(URL.createObjectURL(response.data))
               
                
              
                    //setTakenPics(prev => [...prev, login])
                   // setPicsObj({
                    //    login: login,
                     //   pictureUrl: URL.createObjectURL(response.data)
                   // })
                  //  setProfilePicsArray(prev=>[...prev, picsObj])
                
              //console.log("Taken pics: ",takenPics)
              //console.log("Array ProfilePics: ", profilePicsArray)
            })
            return picture
        }
    }
    const handleLoadMore = (e) =>{
        setIsLoading(true)
        var currentDateTime = format(new Date(), 'yyyy-MM-dd kk:mm:ss')
        const formatCurrentDateTime = currentDateTime.replace(/ /g,'T')
        var date = messagesData.at(-1).date
         var tempArray = messagesData
        setMessagesData([])
        
        ChatService.getMessages(walletIdHelper, date, userData.token)
        .then((response)=>{
            console.log(response.data)
            setMessagesData(response.data)
            //setMessagesData(oldData => [...oldData, response.data])
            
            setIsLoading(false)
            
            
        })
        .catch((error)=>{
            console.log(error)
        })
        
        
    }

    const renderMaping = (data) =>{
        return (
            data.map(
                singleMessageData=>
                
                singleMessageData.sender.login === userData.login ? (
                    <Row key={singleMessageData.id}>
                       
                     <Row>
                        <Col md={{span: 6, offset: 8 }} xs={{span: 5, offset:7 }}>
                            {singleMessageData.date.replace('T',' ')}
                        </Col>
                     </Row>
                     <Row>       
                        <Col md={{span: 5, offset: 6 }} xs={{span: 5, offset:5 }} className="box-subcontent-3">{singleMessageData.content}</Col>
                        <Col md="1" xs="2" ><img src={handleImageSrc(singleMessageData.sender.image, singleMessageData.sender.login)} className="icons-size profile-img-preview" alt="Pic" /></Col>
                     </Row>
                     <Row>
                         <Col md={{span: 5, offset:10 }} xs={{span: 5, offset:8 }}>{singleMessageData.sender.login}</Col>
                    </Row> 
                    
                 </Row> 
                ):(  
                <Row key={singleMessageData.id}>
                     <Row>
                        <Col md={{span: 4, offset: 2 }}>
                            {singleMessageData.date.replace('T',' ')}
                        </Col>
                     </Row>
                     <Row>
                        <Col md="1" xs="2" ><img src={handleImageSrc(singleMessageData.sender.image, singleMessageData.sender.login)} className="icons-size profile-img-preview" alt="Pic" /></Col>
                        <Col md={{span: 5, offset: 0 }} xs={{span: 6, offset:1 }} className="box-subcontent-3">{singleMessageData.content}</Col>
                     
                     </Row>
                     <Row>
                         <Col md={{span: 5, offset:0 }} xs={{span: 5, offset:1 }}>{singleMessageData.sender.login}</Col>
                    </Row>  
                 </Row> 
                   
             ))  
        )
    }
    const refreshChat = (e) =>{
        var currentDateTime = format(new Date(), 'yyyy-MM-dd kk:mm:ss')
        const formatCurrentDateTime = currentDateTime.replace(/ /g,'T')
        console.log(formatCurrentDateTime)
        ChatService.getMessages(walletIdHelper, formatCurrentDateTime, userData.token)
        .then((response)=>{
            console.log(response.data)
            setMessagesData(response.data)
            setIsLoading(false)
        })
        .catch((error)=>{
            console.log(error)
        })
    }
    return (
        <Container>
             <Header title={"Czat"}/> 
             <Container className="box-content">
                 <Row >
                     <Col md={{span: 4, offset: 5 }} xs={{span: 5, offset:4 }}>
                        <button className="main-button-style base-text" onClick={(e)=>{
                            handleLoadMore()
                        }}>Załaduj więcej</button>
                        
                     </Col>
                    
                 </Row>
                 <br />
                
                 <Row id="chat-container" className="chat-box" style={{
                     overflowY : "scroll",
                     overflowX : "hidden",
                  
                }}>
                    
                     {
                          isLoading || messagesData.length === 0 ? (<div className="text-size base-text center-content">Ładownie...</div>):(renderMaping(messagesData))    
                     }
                  
                   
                 </Row>
                  <br />
                 <Row>
                   

                    <Col md="2">
                        <button className="refresh-chat-icon icons-size-2" onClick={(e)=>{
                              refreshChat(e)
                            console.log("klik")
                        }}/>
                     </Col>
                     <Col md="8">
                        <input type="text" id="messageInput" className="width-100" style={{textAlign:'center'}}
                        onChange={(e)=>{
                            handleChange(e)
                        }}/>
                     </Col>
                     <Col md="2">
                        <button
                            className="send-message-icon icons-size-2" 
                            onClick={(e)=>{
                            handleSendMessage(e)
                            console.log("klik")
                        }}></button>
                     </Col>
                   
                 </Row>
                

             </Container>
        </Container>
    )
}

export default ChatPage
