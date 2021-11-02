import React, { Component } from 'react';
import Header from '../../components/Header';
import { Container, Row, Col } from 'react-bootstrap';
import UnitService from '../../services/UnitsService.js';
import { useEffect } from 'react';
import UserService from '../../services/user.service';
import { useState } from 'react';
function CreateListPage () {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [units, setUnits] = useState([]);
    const [errorMessage, setErrorMessage]=useState("")
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
        return (
            <Container>
                <Header title ="Tworzenie listy"/> 
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Wydatki</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <h4> komponent z aktualnymi pozycjami na liście </h4>

                </div>
                <div className="box-content form-container" >

                                <div className="center-content">
                                    <h2 className="text-label ">Dodaj produkt do listy</h2> 
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