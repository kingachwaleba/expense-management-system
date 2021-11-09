import React, { Component } from 'react';
import Header from '../../components/Header';
import UserService from '../../services/user.service';
import { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import ListsService from '../../services/ListsService';
import EditListDetailElementComponent from '../../components/EditListDetailElementComponent';
import AddElementToExistingListComponent from '../../components/AddElementToExistingListComponent';
import StatusService from '../../services/StatusService';

import { useLayoutEffect } from 'react';

function ListDetailPage () {
    let counter = 0;
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    let listID = '';
    if (sessionStorage && sessionStorage.getItem('listID')) {
        listID = JSON.parse(sessionStorage.getItem('listID'));
    }
    const[refresh, setRefresh] = useState(false)
    const[listDetailData, setListDetailData] = useState([])
    const[status, setStatus] = useState([])
    const[edit, setEdit] = useState([])
    const[errorMessage,setErrorMessage] = useState("")
    const[errorManageElementMessage,setErrorManageElementMessage] = useState("")
    const[loading, setLoading] = useState(true)
    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("userToken:")
        console.log(userData.token)
       // let isMounted = true;
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
           
                setListDetailData(response.data.listDetailSet)
                setLoading(false)
        })
        .catch(error=>{
            console.error({error})
        })
        
        //Promise.all - js -doczytać 
        StatusService.getStatues()
        .then((response)=>{
            setStatus(response.data)
            console.log("Statusy", status)
           // console.log(response.data)
        })
        .catch(error=>{
            console.error({error})
        });      
       
    },[])
