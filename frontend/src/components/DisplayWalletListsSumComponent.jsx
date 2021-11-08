import React, { Component } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import ListsService from '../services/ListsService';
import UserService from '../services/user.service';
import { useState } from 'react';
import { useEffect } from 'react';
function DisplayWalletListsSumComponent (){
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [listsSumData, setListsSumData] = useState([])
    const [message, setMessage] = useState("")
    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("userToken:")
        console.log(userData.token)
        ListsService.getWalletLists(walletID,userData.token)
        .then((response)=>{
            console.log("Get expenseSum data (responseData)")
            console.log(response.data)
            console.log(response.data)
            setListsSumData(response.data)
            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])
    useEffect(()=>{
        console.log("listsSumData:")
        console.log(listsSumData)
        if(Object.keys(listsSumData).length === 0){
            setMessage("Brak list")
        }
        else setMessage("")
    },[listsSumData])
        return (
            <Container className="container">

            <div className=" text-size base-text center-content">{message}</div>
                 {                
                         listsSumData.map(
                             list =>
                            
                             <div key = {list.id} className = "box-subcontent-2 text-size">
                            <div className="center-content text-label">  <b>  {list.name}  </b></div >
                            <div className='separator-line'></div>
                            <Row>
                                <Col className="text-label right-content">Liczba pozycji:</Col >
                                <Col className="base-text left-content"> {list.listDetailSet.length}</Col>
                            </Row>    
                                
                              
                           
                           
                            <div className='separator-line'></div>
                           
                            <div className="center-content">
                                <a className="center-content href-text text-size"   
                                    onClick={(e)=>{
                                        sessionStorage.setItem('listID',JSON.stringify(list.id))
                                        window.location.href='/list-detail'
                                    }}>
                                     Zobacz szczegóły
                                    </a> 
                            </div>
                             
                            
                           </div> 
                                
                             
                         )   
             }

            </Container>
        );
    
}

export default DisplayWalletListsSumComponent;