import React, { Component } from 'react';
import Header from '../../components/Header';
import WalletCategoryComponent from '../../components/WalletCategoryComponent';
import { WalletCategory } from '../../models/walletCategory';
import { editWalletHolder } from '../../models/helpers/editWalletHolder';
import { Wallet } from '../../models/wallet';
import WalletService from '../../services/WalletService';
import { useLocation } from 'react-router';
import UserService from '../../services/user.service';

function EditWalletPage () {
    let location  = useLocation();
    var walletID = location.state.walletID;
    var walletCategory;
    var submitted = false;
    var name;
    var description;
    var userToken;
    var userName;


    const user = UserService.getCurrentUser();
        if (user) {
           userToken = user.token
           userName = user.name
         }
        console.log("userToken komponentu to: " + userToken);


    function readWalletCategory (event){
        var {id, value} = event.target;
       
        walletCategory = walletCategory;
        //walletCategory.id = id;
        //walletCategory.name = value;
        console.log({value})
        walletCategory = new WalletCategory(id,value);
        console.log({walletCategory})
        
        
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
        const editWallet = new editWalletHolder(name,walletCategory.name, description)
        console.log("Edit wallet data: ")
        console.log({editWallet})
        console.log({walletID})
        console.log({userToken})
        
       
      
    
      
        if (!name || !description) {
            return;
        }

        
        WalletService.editWallet(walletID,editWallet,userToken)
       
   
    }
    function walletHolderHelper(e){
       //const wallet_holder = new WalletHolder()

    
       
       //console.log(wallet_holder);
    }
        return (
            <div>
                <div className="container">
                 <Header title='Edytuj portfel:' />
                <div className="form-container">

                <div className="box-content">
                    <h4 className="text-label center-content" >Edytuj profil</h4>
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
                                placeholder="Wpisz nazwę..."
                                required
                                
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
                                placeholder="Wpisz opis..."
                                required
                               
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
                            onClick={e =>  {submitted = true
                                handleEditWallet(e);
                                window.location.href='/home'
                            
                            }
                                            

                                    }
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