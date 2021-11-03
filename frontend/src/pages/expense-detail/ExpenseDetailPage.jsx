import React, { Component } from 'react';
import Header from '../../components/Header';
import { useEffect } from 'react';
import { useState } from 'react';
import UserService from '../../services/user.service';
import ExpenseService from '../../services/ExpenseService';
import { Container, Col, Row } from 'react-bootstrap';
import { Button } from 'react-bootstrap';
import ImageService from '../../services/ImageService';
function ExpenseDetailPage(){
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
    walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    let expenseID = '';
    if (sessionStorage && sessionStorage.getItem('expenseID')) {
    expenseID = JSON.parse(sessionStorage.getItem('expenseID'));
    }
    const userData = UserService.getCurrentUser()
    const [expenseCategory, setExpenseCategory] = useState([])
    const [expenseDetailSet, setExpenseDetailSet] = useState([])
    const [expenseName, setExpenseName]= useState("")
    const [expenseWallet, setExpenseWallet]= useState([])
    const [ownerData, setOwnerData] = useState([])
    const [receiptImg, setReceiptImg] = useState([])
    const [expenseCost, setExpenseCost] = useState("")
    const [message, setMessage] = useState("")
    const [displayImg, setDisplayImg] = useState(null)
    useEffect(()=>{
        console.log("WalletId:")
        console.log(walletID)
        console.log("ExpenseId:")
        console.log(expenseID)
        console.log("userToken:")
        console.log(userData.token)
        ExpenseService.getExpenseDetail(expenseID,userData.token)
        .then((response)=>{
            console.log("Get expenseDetail data (responseData)")
            console.log(response.data)

            console.log("Expense category (responseData)")
            console.log(response.data.expense.category)
            setExpenseCategory(response.data.expense.category)

            console.log("ExpenseDetailSet (responseData)")
            console.log(response.data.expense.expenseDetailSet)
            setExpenseDetailSet(response.data.expense.expenseDetailSet)

            console.log("ExpenseName (responseData)")
            console.log(response.data.expense.name)
            setExpenseName(response.data.expense.name)

            console.log("ExpenseWallet (responseData)")
            console.log(response.data.expense.wallet)
            setExpenseWallet(response.data.expense.wallet)

            console.log("OwnerData (responseData)")
            console.log(response.data.expense.user)
            setOwnerData(response.data.expense.user)

            console.log("ReceiptImg (responseData)")
            console.log(response.data.expense.receipt_image)
            setReceiptImg(response.data.expense.receipt_image)

            console.log("ExpenseCost (responseData)")
            console.log(response.data.expense.total_cost)
            setExpenseCost(response.data.expense.total_cost)
        })
       
        .catch(error=>{
            console.error({error})
        });
               
          
    },[])
    useEffect(()=>{
      
    },[expenseCategory])

    useEffect(()=>{
        var usersList = []
        expenseDetailSet.forEach(element => {
            if(usersList.includes(element.user.login)){}
            else{
                usersList.push(element.user.login) 
            }
           
        console.log("Lista użytkowników")
        console.log(usersList)
        sessionStorage.setItem('currentExpenseUsersList',JSON.stringify(usersList))
       
        });
          
    },[expenseDetailSet])
    useEffect(()=>{
      if(receiptImg != null){
        setMessage("")
        document.getElementById('img-preview-div').style.display = 'block';
        var formData = new FormData();
            formData.append('imageName',receiptImg)
        console.log(receiptImg)
        console.log(formData.get('imageName')) 

        /*
        ImageService.getImg(formData, userData.token)
        .then(response=>{
            console.log(response)
            //setDisplayImg(response.data)
        })
        .catch(error=>{
            console.error(error)
        });
        
       //-------------------------------------------------------------
       /*
                fetch('http://localhost:8080/files', {
                        method: 'GET',
                        responseType: 'blob',
                        headers: {
                        "Authorization" : `Bearer ${userData.token}`,
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                },
                })
                .then((response) => {
                console.log(response.text());
                })
                .then((data) => {
                    console.log("Fetched IMG:")
                    console.log( JSON.parse(data) )
                    setDisplayImg(JSON.parse(data))
                })
                .catch(error=>{
                    console.error({error})
                });
                */
               //--------------------------------------------------------

      }
      else{
          setMessage("Brak zdjęcia paragonu")
          document.getElementById('img-preview-div').style.display = 'none';
      }
    },[receiptImg])
        return (
            <Container>
                <Header title="Wydatek"/>
                <div className="box-content text-size">
                        <div className="center-content text-label"><b>Wydatek: {expenseName}</b></div>       
                        <div className='separator-line'></div>
                        <div  className="grid-container">
                                <div  className="text-label right-content">Wykonany przez:</div >
                                <div className="left-content">{ownerData.login}</div >
                        </div >
                        <div  className="grid-container">
                                <div  className="text-label right-content">Kategoria:</div >
                                <div className="left-content">{expenseCategory.name}</div >
                        </div >
                        <div  className="grid-container">
                                <div  className="text-label right-content">Koszt:</div >
                                <div className="left-content">{expenseCost} zł</div >
                        </div >
                        <div className='separator-line'></div>
                        <div className="center-content">
                                    {message}
                        <div id="img-preview-div">
                            <img src={displayImg} alt="img" />
                        </div>         
                        

                        </div>
                        <div className='separator-line'></div>
                        <div className="box-subcontent">
                            <div  className="grid-container">
                                    <div  className="text-label left-content">Nazwa:</div >
                                    <div className="text-label right-content"> Należność:</div >
                            </div >
                            <div className='separator-line'></div>
                           
                            { 
                                expenseDetailSet.map(
                                    expenseUser =>
                                    <div key = {expenseUser.id} className = "text-size">
                                        <div  className="grid-container">
                                            <div  className="text-label left-content">{expenseUser.user.login}</div >
                                            <div className="right-content error-text">{expenseUser.cost} zł</div >
                                        </div>  
                                </div> 
                            )}
                            </div>
                            {(ownerData.login === userData.login) ?(
                                 <Container>
                                <br />
                                <Row>
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle deleteWalletButton"  
                                        onClick={e =>{
                                           
                                                window.location.href='/edit-expense'
                                           
                                        }}> 
                                        Edytuj wydatek              
                                    </Button>
                                
                               </Row>
                               <br />         
                               <Row>
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle deleteWalletButton"  
                                        onClick={e =>{
                                            if(window.confirm('Czy na pewno chcesz usunąć wydatek?')){  
                                                ExpenseService.deleteExpense(expenseID, userData.token)
                                                .catch(error=>{
                                                    console.error({error})
                                                });
                                               window.location.href='/wallet'
                                            }
                                        }}> 
                                        Usuń wydatek            
                                    </Button>
                                   </Row> 
                                   <br />
                                   <Row >
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle"  
                                        onClick={e =>{        
                                                window.location.href='/wallet'
                                        }}> 
                                        Wróć do portfela         
                                    </Button>
                            
                                </Row>
                                </Container>
                            ):(
                                <div>
                                    <br />
                                <Row >
                                   
                                    <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle"  
                                        onClick={e =>{ 
                                          
                                                
                                                window.location.href='/wallet'
                                            

                                        }}> 
                                        Wróć do portfela         
                                    </Button>
                            
                                </Row>
                                </div>
                            )}

                    
                </div>
            </Container>
        );
    
}

export default ExpenseDetailPage;