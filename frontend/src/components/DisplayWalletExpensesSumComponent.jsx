import React, { Component } from 'react';
import UserService from '../services/user.service';
import { Container, Col, Row } from 'react-bootstrap';
import ExpenseService from '../services/ExpenseService';
import { useState } from 'react';
import { useEffect } from 'react';
import { Button } from 'react-bootstrap';

function DisplayWalletExpensesSumComponent () {
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
    walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userData = UserService.getCurrentUser()
    const [expenseSumData, setExpenseSumData] = useState([])
    const [isExpenseOwner, setIsExpenseOwner] = useState(false)
    const [message, setMessage] = useState("")

    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("userToken:")
        console.log(userData.token)
        ExpenseService.getWalletExpenses(walletID,userData.token)
        .then((response)=>{
            console.log("Get expenseSum data (responseData)")
            console.log(response.data)
            console.log(response.data)
            setExpenseSumData(response.data)
            
        })
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])
    useEffect(()=>{
        console.log("ExpenseSumData:")
        console.log(expenseSumData)
        if(Object.keys(expenseSumData).length === 0){
            setMessage("Brak wydatków")
        }
        else setMessage("")
    },[expenseSumData])
   
        return (
            <Container>
             
                <div className=" text-size base-text center-content">{message}</div>
                 {                
                         expenseSumData.map(
                             expense =>
                            
                             <div key = {expense.id} className = "box-subcontent-2 text-size">
                            <div className="center-content text-label">  <b>  {expense.name}  </b></div >
                            <div className='separator-line'></div>
                            <div className="grid-container" >
                                <div className="text-label right-content">Kategoria:</div >
                                <div className="left-content" >{expense.category.name}</div >
                            </div >
                            <div  className="grid-container">
                                <div  className="text-label right-content">Koszt:</div >
                                <div className="left-content">{expense.total_cost}</div >
                            </div >
                           
                            <div className='separator-line'></div>
                           
                            <div className="center-content">
                                <a className="center-content href-text text-size"   
                                    onClick={(e)=>{
                                        sessionStorage.setItem('expenseID',JSON.stringify(expense.id))
                                        window.location.href='/expense-detail'
                                    }}>
                                     Zobacz szczegóły
                                    </a> 
                            </div>
                             
                            
                           </div> 
                                
                             
                         )   
             }

 


            </Container>
        );
    
}

export default DisplayWalletExpensesSumComponent;