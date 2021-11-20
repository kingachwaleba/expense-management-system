import React, { Component } from 'react';
import Header from '../../components/Header';
import { WalletCategory } from '../../models/walletCategory';
import { editWalletHolder } from '../../models/helpers/editWalletHolder';
import WalletService from '../../services/WalletService';
import { useLocation } from 'react-router';
import UserService from '../../services/user.service';
import { useEffect } from 'react';
import WalletDetailService from '../../services/WalletDetailService';
import { useState } from 'react';
import WalletCategoryService from '../../services/WalletCategoryService';

function EditWalletPage () {
    let location  = useLocation();
    var walletID = location.state.walletID;
    var submitted = false;
    var name;
    var userToken;
    var userName;
    const [,setState] = useState();
    const [walletData, getWalletData] = useState([]);
    const [errorMessage, setErrorMessage] = useState("");
    const [walletCategories, getWalletCategories] = useState([])
    const [currentCategory,setCurrentCategory] = useState([])
    
    //------------
    const [walletName, setWalletName] = useState("")
    const [walletDescription, setWalletDescription] = useState("")
    const [walletCategory2, setwalletCategory2] = useState([])
    const user = UserService.getCurrentUser();
            if (user) {
            userToken = user.token
            userName = user.name
            }
       
    useEffect(()=>{
        const user = UserService.getCurrentUser();
        WalletDetailService.getWalletDetail(walletID,user.token)
            .then((response)=>{
            const allData = response.data
            getWalletData(allData)
            console.log(response.data)
            setCurrentCategory(response.data.walletCategory)
            setWalletName(response.data.name) 
            setWalletDescription(response.data.description)
            setwalletCategory2(new WalletCategory(response.data.walletCategory.id, response.data.walletCategory.name))
            setCurrentCategory((state)=>{return state})
        })
        .catch(error=>{
            console.error({error})
            });

          
    },[])

    useEffect(()=>{
        WalletCategoryService.getCategories(user.token)
        .then((response)=>{
            const allCategories = response.data
            getWalletCategories(allCategories)
            console.log(allCategories)
        })
        .catch(error=>{
            console.log({error})
            setErrorMessage(error.response.data)
        })

           
    },[])
   
    function currentCategoryName(name){
        console.log(currentCategory.name)
        if (currentCategory.name === name)
        return true
    }
    function readWalletCategory (event){
        var {id, value} = event.target;
        
        setwalletCategory2(new WalletCategory(id,value)) 
    }

    function handleChangeName(e) {
        var {value} = e.target;
        setWalletName(value)   
    }

    function handleChangeDescription(e) {
        var {name, value} = e.target; 
        setWalletDescription(value) 
    }
    function handleEditWallet(e) {
        e.preventDefault();
        submitted = true;
        const editWallet = new editWalletHolder(walletID,walletName,walletCategory2, walletDescription)
        console.log(editWallet)
     
        WalletService.editWallet(walletID,editWallet,userToken)
        .catch((error)=>{
                   
            console.log(error)
        })
        window.location.href='/home'
   
    }
   
        return (
            <div>
                <div className="container">
                 <Header title='Edytuj portfel:' />
                <div className="form-container">
               
                <div className="box-content text-size">
                    <div className="text-label center-content text-size" >{walletData.name}</div>
                    <div className="separator-line"></div>
                    
                    <form
                        name="form"
                        method="post"
                        onSubmit={(e) => handleEditWallet(e)}>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder={walletData.name}
                                minLength="1"
                                maxLength="45"
                                onChange={(e) => handleChangeName(e)}/>
                            
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Description">Opis: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="description"
                                placeholder={walletData.description}
                                maxLength="1000"
                                onChange={(e) => handleChangeDescription(e)}/>
                            
                        </div>
                        
                        <div>
                            <div className = "box-subcontent">
                        <div className="base-text text-size">
                        Kategoria:
                
                        </div>
                        {console.log(currentCategory)}
                        {
                         
                         walletCategories.map(
                             category =>
                            
                             <div key = {category.id} className = "left-content custom-radiobuttons margin-left-text">
                           
                             <label className = "form-label text-size" htmlFor={category.id}>
                               <input type="radio" id={category.id} name="category" value={category.name} 
                                    defaultChecked = {currentCategoryName(category.name)}
                                   
                                   onChange={readWalletCategory}
                                   >
                                       
                               </input>
                               
                               <div className="checkmark icons-size-2"></div>
                                {category.name}</label>
                               
                           </div> 
                                
                             
                         )   
             }
                    
                </div>        
                        
                        </div>
                    
                        <br></br>
                        <br></br>
                        <div className="text-size error-text">
                            {errorMessage}
                        </div>
                        
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>  {submitted = true
                                //handleEditWallet(e);
                                //window.location.href='/home'
                               
                            
                            }}
                            >
                            Zapisz zmiany
                        </button>
                    
                    </form>
                  </div>
                </div>
            </div>
        </div>
        );
    
}

export default EditWalletPage;