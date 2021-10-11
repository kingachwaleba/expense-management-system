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
function WalletPage () {


    let location  = useLocation();
    console.log(location);
    var walletID = location.state.walletID;
    var userToken;
    var userName;
        console.log("Wallet id: " + walletID);
   


    const user = UserService.getCurrentUser();
        if (user) {
           userToken = user.token
           userName = user.name
         }
        console.log("userToken komponentu to: " + userToken);



         
    const [walletData, getWalletData] = useState([]);
    const [walletCategoryData, getWalletCategoryData] = useState([]);
    const [walletUsersData, getWalletUsersData] = useState([]);


    useEffect(()=>{
                WalletDetailService.getWalletDetail(walletID,userToken).then((response)=>{
                    const allData = response.data
                    getWalletData(allData)
                    console.log("Response data: " + response.data)
                    console.log(response.data)


                    const categoryData = response.data.walletCategory
                    getWalletCategoryData(categoryData)

                    const usersData = response.data.userList
                    getWalletUsersData(usersData)
                })
        
    },[])
   
   function checkOwner(userName, ownerName){

        if(userName == ownerName){
            document.getElementById('showing-content-expenses').style.display = 'block';

        }
        else{
            document.getElementById('showing-content-expenses').style.display = 'none';
        }
   }



    //UsersWalletsService.getUserWallets(user.token).then((response)=>{
       // this.setState({wallets: response.data})
      

        

   
    
    
        return (
            <div className="container">
                <Header title = "Portfel"/>

            
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
                                        <h3>Bilans: {walletData.walletExpensesCost} zł</h3>     
                                    </div>
                            </div> 
                    </>


                    <div className="box-subcontent center-content">
                       <h3> <a href="/chat" className="card-link  href-text ">Otwórz czat</a></h3>
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
                                           <DisplayWalletUsersDataComponent key={walletID} usersData= {walletUsersData}/>
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
                            <a href="/wallet-stats" className="card-link center-content btn btn-primary width-100 main-button-style" >Statystyki</a>
                            <br></br>
                            <br></br>
                            
                            <Link className="card-link center-content btn btn-primary width-100 main-button-style"  id="mainbuttonstyle"
                                        to={{
                                        pathname: '/edit-wallet', 
                                        state:{
                                            walletID: walletID
                                        }
                                            

                                        }}>Edytuj portfel
                                       
                                            
                                </Link>
                            <br></br>
                            <br></br>
                            <Button className="card-link main-button-style center-content btn btn-primary width-100 "  
                                onClick={e =>{ 
                                        
                                        DeleteWalletService.deleteWallet(walletID,userToken);
                                        window.location.href='/home'

                                }}>
                          
                                        
                                        Usuń portfel
                                       
                                            
                            </Button>
                </div>
                </div>


           
                
            </div>
        );
    }


export default WalletPage;