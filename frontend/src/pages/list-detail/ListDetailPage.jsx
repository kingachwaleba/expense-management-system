import React, { Component } from 'react';
import Header from '../../components/Header';
import UserService from '../../services/user.service';
import { useEffect, useState } from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import ListsService from '../../services/ListsService';
import EditListDetailElementComponent from '../../components/EditListDetailElementComponent';
import AddElementToExistingListComponent from '../../components/AddElementToExistingListComponent';
import StatusService from '../../services/StatusService';


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
    const[refresh, setRefresh] = useState(false)
    const[listDetailData, setListDetailData] = useState([])
    const[status, setStatus] = useState([])
    const[edit, setEdit] = useState([])
    const[errorMessage,setErrorMessage] = useState("")
    const[errorManageElementMessage,setErrorManageElementMessage] = useState("")
    const[loading, setLoading] = useState(true)
    const[listName, setListName]=useState("")
    const[listData, setListData]=useState([])
    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("userData:")
        console.log(userData)
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
                console.log(response.data)
                setListDetailData(response.data.listDetailSet)
                setListName(response.data.name)
                setListData(response.data)
                setLoading(false)
        })
        .catch(error=>{
            console.error({error})
        })
        
        //Promise.all - js -doczytać 
        StatusService.getStatues()
        .then((response)=>{
            setStatus(response.data)
            //console.log("Statusy", status)
           // console.log(response.data)
        })
        .catch(error=>{
            console.error({error})
        });      
       

    },[])

    useEffect(()=>{ 
        ListsService.getListDetail(listID,userData.token)
        .then((response)=>{
          
         
            ListsService.getListDetail(listID,userData.token)
            .then((response)=>{
            setListDetailData(response.data.listDetailSet)
         
        })
        .catch(error=>{
            console.error({error})
        })
    

            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[edit])

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
    


  
    //------------------------
    const addElement = element =>{
        if(!element.name || !element.quantity)return
   
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

    const submitUpdate = newElement => {
        ListsService.editListElement(edit.id, newElement, userData.token)
        .then(()=>{
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
    const setHoverTitle = (username, status,forElement)=>{
        if(forElement){
            if(username === userData.login && status === 'zarezerwowany'){
                return "Ten produkt jest zarezerwowany przez Ciebie"
            }
            else if(username === userData.login && status === 'zrealizowany'){
                return "Ten produkt został zakupiony przez Ciebie."
            }
            else if(status === 'zarezerwowany'){
                return `Ten produkt został zarezerwowany przez: ${username}`
            }
            else if(status ===  'zrealizowany'){
                return `Ten produkt został już zakupiony przez: ${username}`
            }
        }
        else{
            if(username === userData.login && status === 'zarezerwowany'){
                return "Lista jest zarezerwowana przez Ciebie"
            }
            else if(username === userData.login && status === 'zrealizowany'){
                return "Lista została zrealizowana przez Ciebie."
            }
            else if(status === 'zarezerwowany'){
                return `Lista została zarezerwowana przez: ${username}`
            }
            else if(status ===  'zrealizowany'){
                return `Lista została zrealizowana przez: ${username}`
            }
        }
    }
const renderMaping = (listData) =>{
    
    return(
                                
        listData.map(
            element1 =>
            <div key = {element1.id} className = "text-size">
            
            {console.log("Element1: ",element1)}
        
                <Row title={element1.user ? (setHoverTitle(element1.user.login, element1.status.name, true)):("Ten produkt czeka na zakup.")}>
                    <Col xs="1"> 
                        <button 
                        className={`delete-list-element icons-size-2 left-content ${element1.status.id === Object.values(status)[1].id || element1.status.id === Object.values(status)[2].id ? "grey-scale" : ""}`}
                            disabled={element1.status.id === Object.values(status)[1].id || element1.status.id === Object.values(status)[2].id}
                            onClick={(e)=>{
                               
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
                            }
                        }
                            title="Usuń element"
                        ></button>

                     </Col>
                    <Col xs="1"> 
                        <button 
                        className={`edit-list-element icons-size left-content ${element1.status.id === Object.values(status)[2].id ? "grey-scale" : ""}`}
                        title="Edytuj element"
                        disabled={element1.status.id === Object.values(status)[2].id}
                            onClick={(e)=>{
                            if(element1.status.id === Object.values(status)[0].id){
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
                                        <div  className ={ `custom-checkboxes-list-book margin-left-text ${element1.status.id === Object.values(status)[2].id ? "grey-scale" : ""}` }>
                                        <label className = "form-label text-size " htmlFor={element1.name+"-booking"}>
                                        <input type="checkbox" id={element1.name + "-booking"} name="users"
                                            defaultChecked={element1.status.id === Object.values(status)[1].id}
                                            
                                            disabled={element1.status.id === Object.values(status)[2].id || (element1.user ? (element1.user.login !== userData.login):(""))}
                                            onChange={(e)=>{
                                                if(e.target.checked){
                                                    console.log(element1.id)
                                                   
                                                    
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
                                                    console.log(element1.id)
                                            
                                                    
                                                    ListsService.changeElementStatus(element1.id, Object.values(status)[0].id, userData.token)
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

                                            }}
                            >
                                
                            </input>
                        
                        <div className="checkmark-checkbox-list-book icons-size-2"></div>
                        </label>
                        </div>
                        </div>
                    </Col>
                    <Col xs="1">
                                    <div className="form-container">
                                        <div  className = "custom-checkboxes-list-buy margin-left-text" >
                                        <label className = "form-label text-size " htmlFor={element1.name+"-buy"}>
                                        <input type="checkbox" id={element1.name+"-buy"} name="users"
                                            defaultChecked={element1.status.id === Object.values(status)[2].id}
                                            disabled={element1.user ? (element1.user.login !== userData.login):("")}
                                            onChange={(e)=>{
                                                if(e.target.checked){
                                                   
                                                   
                                                    
                                                    ListsService.changeElementStatus(element1.id, Object.values(status)[2].id, userData.token)
                                                    .then((response)=>{
                                    
                                                        //console.log(response.data)
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
                                                   // console.log(element1.id)
                                                    //console.log(Object.values(status)[2].id)
                                                    
                                                    ListsService.changeElementStatus(element1.id, Object.values(status)[0].id, userData.token)
                                                    .then((response)=>{
                                    
                                                        //console.log(response.data)
                                                        if(refresh) setRefresh(false)
                                                        else setRefresh(true)
                                                        
                                                    })
                                                    .catch(error=>{
                                                        console.error({error})
                                                        e.target.checked=false
                                                        setErrorManageElementMessage(error.response.data)
                                                    });
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


    const renderListManagement = (listData) => {
        return(
            <Row title={listData.user ? (setHoverTitle(listData.user.login, listData.status.name, false)):("")}>
                <Col md = "2" xs="1"> 
                <button className="delete-list-element icons-size-2"
                 onClick={(e)=>{
                    if (window.confirm('Czy chcesz usunąć listę?')){
                        ListsService.deleteList(listID, userData.token)
                        .then(()=>{
                            console.log("Usunięto listę.")
                            window.location.href = "/wallet"
                        })
                        .catch((error)=>{
                            console.log(error)
                        })
                    }
                 }}
                title="Usuń listę"
                ></button>

                </Col>
                <Col md="6"xs="6" className="center-content">
                    <div className="text-label text-size">{listData.name}</div> 
                </Col>


                <Col md = "2" xs="1">
                    <div  className = "form-container left-content custom-checkboxes-list-book">
                    <label className = {`form-label text-size ${listData.status.id === Object.values(status)[2].id ? "grey-scale" : ""}`} 
                    htmlFor="confirm-book-list">
                            <input type="checkbox" id="confirm-book-list" name="users"
                            defaultChecked={listData.status.id === Object.values(status)[1].id}
                            disabled={listData.status.id === Object.values(status)[2].id || (listData.user ? (listData.user.login !== userData.login):(""))}
                            onChange={(e)=>{
                                
                                if(e.target.checked){  
                                    ListsService.changeListStatus(listID, Object.values(status)[1].id, userData.token)
                                    .then(()=>{
                                        if(refresh) setRefresh(false)
                                        else setRefresh(true)
                                        window.location.href="/list-detail"
                                    })
                                    .catch(error=>{
                                        console.error({error})
                                        e.target.checked=false
                                        setErrorManageElementMessage(error.response.data)
                                    });
                                }
                                else{
                                    ListsService.changeListStatus(listID, Object.values(status)[0].id, userData.token)
                                    .then(()=>{
                                        if(refresh) setRefresh(false)
                                        else setRefresh(true)
                                        window.location.href="/list-detail"
                                    })
                                    .catch(error=>{
                                        console.error({error})
                                        e.target.checked=false
                                        setErrorManageElementMessage(error.response.data)
                                    });
                                }
                            }}
                            />    
                            <div className="checkmark-checkbox-list-book icons-size-2"></div>
                    </label>
                    </div>
                </Col>
                <Col md = "2" xs="1">
                    <div  className = "form-container left-content custom-checkboxes-list-buy">
                        <label className = "form-label text-size" htmlFor="confirm-buy-list">
                            <input type="checkbox" id="confirm-buy-list" name="users"
                           
                            defaultChecked={listData.status.id === Object.values(status)[2].id}
                            disabled={listData.user ? (listData.user.login !== userData.login):("")}
                            onChange={(e)=>{
                                
                                if(e.target.checked){  
                                    ListsService.changeListStatus(listID, Object.values(status)[2].id, userData.token)
                                    .then(()=>{
                                        if(refresh) setRefresh(false)
                                        else setRefresh(true)
                                        window.location.href="/list-detail"
                                    })
                                    .catch(error=>{
                                        console.error({error})
                                        e.target.checked=false
                                        setErrorManageElementMessage(error.response.data)
                                    });
                                }
                                else{
                                    ListsService.changeListStatus(listID, Object.values(status)[0].id, userData.token)
                                    .then(()=>{
                                        if(refresh) setRefresh(false)
                                        else setRefresh(true)
                                        window.location.href="/list-detail"
                                    })
                                    .catch(error=>{
                                        console.error({error})
                                        e.target.checked=false
                                        setErrorManageElementMessage(error.response.data)
                                    });
                                }
                            }}
                            >
                                
                            </input>

                            <div className="checkmark-checkbox-list-buy icons-size-2"></div>
                        </label>
                    </div>
                </Col>
            </Row>
        )
    }
    //------------------------
        return (
            <Container>
                <Header title ="Lista zakupów"/>
                { 
                            !listName  ? (
                                   <div  className="container box-content text-size base-text center-content"> 

                                       <div>Wystąpił błąd podczas wczytywania danych</div> 
                                       <br />
                                       <button className="card-link main-button-style center-content btn btn-primary text-size" type="button" onClick={(e)=>{
                                           window.location.href='/wallet'
                                       }}>
                                           Wróć na stronę portfela
                                       </button>
                                   </div>
                                       
                                    
                                ):(
                 <Col>
                <div className="box-content">
                    {
                        loading || listData.length === 0 || status.length === 0 ? (<div className="text-size base-text center-content">Wczytywanie...</div>):(renderListManagement(listData))
                    }
                             
                       
                                <div className="separator-line"></div>
                                <Container>
                                {console.log(listDetailData)}
                              
                                {//console.log("Dane:" ,  loading, listDetailData, status)
                                }


                                {

                                        loading || listDetailData.length === 0 || status.length === 0 ? (<div className="text-size base-text center-content">Brak elementów na liście.</div>):(renderMaping(listDetailData))    
                               
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
                <br />
                <div className="center-content">
                    <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                    type="button"
                    onClick={(e)=>{
                        sessionStorage.setItem('listName',JSON.stringify(listName))
                        window.location.href="/edit-shopping-list-name"
                    }}>Edytuj listę</button>
                </div>
                <br />
                <div className="center-content">
                    <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                    type="button"
                    onClick={(e)=>{
                        window.location.href="/wallet"
                    }}>Wróć</button>
                </div>
                
               </Col>)
}
            </Container>
        );
    
}

export default ListDetailPage;
