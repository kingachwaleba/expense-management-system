import React, { Component, useEffect } from 'react';
import Header from '../../components/Header';
import DisplayWalletUsersDataComponent from '../../components/DisplayWalletUsersDataComponent';
import DisplayWalletExpensesSumComponent from '../../components/DisplayWalletExpensesSumComponent';
import DisplayWalletListsSumComponent from '../../components/DisplayWalletListsSumComponent';
import { useLocation } from 'react-router-dom';
import UserService from '../../services/user.service';
import WalletDetailService from '../../services/WalletDetailService';
import { useState } from 'react';
import { Button } from 'reactstrap';
import DeleteWalletService from '../../services/DeleteWalletService';
import { Link } from 'react-router-dom';
import { Container, Col, Row } from 'react-bootstrap';
import ManageWalletUsersService from '../../services/ManageWalletUsersService';

function WalletPage () {


    let location  = useLocation();
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
    walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    var userToken;
    var userName;     
    const [walletData, getWalletData] = useState([]);
    const [walletCategoryData, getWalletCategoryData] = useState([]);
    const [walletUsersData, getWalletUsersData] = useState([]);
    const [walletOwner, getWalletOwner] = useState("");
    const [isOwner, checkIsOwner] = useState(false);
    useEffect(()=>{
                const user = UserService.getCurrentUser();
               
                
                    WalletDetailService.getWalletDetail(walletID,user.token).then((response)=>{
                    const allData = response.data
                    getWalletData(allData)
                    const categoryData = response.data.walletCategory
                    getWalletCategoryData(categoryData)
                    getWalletOwner(response.data.owner)
                    const usersData = response.data.userList
                    console.log("USERS DATA")
                    console.log([usersData])
                    getWalletUsersData(usersData)
                    if(user.login == response.data.owner) checkIsOwner(true)
                    else checkIsOwner(false)
                    console.log(walletData)
                    
                })
                .catch(error=>{
                    console.error({error})
                });

               
    },[])
   
   




      

        

   
    
    
        return (
            <Container>
                <Header title = "Portfel"/>

            <Col>
                <div className="container box-content">
                    <>
                                <h2 className="text-label center-content">  {walletData.name}</h2> 

                                <div className="separator-line" ></div>

                                <h3>Opis: {walletData.description}</h3>  

                                <div className="separator-line" ></div>   

                                <h3>Kategoria: {walletCategoryData.name}</h3>  
                                <h3>Właściciel: {walletData.owner}</h3>  
                                <h3>Liczba członków: {walletData.userListCounter}</h3>       
                                <h3>Wydatki: {walletData.walletExpensesCost} zł</h3>     
                                 <div className = "grid-container">
                                    <div className = "center-content">
                                        <h3>Twoje wydatki: {walletData.walletExpensesCost} zł</h3>     
                                    </div>
                                    <div className = "center-content">
                                        <h3>Twój bilans: {walletData.loggedInUserBalance} zł</h3>     
                                    </div>
                                 </div> 
                    </>


                    <div className="box-subcontent center-content">
                       <h3> <a href="/chat" className="card-link  href-text text-size">Otwórz czat</a></h3>
                    </div>
                    <div className="box-subcontent center-content">
                        <div className="grid-container-3">
                            <div className="left-content" >
                                
                                <button className="add-icon text-size icons-size"
                                onClick={(e)=>{
                                    sessionStorage.setItem('walletID',JSON.stringify(walletID))
                                    window.location.href='/add-members'
                                }}>
                                </button> 

                            </div>

                            <div className="center-content">
                                <h2 className="text-label text-size">Członkowie</h2> 
                            </div>
                            
                            <div className="right-content">
                                    <button id="show-hide-button-users" className="dropdown-arrow icons-size" onClick={function(e){

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
                            <DisplayWalletUsersDataComponent walletId={walletID} usersData= {walletUsersData} walletOwner = {walletData.owner}/>
                            <div className="separator-line"></div>
                            { 
                            isOwner ? (
                                    <div className="center-content">
                                        <a className="center-content href-text text-size"   
                                            onClick={(e)=>{
                                                sessionStorage.setItem('walletID',JSON.stringify(walletID))
                                                window.location.href='/edit-users-list'
                                            }}>
                                            Edytuj listę członków
                                        </a> 
                                    </div>
                                ):(<div/>)
                            }      
                
                        </div>
                   


                    <div className="box-subcontent center-content">
                            <div className="grid-container-3">
                                <div className="left-content" >
                                    <a href="/add-expense"><button className="add-icon icons-size"></button> </a>
                                </div>
                                <div className="center-content">
                                    <h2 className="text-label text-size">Wydatki</h2> 
                                </div>
                                <div className="right-content">
                                        <button id="show-hide-button-expenses" className="dropdown-arrow icons-size" onClick={function(e){

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
                                            
                        </div>





                                                    
                        <div className="box-subcontent center-content">
                            <div className="grid-container-3">
                                <div className="left-content" >
                                    <a href="/create-list"><button className="add-icon icons-size"></button> </a>
                                </div>
                                <div className="center-content">
                                    <h2 className="text-label text-size">Listy zakupów</h2> 
                                </div>
                                <div className="right-content">
                                        <button id="show-hide-button-lists" className="dropdown-arrow icons-size" onClick={function(e){

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
                                          
                        </div>







                    <div className="center-content">
                        <Container>
                            <Col>
                            <br />
                                <Row>
                                    <br />
                                    <Link to="/wallet-stats" className="card-link center-content btn btn-primary width-100 main-button-style text-size" >Statystyki</Link>
                                </Row>
                           
                            
                            {isOwner ?(
                                 <div>
                                 <br />
                                
                                <Row>
                                    <Link className="card-link center-content btn btn-primary width-100 main-button-style text-size"  id="mainbuttonstyle editWalletButton"
                                                to={{
                                                pathname: '/edit-wallet', 
                                                state:{
                                                    walletID: walletID
                                                }}}>
                                                Edytuj portfel                   
                                    </Link>
                                <br></br>
                               </Row>
                               <br />
                               <Row>
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle deleteWalletButton"  
                                        onClick={e =>{
                                            if(window.confirm('Czy na pewno chcesz usunąć portfel?')){  
                                                DeleteWalletService
                                                    .deleteWallet(walletID,UserService.getCurrentUser().token)
                                                    .catch((error)=>{
                                                        console.log(error)
                                                    });
                                                window.location.href='/home'
                                            }
                                        }}> 
                                        Usuń portfel              
                                    </Button>
                                   </Row> 
                                </div>
                            ):(
                                <div>
                                    <br />
                                <Row >
                                    
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle"  
                                        onClick={e =>{ 
                                            if(window.confirm('Czy na pewno chcesz opuścić portfel?')){
                                                ManageWalletUsersService
                                                    .leaveWallet(walletID,UserService.getCurrentUser().token)
                                                    .catch((error)=>{
                                                        console.log(error)
                                                    });
                                                window.location.href='/home'
                                            }

                                        }}> 
                                        Opuść portfel          
                                    </Button>
                            
                                </Row>
                                </div>
                            )}
                            </Col>
                            </Container>


                </div>
                </div>


           
                </Col>
            </Container>
        );
    }


export default WalletPage;