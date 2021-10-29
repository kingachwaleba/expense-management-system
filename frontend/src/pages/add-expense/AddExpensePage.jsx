import React, { Component } from 'react';
import Header from '../../components/Header';
import UsersToExpenseComponent from '../../components/UsersToExpenseComponent';
import ExpenseCategoryComponent from '../../components/ExpenseCategoryComponent';
import { Container, Row, Col} from 'reactstrap';
import UserService from '../../services/user.service';
import WalletDetailService from '../../services/WalletDetailService';
import { useState } from 'react';
import { useEffect } from 'react';
import WalletCategoryService from '../../services/WalletCategoryService';
function AddExpensePage () {

    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
    walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    const userToken = UserService.getCurrentUser()
    const [expenseCategories, getExpenseCategories] = useState([])
    const [walletUsers, setWalletUsers] = useState([])
    useEffect(()=>{
        const userData = UserService.getCurrentUser();
        
       
        WalletDetailService.getWalletDetail(walletID,userData.token)
        .then((response)=>{
            console.log(response.data)
            setWalletUsers(response.data.userList)
        })
        .catch(error=>{
            console.error({error})
        });
        
        WalletCategoryService.getCategories(userData.token)
            .then((response)=>{
                const allCategories = response.data
                getExpenseCategories(allCategories)
                console.log(allCategories)
            })
            .catch(error=>{
                console.log({error})
               
            })
          
    },[])
   
        return (
            <Container className="container">
                <Row>
                  <Header title="Dodaj wydatek"/>  
                </Row>
                <Col md="7" className="box-content base-text text-size form-container">
                    <div className = "href-text center-content">Dodaj wydatek</div>
                    <div className="separator-line"/>
                <form name="form"
                        method="post"
                        //onSubmit={}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę..."
                                minLength="1"
                                maxLength="45"
                                //onChange={}
                            />
                            
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Description">Kwota: </label>
                            <input
                                type="number"
                                className="form-control"
                                name="price"
                                placeholder="Wpisz kwotę..."
                                maxLength="1000"
                                //onChange={}
                            />
                            
                        </div>
                        <div className = "box-subcontent">
                        <div className="base-text text-size">
                        Kategoria:
                
                        </div>
                        
                        {
                         
                         expenseCategories.map(
                             category =>
                            
                             <div key = {category.id} className = "center-content custom-radiobuttons">
                           
                             <label className = "form-label text-size" htmlFor={category.id}>
                               <input type="radio" id={category.id} name="category" value={category.name} 
                                    defaultChecked = {category.name === Object.values(expenseCategories)[0].name}
                                   
                                   //onChange={readWalletCategory}
                                   >
                                       
                               </input>
                               
                               <div className="checkmark icons-size-2"></div>
                                {category.name}</label>
                               
                           </div> 
                                
                             
                         )   
             }
                    
                </div>    
                        <button
                            className="btn btn-primary btn-block form-button"
                            id = "mainbuttonstyle"
                            //type = "submit"
                            onClick={e =>  {

                                window.location.href='/wallet'
                            }}
                            >
                            Dodaj
                        </button>
                </form>
                  <button
                            className="btn btn-primary btn-block form-button"
                            id = "mainbuttonstyle"
                            //type = "submit"
                            onClick={e =>  {
                                window.location.href='/wallet'
                            }}
                            >
                            Anuluj
                        </button>
                </Col>
                
                
                                
            </Container>
        );
    
}

export default AddExpensePage;