import React, { Component } from 'react';
import Header from '../../components/Header';
import DisplayWalletDataComponent from '../../components/DisplayWalletDataComponent';
import DisplayWalletUsersDataComponent from '../../components/DisplayWalletUsersDataComponent';
import DisplayWalletExpensesSumComponent from '../../components/DisplayWalletExpensesSumComponent';
import DisplayWalletListsSumComponent from '../../components/DisplayWalletListsSumComponent';
import { useLocation } from 'react-router-dom';
import { BrowserRouter, Route } from "react-router-dom";
import UserService from '../../services/user.service';
import WalletDetailService from '../../services/WalletDetailService';


function WalletPage () {


    let location  = useLocation();
    console.log(location);
    var walletID = location.state.walletID;
    var userToken;
    console.log(walletID);
    
    const user = UserService.getCurrentUser();
        if (user) {
            userToken = user.token
         }
        console.log("userToken komponentu to: " + userToken);
   


    const wallet = WalletDetailService.getWalletDetail(walletID,userToken);
     console.log(wallet);    


   
    
    
        return (
            <div className="container">
                <Header title = "Portfel"/>
                <div className="container box-content">
                        <DisplayWalletDataComponent />
                    <div className="box-subcontent center-content">
                        <a href="/chat" className="card-link  href-text ">Otwórz czat</a>
                    </div>
                    <div className="box-subcontent center-content">
                        <div className="grid-container-3">
                        <div className="left-content" >
                                    <a href="/add-members"><button className="add-icon"></button> </a>
                                </div>
                            <div>
                                <h2 className="text-label ">Członkowie</h2> 
                            </div>
                            <div className="right-content">
                                    <button id="show-hide-button-users" className="dropdown-arrow" onClick={function(e){

                                                    console.log('Klik');
                                                    if ( !document.getElementById("show-hide-button-users").classList.contains('click-to-hide-arrow') ){
                                                        document.getElementById('showing-content-users').style.display = 'block';
                                                        document.getElementById('show-hide-button-users').classList.add('click-to-hide-arrow');
                                                    }
                                                    else {
                                                        document.getElementById('showing-content-users').style.display = 'none';
                                                        document.getElementById('show-hide-button-users').classList.remove('click-to-hide-arrow');
                                                    }
                                        }}>

                                    </button>
                                   
                                
                            </div>
                            
                       </div> 
                    </div>
                       <div id="showing-content-users" className="hide-content box-subcontent-2" style={{display:'none'}}>
                                           <DisplayWalletUsersDataComponent/>
                        </div>
                   


                    <div className="box-subcontent center-content">
                            <div className="grid-container-3">
                                <div className="left-content" >
                                    <a href="/add-expense"><button className="add-icon"></button> </a>
                                </div>
                                <div>
                                    <h2 className="text-label ">Wydatki</h2> 
                                </div>
                                <div className="right-content">
                                        <button id="show-hide-button-expenses" className="dropdown-arrow" onClick={function(e){

                                                        console.log('Klik');
                                                        if ( !document.getElementById("show-hide-button-expenses").classList.contains('click-to-hide-arrow') ){
                                                            document.getElementById('showing-content-expenses').style.display = 'block';
                                                            document.getElementById('show-hide-button-expenses').classList.add('click-to-hide-arrow');
                                                        }
                                                        else {
                                                            document.getElementById('showing-content-expenses').style.display = 'none';
                                                            document.getElementById('show-hide-button-expenses').classList.remove('click-to-hide-arrow');
                                                        }
                                            }}>

                                        </button>
                                    
                                    
                                </div>
                                
                        </div> 
                        </div>
                        <div id="showing-content-expenses" className="hide-content" style={{display:'none'}}>
                                            <DisplayWalletExpensesSumComponent/>
                                            <DisplayWalletExpensesSumComponent/>
                        </div>






                        <div className="box-subcontent center-content">
                            <div className="grid-container-3">
                                <div className="left-content" >
                                    <a href="/create-list"><button className="add-icon"></button> </a>
                                </div>
                                <div>
                                    <h2 className="text-label ">Listy zakupów</h2> 
                                </div>
                                <div className="right-content">
                                        <button id="show-hide-button-lists" className="dropdown-arrow" onClick={function(e){

                                                        console.log('Klik');
                                                        if ( !document.getElementById("show-hide-button-lists").classList.contains('click-to-hide-arrow') ){
                                                            document.getElementById('showing-content-lists').style.display = 'block';
                                                            document.getElementById('show-hide-button-lists').classList.add('click-to-hide-arrow');
                                                        }
                                                        else {
                                                            document.getElementById('showing-content-lists').style.display = 'none';
                                                            document.getElementById('show-hide-button-lists').classList.remove('click-to-hide-arrow');
                                                        }
                                            }}>

                                        </button>
                                    
                                    
                                </div>
                                
                        </div> 
                        </div>
                        <div id="showing-content-lists" className="hide-content" style={{display:'none'}}>
                                            <DisplayWalletListsSumComponent/>
                                            <DisplayWalletListsSumComponent/>
                                            <DisplayWalletListsSumComponent/>
                        </div>







                    <div className="center-content">
                            <a href="/wallet-stats" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Statystyki</a>
                            <br></br>
                            <br></br>
                            <a href="/edit-wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Edytuj portfel</a>
                            <br></br>
                            <br></br>
                            <a href="/delete-wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Usuń portfel</a>
                </div>
                </div>
                
            </div>
        );
    }


export default WalletPage;