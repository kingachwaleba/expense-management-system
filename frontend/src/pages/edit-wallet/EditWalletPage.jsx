import React, { Component } from 'react';
import Header from '../../components/Header';
import WalletCategoryComponent from '../../components/WalletCategoryComponent';
import { WalletCategory } from '../../models/walletCategory';
import { editWalletHolder } from '../../models/helpers/editWalletHolder';
import { Wallet } from '../../models/wallet';
import WalletService from '../../services/WalletService';
import { useLocation } from 'react-router';
import UserService from '../../services/user.service';
import { useEffect } from 'react';
import WalletDetailService from '../../services/WalletDetailService';
import { useState } from 'react';

function EditWalletPage () {
    let location  = useLocation();
    var walletID = location.state.walletID;
    var walletCategory;
    var submitted = false;
    var name;
    var description;
    var userToken;
    var userName;
    const [walletData, getWalletData] = useState([]);

    const user = UserService.getCurrentUser();
        if (user) {
           userToken = user.token
           userName = user.name
         }
       
         useEffect(()=>{
            const user = UserService.getCurrentUser();
            WalletDetailService.getWalletDetail(walletID,user.token).then((response)=>{
                const allData = response.data
                getWalletData(allData)
                
            })
            .catch(error=>{
                console.error({error})
              
            
            });

           
},[])

    function readWalletCategory (event){
        var {id, value} = event.target;
        walletCategory = walletCategory;
        walletCategory = new WalletCategory(id,value);  
    }

    function handleChangeName(e) {
        var {value} = e.target;

        name = value;   
    }

    function handleChangeDescription(e) {
        var {name, value} = e.target; 
        description = value; 
    }
    function handleEditWallet(e) {
        e.preventDefault();
        submitted = true;
        const wallet = new Wallet(name,description)
        const editWallet = new editWalletHolder(walletID,name,walletCategory, description)
        if (!name || !description) {
            return;
        }

        WalletService.editWallet(walletID,editWallet,userToken)
        .catch((error)=>{
                   
            console.log(error)
        })
  
   
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
                                required
                                minLength="1"
                                maxLength="45"
                                onChange={(e) => handleChangeName(e)}/>
                            <div className="invalid-feedback">
                                A valid login is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Description">Opis: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="description"
                                placeholder={walletData.description}
                                required
                                maxLength="1000"
                                onChange={(e) => handleChangeDescription(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>
                        
                        <div>
                        <WalletCategoryComponent readWalletCategory={readWalletCategory} />
                    
                        
                        
                        </div>
                    
                        <br></br>
                        <br></br>
                        <button
                            className="btn btn-primary btn-block form-button"
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