/*
    useEffect(()=>{ 
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
          
            //setListDetailData(response.data.listDetailSet)
            loadListDetailData(response.data.listDetailSet)
            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[status])
    */
    
    const counterIncrease =()=>{
        counter++
    }

   
    useEffect(()=>{ 
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
          
            let isMounted = true;
            ListsService.getListDetail(listID,userData.token)
            .then((response)=>{
            if(isMounted)setListDetailData(response.data.listDetailSet)
            console.log("Get expenseSum data (responseData)")
            //console.log(response.data)
            
            //setListDetailData(response.data.listDetailSet)
            //console.log(response.data.listDetailSet)
            
        })
        .catch(error=>{
            console.error({error})
        })
        return()=>{isMounted = false}

            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[edit])
/*
    useEffect(()=>{ 
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
          
            setListDetailData(response.data.listDetailSet)
            setErrorMessage("")
            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[refresh])
    */


    const sleep = (milliseconds) => {
        return new Promise(resolve => setTimeout(resolve, milliseconds))
    }
    
    const loadListDetailData = (data) =>{
        sleep(5000).then(()=>{
             setListDetailData(data)
             setLoading(false)
        
        }
            
          
           
        )
        counter++
    }
    //------------------------
    const addElement = element =>{
        if(!element.name || !element.quantity)return
        //const newElement = [element, ...currentElements]
        //setCurrentElements(newElement);
        //console.log(currentElements);
        ListsService.addListElement(listID, element, userData.token)
        .then((response)=>{
            
            console.log(response.data)
            if(refresh) setRefresh(false)
            else setRefresh(true)
            setErrorMessage("")
            
        })
        .catch(error=>{
            console.error({error})
            setErrorMessage(error.response.data)
        });
    }
    /*
    useLayoutEffect(() => {
         ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
          
            setListDetailData(response.data.listDetailSet)
            setErrorMessage("")
            
        })
        .catch(error=>{
            console.error({error})
        });
             
    }, [])
   */
    const submitUpdate = newElement => {
        console.log(newElement)
        console.log(listID)
        ListsService.editListElement(edit.id, newElement, userData.token)
        .then((response)=>{
            
            console.log(response.data)
            setErrorMessage("")
            
        })
        .catch(error=>{
            console.error({error})
            setErrorMessage(error.response.data)
        });
      
       setEdit({
            id: null,
            name: "",
            quantity: "",
            unit:{
                id:"",
                name:""
            },
            status:{
                id:"",
                name:""
            },
            user:null
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
const renderMaping = (listData) =>{
    
    return(
                                
        listData.map(
            element1 =>
            <div key = {element1.id} className = "text-size">
            
            {console.log("Element1: ",element1)}
            {console.log("Status: ",Object.values(status))}
                <Row>
                    <Col xs="1"> 
                        <button 
                        className={`delete-list-element icons-size-2 left-content ${element1.status.id == Object.values(status)[1].id || element1.status.id == Object.values(status)[2].id ? "grey-scale" : ""}`}
                            disabled={element1.status.id == Object.values(status)[1].id || element1.status.id == Object.values(status)[2].id}
                            onClick={(e)=>{
                                if(window.confirm('Czy chcesz usunąć ten element z listy zakupów?')){
                                ListsService.deleteListElement(element1.id, userData.token)
                                .then((response)=>{
                                    
                                    console.log(response.data)
                                    if(refresh) setRefresh(false)
                                    else setRefresh(true)
                                    
                                })
                                .catch(error=>{
                                    console.error({error})
                                    setErrorManageElementMessage(error.response.data)
                                });
                            }}
                        }
                            title="Usuń element"
                        ></button>

                     </Col>
                    <Col xs="1"> 
                        <button 
                        className={`edit-list-element icons-size left-content ${element1.status.id == Object.values(status)[1].id || element1.status.id == Object.values(status)[2].id ? "grey-scale" : ""}`}
                        title="Edytuj element"
                        disabled={element1.status.id == Object.values(status)[1].id || element1.status.id == Object.values(status)[2].id}
                            onClick={(e)=>{
                            if(element1.status.id == Object.values(status)[0].id){
                                setEdit({
                                    id: element1.id,
                                    name: element1.name,
                                    quantity: element1.quantity,
                                      unit:{
                                        id: element1.unit.id,
                                        name: element1.unit.name
                                    },
                                    status:{
                                        id: element1.status.id,
                                        name: element1.status.name
                                    },
                                    user:element1.user
                                })
                                setErrorMessage("")
                            }
                            else(setErrorMessage("Nie możesz edytować tego elementu."))

                        }}></button>
                    </Col>
                    <Col xs="3" className="center-content ">{element1.name}</Col>
                   
                    <Col xs="1" className="center-content">{element1.quantity}</Col >

                    <Col xs="3" className="right-content">{element1.unit.name}</Col>
                    <Col xs="1" >
                                    <div className="form-container">
                                        <div  className = "custom-checkboxes-list-buy margin-left-text " >
                                        <label className = "form-label text-size " htmlFor={element1.name+"-booking"}>
                                        <input type="checkbox" id={element1.name + "-booking"} name="users"
                                            defaultChecked={element1.status.id == Object.values(status)[2].id || element1.status.id == Object.values(status)[1].id}
                                            title={element1.user + " zadeklarował kupno."}
                                            
                                            onChange={(e)=>{
                                                if(e.target.checked){
                                                    console.log(element1.id)
                                                    console.log(Object.values(status)[1].id)
                                                    
                                                    ListsService.changeElementStatus(element1.id, Object.values(status)[1].id, userData.token)
                                                    .then((response)=>{
                                    
                                                        console.log(response.data)
                                                        if(refresh) setRefresh(false)
                                                        else setRefresh(true)
                                                        
                                                    })
                                                    .catch(error=>{
                                                        console.error({error})
                                                        e.target.checked=false
                                                        setErrorManageElementMessage(error.response.data)
                                                    });
                                                }
                                                else{
                                                 
                                                }

                                            }}
                            >
                                
                            </input>
                        
                        <div className="checkmark-checkbox-list-buy icons-size-2"></div>
                        </label>
                        </div>
                        </div>
                    </Col>
                    <Col xs="1">
                                    <div className="form-container">
                                        <div  className = "custom-checkboxes-list-buy margin-left-text" >
                                        <label className = "form-label text-size " htmlFor={element1.name+"-buy"}>
                                        <input type="checkbox" id={element1.name+"-buy"} name="users"
                                            defaultChecked={element1.status.id == Object.values(status)[2].id}
                                            disabled={element1.status.id == Object.values(status)[2].id}
                                            onChange={(e)=>{
                                                if(e.target.checked){
                                    
                                                    
                                                }
                                                else{
                                                    
                                                }

                                            }}
                            >
                                
                            </input>
                        
                        <div className="checkmark-checkbox-list-buy icons-size-2"></div>
                        </label>
                        </div>
                        </div>
                    </Col>
                    <div className="separator-line-thin"></div>
                </Row>  
        </div> 
    ))
}

    
    if(edit.id){
      
        return( 
             <div className="box-content form-container" >
             <EditListDetailElementComponent edit={edit} onSubmit={submitUpdate} title="Edytuj element"
                currentName={edit.name} 
                currentQuantity={edit.quantity} 
                currentUnit={edit.unit}
                currentStatus={edit.status}
                currentUser={edit.user}
                cancel={cancelUpdate}/>
             </div>
        )
        
    }
    //------------------------
        return (
            <Container>
                <Header title ="Lista zakupów"/>
                 
                <div className="box-content">
                    <Row>
                                <Col md = "2" xs="1"> 
                                                <button className="delete-list-element icons-size-2"
                                                   // onClick={(e)=>{ removeElement(element.id)}}
                                                title="Usuń listę"
                                                ></button>

                                </Col>
                                <Col md="6"xs="6" className="center-content">
                                    <h2 className="text-label text-size">Lista zakupów</h2> 
                                </Col>
                               
                                <Col md = "2" xs="1">
                                <div  className = "form-container left-content custom-checkboxes-list-buy">
                                    <label className = "form-label text-size" htmlFor="confirm-buy-list">
                                        <input type="checkbox" id="confirm-buy-list" name="users"
                                        //defaultChecked={currentExpenseUsersList.includes(user.login)}
                                        
                                        //onChange={(e)=>handleCreateExpenseUsersList(e)}
                                        >
                                            
                                        </input>
                                    
                                    <div className="checkmark-checkbox-list-buy icons-size-2"></div>
                                    </label>
                                    </div>
                                </Col>
                                <Col md = "2" xs="1">
                                <div  className = "form-container left-content custom-checkboxes-list-buy">
                                    <label className = "form-label text-size" htmlFor="confirm-buy-list">
                                        <input type="checkbox" id="confirm-buy-list" name="users"
                                        //defaultChecked={currentExpenseUsersList.includes(user.login)}
                                        
                                        //onChange={(e)=>handleCreateExpenseUsersList(e)}
                                        >
                                            
                                        </input>
                                    
                                    <div className="checkmark-checkbox-list-buy icons-size-2"></div>
                                    </label>
                                    </div>
                                </Col>
                    </Row>        
                                <div className="separator-line"></div>
                                <Container>
                                {console.log(listDetailData)}
                                {console.log(counter)}
                                {console.log("Dane:" ,  loading, listDetailData, status)}
                                {

                                        loading || listDetailData.length === 0 || status.length === 0 ? (<div></div>):(renderMaping(listDetailData))    
                               
                                }
                            </Container>

                </div>
                <div className="error-text text-size">
                        {errorMessage} 
                        <br/>
                </div>
                <div className="box-content form-container">
                              <AddElementToExistingListComponent onSubmit={addElement} title="Dodaj element"/>

                </div>
               
                <div className="center-content">
                    <a href="/wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Wróć</a>
                </div>
                
                
            </Container>
        );
    
}

export default ListDetailPage;
