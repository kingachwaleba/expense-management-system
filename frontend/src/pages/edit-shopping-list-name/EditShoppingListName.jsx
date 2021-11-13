import React from 'react'
import { Container, Row, Col } from 'reactstrap'
import Header from '../../components/Header'
import ListsService from '../../services/ListsService'
import UserService from '../../services/user.service'
import { useState } from 'react'
function EditShoppingListName() {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    let listID = '';
    if (sessionStorage && sessionStorage.getItem('listID')) {
        listID = JSON.parse(sessionStorage.getItem('listID'));
    }
    let listName = '';
    if (sessionStorage && sessionStorage.getItem('listName')) {
        listName = JSON.parse(sessionStorage.getItem('listName'));
    }

    const[name, setName] = useState("listName")
    const[errorMessage, setErrorMessage] = useState("")
    const [submitted, setSubmitted] = useState(false)
const handleSubmit = (e) =>{
    e.preventDefault();
    var newName = document.createTextNode("nowanazwa")
    console.log(name)
    console.log(listID, newName, userData.token)
    //console.log(JSON.parse(newName))
    ListsService.editListName(listID,newName, userData.token)
    .then((response)=>{
        console.log(response)
    })
    .catch((error)=>{
        console.log(error)
        console.log(error.response.data)
    })
}
const handleChange = e => {
    setName(e.target.value)
  
}
    return (
        <Container className="base-text text-size">
                   <Header title ="Edytuj listę"/>
                   <div className="box-content">
                   <form name="form"
                        method="post"
                        onSubmit={(e)=>handleSubmit(e)}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                               
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder={listName}
                                minLength="1"
                                maxLength="45"
                                onChange={(e)=>handleChange(e)}
                            />
                            
                        </div>

                   
               
                <div className="error-text text-size">
                    {errorMessage}
                </div>
                <br />
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>{
                                console.log("Wysłano prośbę")
                               
                            }}
                            >
                            Zapisz zmiany
                        </button>
                </form>


                {/* *
                    <form   
                        name="form"
                     
                        onSubmit={handleSubmit}>
                        <div className={'form-group'}>
                            <label className="form-label text-size"  htmlFor="Name">Nazwa: </label>
                            <input
                               required
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder={listName}
                                minLength="1"
                                maxLength="30"
                                //value ={listName}
                               onChange={handleChange}
                            />
                         
                        </div>
                        <br />
                        <div className="center-content">
                            <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                            type="submit"
                            onClick={(e)=>{
                                console.log("Wysłano prośbę o zmianę nazwy")
                            }}>Zapisz zmiany</button>
                        </div> 
                    </form>  
                        */}
                    <br />
                    <div className="center-content">
                        <button className="card-link center-content btn btn-primary width-100 text-size main-button-style" 
                        type="button"
                        onClick={(e)=>{
                            window.location.href="/list-detail"
                        }}>Anuluj</button>
                    </div>
                    </div>
        </Container>
    )
}

export default EditShoppingListName
