import React, { useEffect } from 'react';
import { Container, Row, Col } from 'reactstrap';
import UserService from '../services/user.service';
import { useState } from 'react';
import WalletService from '../services/WalletService';
import NotificationService from '../services/NotificationService';
const DisplayWalletUsersDataComponent = (props) =>{
var clickCounter = 0;  
let walletID = '';

const [debtorData, setDebtorData]=useState([])
if (sessionStorage && sessionStorage.getItem('walletID')) {
walletID = JSON.parse(sessionStorage.getItem('walletID'));
}
    const user = UserService.getCurrentUser()
    function setHelperClassname(user){
        var classNameHelper = this.state.classNameHelper;
        if(this.props.currentList.includes(user)){
            this.state.classNameHelper = "delete-user-from-list"
        }
        else{
            this.state.classNameHelper = "add-user-to-list"
          
        }
    }

    useEffect(()=>{
        
        console.log(props.usersData)
       
    },[])
    function manageDebts(creditor, debtor, howMuch,debtorId,debtorImg,debtorDeleted,debtorRoles, creditorId, creditorImg,creditorDeleted,creditorRoles){
        if(creditor == user.login ){
            console.log("NADWYŻKA")
            return(
                <div className="grid-container-3 box-subcontent">
                <div className="center-content">
                    <div className="green-text center-content text-size">Należność: {howMuch}zł</div>
                </div>
                <div className="center-content">
                    <button className="reminder-icon icons-size"
                    title="Wyślij przypomnienie"
                      onClick={(e)=>{
                        var debtsHolder = {
                            debtor:{
                                id: debtorId,
                                login: debtor,
                                image: debtorImg,
                                deleted: debtorDeleted,
                                roles: debtorRoles
                            },
                            creditor:{
                                id:creditorId,
                                login:creditor,
                                image:creditorImg,
                                deleted: creditorDeleted,
                                roles: creditorRoles
                            },
                            howMuch: howMuch
                        }
                        NotificationService.sendReminder(walletID,debtsHolder,user.token)
                        .then((response)=>{
                         console.log(response)
                         if(response.status == 200)
                         alert("Wysłano przypomienie o długu");
                            
                        })
                        .catch(error=>{
                            console.error({error})
                            alert(error.response);
                        });
                    }}/>

                </div>
                <div className="center-content">
                    <button className="handshake-icon icons-size"
                    title="Potwierdz uregulowanie rachunku"
                
                    onClick={(e)=>{
                        var debtsHolder = {
                            debtor:{
                                id: debtorId,
                                login: debtor,
                                image: debtorImg,
                                deleted: debtorDeleted,
                                roles: debtorRoles
                            },
                            creditor:{
                                id:creditorId,
                                login:creditor,
                                image:creditorImg,
                                deleted: creditorDeleted,
                                roles: creditorRoles
                            },
                            howMuch: howMuch

                        }
                        WalletService.payDebt(walletID,debtsHolder,user.token)
                        .then((response)=>{
                         console.log(response)
                         window.location.href='/wallet'
                            
                        })
                        .catch(error=>{
                            console.error({error})
                            alert(error.response);
                        });
                    }}/>
                </div>
            </div>
            )
        }
        else if(debtor==user.login){
            console.log("DŁUŻNIK")
            return(
            <div className="box-subcontent text-size error-text">
                Oddaj {creditor} należność: {howMuch} zł
            </div>
            )
        }
    }

        return (
            <Container>
                <div className="grid-container-3 text-size">
                    <div className="left-content text-label text-size">Nazwa:</div>
                    <div className="right-content text-label">Bilans:</div>
                    <div className="left-content text-label"></div>
                </div>
                <div className="separator-line"></div>
                
               
                {
                         
                        props.usersData.map(
                             userData =>
                             
                             <div key = {userData.login} className = "center-content">
                                
                                 <div className="text-size">

                                      
                                     
                                    <div className="right-content">
                                
                                    {
                                        userData.debt != null ? (
                                        <div>
                                            <div  className="grid-container-3"  >
                                                <div className="left-content">
                                                    { userData.login }
                                                </div>
                                                <div className="right-content">
                                                { userData.balance } zł
                                                </div>
                                                <div className="right=content">
                                                <button 
                                                    className={`cash-icon icons-size
                                                    ${userData.debt.creditor.login == user.login && userData.login != user.login ? "invert-colors" : ""} 
                                                    ${userData.debt.howMuch == 0 || userData.login == user.login || userData.debt.creditor === null ? "grey-scale":""}` }
                                                    disabled={ userData.login == user.login}
                                                   
                                                    onClick={(e)=>{
                                                        clickCounter++
                                                        document.getElementById("expense-management-"+userData.login).style.display = 'block'; 
                                                        if(clickCounter==2){
                                                            document.getElementById("expense-management-"+userData.login).style.display = 'none';
                                                            clickCounter = 0
                                                        } 

                                                    }}
                                                /></div>  
                                            </div>   
                                           <div className="center-content" id={"expense-management-"+ userData.login} 
                                                        style={{display:'none'}}
                                                        >
                                                        {
     
                                                        manageDebts(userData.debt.creditor.login, userData.debt.debtor.login, userData.debt.howMuch,
                                                            userData.debt.debtor.id,userData.debt.debtor.image,userData.debt.debtor.deleted,userData.debt.debtor.roles,
                                                            userData.debt.creditor.id,userData.debt.creditor.image,userData.debt.creditor.deleted,userData.debt.creditor.roles)
                                                        } 
                                                
                                   
                                    

                                            </div>
                                        </div>
                                        ):(
                                            <div className="grid-container-3">
                                                 <div className="left-content">
                                                    { userData.login }
                                                </div>
                                                <div className="right-content">
                                                { userData.balance } zł
                                                </div>
                                                <div className="right-content">
                                                  <button 
                                            className={`cash-icon icons-size left-content grey-scale`}
                                           
                                        
                                            disabled={ userData.login == user.login}
                                            onClick={(e)=>{
                                                clickCounter++
                                                document.getElementById("expense-management-"+userData.login).style.display = 'block'; 
                                                if(clickCounter==2){
                                                    document.getElementById("expense-management-"+userData.login).style.display = 'none';
                                                    clickCounter = 0
                                                } 

                                            }}
                                        /> </div>
                                            </div>
                                        )
                                    }

                                    
                                        </div>

                                          </div>
        
                             </div>
                             
                         )   
             }
            
          
                
            </Container>
        );
    
}

export default DisplayWalletUsersDataComponent;