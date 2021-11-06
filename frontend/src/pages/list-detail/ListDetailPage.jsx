import React, { Component } from 'react';
import Header from '../../components/Header';
import UserService from '../../services/user.service';
import { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import ListsService from '../../services/ListsService';
function ListDetailPage () {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    let listID = '';
    if (sessionStorage && sessionStorage.getItem('listID')) {
        listID = JSON.parse(sessionStorage.getItem('listID'));
    }
    const[listDetailData, setListDetailData] = useState([])
    const[edit, setEdit] = useState([])
    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("userToken:")
        console.log(userData.token)
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
            console.log("Get expenseSum data (responseData)")
            console.log(response.data)
            console.log(response.data)
            setListDetailData(response.data.listDetailSet)
            console.log(response.data.listDetailSet)
            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])
    
        return (
            <Container>
                <Header title ="Lista zakupów"/> 
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Lista zakupów</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <Container>
                                { 
                                listDetailData.map(
                                    element =>
                                    <div key = {element.id} className = "text-size">
                                    
                                        <Row  className="grid-container">
                                            <Col> 
                                                <button className="delete-user-icon icons-size-2"
                                                   // onClick={(e)=>{ removeElement(element.id)}}
                                      
                                                ></button>
                                    
                                             </Col>
                                            <Col> 
                                                <button className="delete-user-icon icons-size-2"
                                                    onClick={(e)=>{
                                                    
                                                        setEdit({
                                                            id: element.id,
                                                            name: element.name,
                                                            quantity: element.quantity,
                                                            unit:{
                                                                id: element.unit.id,
                                                                name: element.unit.name
                                                            }
                                                        
                                                        })

                                                }}></button>
                                            </Col>
                                            <Col className="center-content">{element.name}</Col>
                                            <Col className="right-content">{element.quantity}</Col >
                                            <Col className="left-content">{element.categoryName}</Col>
                                        </Row>  
                                </div> 
                            )}
                            </Container>

                </div>
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Dodaj produkt do listy</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <h4> komponent z formularzem dodawania produktu </h4>

                </div>
               
                <div className="center-content">
                    <a href="/wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Wróć</a>
                </div>
                
                
            </Container>
        );
    
}

export default ListDetailPage;