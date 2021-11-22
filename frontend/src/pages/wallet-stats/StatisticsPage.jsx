import React, { Component } from 'react';
import { Container, Col, Row } from 'react-bootstrap';
import Header from '../../components/Header';
import DatePicker, {registerLocale} from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css"
import pl from "date-fns/locale/pl";
import WalletService from '../../services/WalletService';
import UserService from '../../services/user.service';
import { useState } from 'react';
import { useEffect } from 'react';

registerLocale("pl",pl)
function StatisticsPage (){
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [selectedDate1, setSelectedDate1] = useState(null)
    const [selectedDate2, setSelectedDate2] = useState(null)
    const [statsData,setStatsData]=useState([])
    const [renderStats, setRenderStats] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")
    const months = ['Styczeń', 'Luty', 'Marzec','Kwiecień','Maj','Czerwiec','Lipiec','Sierpień','Wrzesień','Październik','Listopad','Grudzień']
    const days = ['Pn','Wt','Śr','Cz','Pt','Sb','Nd']
    const locale = {
        localize: {
            month: n => months[n],
            day: n=> days[n]
        },
        formatLong:{}
    }

    function getStats(fromDate, toDate){
        WalletService.getWalletStats(walletID, fromDate, toDate, userData.token)
        .then((response)=>{
           setStatsData(response.data)
            if(response.data.totalCost > 0) setRenderStats(true)
            else{
                setRenderStats(false)
                setErrorMessage("Brak danych w podanym przedziale czasowym.")
            } 
        })
        .catch(error=>{
            console.error({error})
            setErrorMessage(error.response.data)
        });
    }
        return (
            <Container>
                <Header title="Statystyki"/>
                <div className="box-content">
                    <h4 className="text-label center-content">Portfel - statystyki</h4>
                    <div className="separator-line"></div>
                    <div className="box-subcontent-2 base-text text-size center-content">
                        Wybierz przedział czasowy.
                    </div>
                    <br />
                    <Row  className="box-content">
                    <Col>
                        <div className="text-size base-text">Od</div>
                        <div className="center-content">
                            <DatePicker 
                            placeholderText="Wybierz początek"
                            locale='pl' 
                            className="box-subcontent-2 base-text text-size" 
                            selected={selectedDate1} 
                            onChange={date=>{   setSelectedDate1(date)
                                                setRenderStats(false)
                                                setErrorMessage("")}}
                            dateFormat="yyyy-MM-dd"
                            maxDate = {new Date()}
                            />
                        </div>
                    </Col>
                    <Col >
                    
                        <div className="text-size base-text">Do</div>
                        <div className="center-content">
                            <DatePicker 
                            placeholderText="Wybierz koniec"
                            locale='pl' 
                            className="box-subcontent-2 base-text text-size" 
                            dateFormat="yyyy-MM-dd"
                            selected={selectedDate2} 
                            onChange={date=> {setSelectedDate2(date)
                                              setRenderStats(false)
                                              setErrorMessage("")}}
                            minDate={selectedDate1}
                            maxDate = {new Date()}/>
                        </div>

                    </Col>
                    </Row>
                    <div className="box-subcontent-2">
                    <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                           
                            onClick={e =>  {
                                console.log("Data1:")
                                console.log(selectedDate1)
                                console.log("Data2:")
                                console.log(selectedDate2)
                                if(selectedDate1 && selectedDate2){
                               
                                let FromMonth = ('0'+(selectedDate1.getMonth()+1).toString()).slice(-2);
                                let ToMonth = ('0'+(selectedDate2.getMonth()+1).toString()).slice(-2);
                                let FromDay = ('0'+(selectedDate1.getDate()).toString()).slice(-2);
                                let ToDay = ('0'+(selectedDate2.getDate()).toString()).slice(-2);
                           
                                let FromDate =selectedDate1.getFullYear()+"-"+FromMonth+"-"+FromDay+"T00:00:00"
                                let ToDate = selectedDate2.getFullYear()+"-"+ToMonth+"-"+ToDay+"T23:59:59"
                                
                               getStats(FromDate, ToDate)
                               }else setErrorMessage("Wskaż przedział czasowy!")
                            }}
                            >
                           Pokaż statystyki
                        </button>
                        <div>
                            {
                                renderStats ? (
                                    <div className="text-size">
                                        <div className="grid-container">
                                            <div className="box-subcontent-2 text-label">Najwięcej wydali: </div>
                                            <div className="box-subcontent-2">{statsData.maxUsersList.map(user=> user+"  ") +" | "+ statsData.maxExpensesValue }zł</div>
                                        </div>
                                      <div className="separator-line"></div>
                                      <div className="box-subcontent">
                                      <div className="grid-container ">
                                            <div className="text-label">Łącznie wydano: </div>
                                            <div >{statsData.totalCost} zł</div>
                                        </div>
                                        <div className="separator-line-thin"></div>
                                        <div className="grid-container ">
                                            <div className="text-label">Bilety: </div>
                                            <div >{statsData.bilety} zł</div>
                                        </div>
                                        <div className="grid-container ">
                                            <div className="text-label">Chemia: </div>
                                            <div >{statsData.chemia} zł</div>
                                        </div>
                                        <div className="grid-container ">
                                            <div className="text-label">Prezent: </div>
                                            <div >{statsData.prezent} zł</div>
                                        </div>
                                        <div className="grid-container ">
                                            <div className="text-label">Samochód: </div>
                                            <div >{statsData.samochód} zł</div>
                                        </div>
                                        <div className="grid-container ">
                                            <div className="text-label">Spożywcze: </div>
                                            <div >{statsData.spożywcze} zł</div>
                                        </div>
                                        <div className="grid-container ">
                                            <div className="text-label">Wyjście: </div>
                                            <div >{statsData.wyjście} zł</div>
                                        </div>
                                      </div>
                                    </div>   
                                ):(
                                    <div className="text-size base-text center-content">
                                        {errorMessage}
                                    </div>
                                )
                            }
                        </div>
                    </div>
                    <div className="center-content">
                    <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            onClick={e =>  {
                                window.location.href='/wallet'
                            }}
                            >
                            Wróć
                        </button>
                </div>
                </div>
             
            </Container>
        );
}

export default StatisticsPage;