import React from 'react'
import { Container, Row, Col } from 'react-bootstrap';
import UnitsService from '../services/UnitsService';
import { useEffect } from 'react';
import UserService from '../services/user.service';
import { useState } from 'react';
function AddElementToListComponent() {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [units, setUnits] = useState([]);
    const [errorMessage, setErrorMessage]=useState("")
    const [inputName, setInputName] = useState("")
    const [inputQuantity, setInputQuantity] = useState("")
    const [inputCategory, setInputCategory] = useState([])
    useEffect(()=>{
       
        UnitsService.getUnits(userData.token)
        .then((response)=>{
            console.log(response.data)
            setUnits(response.data)
        })
       
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])





    return (
        <Container>
            
            <div className="center-content text-label text-size">
                                    Dodaj produkt do listy
                                </div>
                                <div className="separator-line"></div>
             <form name="form"
                        method="post"
                        //onSubmit={}
                        >
                        <div className={'form-group'}>
                            <label className="form-label text-size"  htmlFor="Name">Nazwa: </label>
                            <input
                               
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę"
                                minLength="1"
                                maxLength="45"
                                value = {inputName}
                                //onChange={(e)=>handleChangeName(e)}
                            />
                            
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label text-size" htmlFor="Quantity">Ilość: </label>
                            <input
                                
                                type="number"
                                step="0.01"
                                className="form-control"
                                name="price"
                                placeholder="Wpisz ilość"
                                maxLength="1000"
                                value={inputQuantity}
                                pattern="^\d{0,8}(\.\d{1,2})?$"
                                //onChange={(e)=>handleChangePrice(e)}
                            />
                            
                        </div>
                    <Container className = "box-subcontent">
                      
                    
                        <Row>
                        {
                         
                         units.map(
                            unit =>
                         
                            <Col key = {unit.id} className = "center-content custom-radiobuttons margin-left-text">
                               
                            <label className = "form-label text-size" htmlFor={unit.id}>
                              <input type="radio" id={unit.id} name="category" value={unit.name} 
                                   //defaultChecked = {handleDefaultCheck(category.name)}
                                  
                                 // onChange={(e)=>readExpenseCategory(e)}
                                  >
                                      
                              </input>
                              
                              <div className="checkmark icons-size-2"></div>
                               {unit.name}</label>
                              
                          </Col> 
                         
                         )   
             }
                   </Row> 
                </Container>    
               
                <div className="error-text text-size">
                    {errorMessage}
                </div>
                <br />
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>  {
                                window.location.href='/wallet' 
                            }}
                            >
                            Dodaj
                        </button>
                </form>
                                
                       
        </Container>
    )
}

export default AddElementToListComponent
