import React, { Component } from 'react';
import Header from '../../components/Header';
import { Container, Row, Col } from 'react-bootstrap';
import UnitService from '../../services/UnitsService.js';
import { useEffect } from 'react';
import UserService from '../../services/user.service';
import { useState } from 'react';
import AddElementToListComponent from '../../components/AddElementToListComponent';
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

                                <AddElementToListComponent/>
                   

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