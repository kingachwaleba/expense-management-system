import React, { Component } from 'react';
import Header from '../../components/Header';
import UnitService from '../../services/UnitsService.js';
import { useEffect } from 'react';
import UserService from '../../services/user.service';
import { useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import AddElementToListComponent from '../../components/AddElementToListComponent';
import EditElementListComponent from '../../components/EditElementListComponent';
import {Unit} from '../../models/unit';
import ListsService from '../../services/ListsService'

function CreateListPage () {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [units, setUnits] = useState([]);
    const [errorMessage, setErrorMessage]=useState("")
    const [errorMessageCreateList, setErrorMessageCreateList]=useState("")
    const [edit, setEdit] = useState([])
    const [listName, setListName] = useState([])
  
    useEffect(()=>{
       
        UnitService.getUnits(userData.token)
        .then((response)=>{
            console.log(response.data)
            setUnits(response.data)
        })
       
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])
    

    const [currentElements, setCurrentElements] = useState([])

    const addElement = element =>{
        if(!element.name || !element.quantity || !element.categoryName || !element.categoryId)return
        const newElement = [element, ...currentElements]
        setCurrentElements(newElement);
        console.log(currentElements);
    }


    useEffect(()=>{
        if(currentElements.length == 0)setErrorMessage("Brak elementów na liście")
        else setErrorMessage("")
    },[currentElements])

    const removeElement = id => {
        const removeArr = [...currentElements].filter(element=> element.id !== id)
        setCurrentElements(removeArr)
    }

    const updateElement = (elementId, newElement) =>{
        if(!newElement.name || !newElement.quantity || !newElement.categoryName || !newElement.categoryId)return
       
        setCurrentElements(prev=>prev.map(item => (item.id === elementId ? newElement: item)));
        console.log(currentElements);
    }

    const submitUpdate = newElement => {
        updateElement(edit.id, newElement)
        setEdit({
            id: null,
            name: "",
            quantity: "",
            categoryName: "",
            categoryId: ""
        })
    }
 
    const cancelUpdate = e => {
        setEdit({
            id: null,
            name: "",
            quantity: "",
            categoryName: "",
            categoryId: ""
        })
    }

    function handleListNameChange(e){
        setErrorMessageCreateList("")
        setListName(e.target.value)
    }
 
    function handleCreateList(){
        console.log(currentElements)
            let listDetailList = []
            currentElements.forEach(function (element){
                if(element.name || element.categoryName || element.categoryId || element.quantity){
                let unit = new Unit(element.categoryId, element.categoryName)
                let arrayEl = {
                    name: element.name,
                    quantity: element.quantity,
                    unit: unit
                }
                console.log(arrayEl)
                listDetailList.push(arrayEl)}      
            })
            let listHolder = {
                list:{
                    name: listName
                },
                listDetailList: listDetailList
            }
            console.log("ListHolder:")
            console.log(listHolder)
            if(listName.length != 0){
                ListsService.createList(walletID, listHolder, userData.token)
                .then(()=>{
                    window.location.href='/wallet'
                    setCurrentElements([])
                })
                .catch(error=>{
                    console.error({error})
                });}
            else setErrorMessageCreateList("Podaj nazwę listy!")
    }

    if(edit.id){
      
        return( 
             <div className="box-content form-container" >
             <EditElementListComponent edit={edit} onSubmit={submitUpdate} title="Edytuj element" currentName={edit.name} currentQuantity={edit.quantity} currentCategoryName={edit.categoryName} currentCategoryId={edit.categoryId} cancel={cancelUpdate}/>
             </div>
        )
        
    }
        return (
            <Container>
                <Header title ="Tworzenie listy"/> 
                <div className="box-content">


                <div className={'form-group'}>
                <div className="center-content text-label text-size">
                                   Utwórz listę:
                                </div>
                               
                                <div className="separator-line"></div> 
                                
                            <input
                               required
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę listy..."
                                minLength="1"
                                maxLength="45"
                                value = {listName}
                                onChange={(e)=>handleListNameChange(e)}
                            />
                            <br />
                        </div>
                        </div>
                        <div className="box-content">
                                <div className="center-content text-label text-size">
                                    Dodane produkty:
                                </div>
                               
                                <div className="separator-line"></div> 
                                <div className="center-content base-text text-size">{errorMessage}</div>
                                <Container>
                                { 
                                currentElements.map(
                                    element =>
                                    <div key = {element.id} className = "text-size">
                                    
                                        <Row  className="grid-container">
                                            <Col className="center-content"> 
                                                <button className="delete-list-element icons-size-2"
                                                    onClick={(e)=>{ removeElement(element.id)
                                                    
                                           

                                                }}></button>
                                    
                                             </Col>
                                            <Col> 
                                                <button className="edit-list-element icons-size"
                                                    onClick={(e)=>{
                                                    
                                                        setEdit({
                                                            id: element.id,
                                                            name: element.name,
                                                            quantity: element.quantity,
                                                            categoryName: element.categoryName,
                                                            categoryId: element.categoryId
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
                <div className="box-content form-container" >

                                <AddElementToListComponent onSubmit={addElement} title="Dodaj element"/>
                   

                </div>
                <br />
                <div className="center-content">
                    <div className="error-text text-size">
                        {errorMessageCreateList} 
                       
                    </div>
                    <br/>
                <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "button"
                            onClick={e =>  {
                                console.log(currentElements)
                                handleCreateList()
                             
                            }}
                            >
                            Utwórz
                        </button>
                </div>
                <br />
                <div className="center-content">
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "button"
                            onClick={e =>  {
                                window.location.href='/wallet'
                            }}
                            >
                            Anuluj
                        </button>
                </div>
                
                
            </Container>
        );
    
}

export default CreateListPage;