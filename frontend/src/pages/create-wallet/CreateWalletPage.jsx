
import React, {Component} from 'react';
import WalletService from '../../services/WalletService';
import {User} from '../../models/user';
import {Wallet} from '../../models/wallet';
import {WalletCategory} from '../../models/walletCategory';
import {WalletHolder} from '../../models/helpers/wallet-holder';
import Header from '../../components/Header';
import WalletCategoryComponent from '../../components/WalletCategoryComponent';
import AddUsersToNewWalletComponent from '../../components/AddUsersToNewWalletComponent';
import UserService from '../../services/user.service';
import { useHistory } from 'react-router';
import { withRouter } from 'react-router';
class CreateWalletPage extends React.Component {

    constructor(props, context) {
        super(props, context);
        this.readWalletCategory = this.readWalletCategory.bind(this);
        this.createUsersList = this.createUsersList.bind(this);
       

     };

   
        state = {
            wallet_holder: new WalletHolder('', ''),
            wallet: new Wallet('','',''), 
            walletCategory: new WalletCategory('',''),
            userList: new Array,
            tmp: "",
            submitted: false,
            loading: false,
            errorMessage: '',
            usertoken: undefined,
        };

      

     componentDidMount() {
        const currentUser = UserService.getCurrentUser();
        if (currentUser) {

            
            this.setState({
                
               
                usertoken: currentUser.token,
                
            });

           
        }
        
       
    }

    
    


    handleChangeName(e) {
        var {value} = e.target;
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
     
        wallet.name = value;
        wallet.walletCategory = this.state.walletCategory;

        
        this.setState({wallet_holder: wallet_holder});
        
    }

    handleChangeDescription(e) {
        var {name, value} = e.target;
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        
        
        wallet.description = value;
     
    }
    
    walletHolderHelper(e){
        var wallet_holder = this.state.wallet_holder;
        wallet_holder.wallet = this.state.wallet;
        wallet_holder.userList = this.state.userList;
      
    }
    
    handleCreateWallet(e) {
        e.preventDefault();
        this.setState({submitted: true});
       var wallet_holder = this.state.wallet_holder;  
        if (!wallet_holder.wallet.name || !wallet_holder.wallet.description)return;
        this.setState(({loading: true}));
        WalletService
            .create_wallet(wallet_holder,this.state.usertoken)
            .catch((error)=>{
                console.log(error.response.data)
            });
    }
    readWalletCategory = (event) => {
        var {id, value} = event.target;
        var wallet = this.state.wallet;
        var walletCategory = this.state.walletCategory
        walletCategory.id = id;
        walletCategory.name = value;
        this.setState({
          [event.target.id]: event.target.id,
       
        });
        
    }
    
    createUsersList = (event) =>{
        var{value} = event.target;
        var wallet = this.state.wallet;
        var userList = this.state.userList;
     
         
            if (!this.state.userList.includes(event.target.id)) {
              this.setState(prevState => ({ userList: [...prevState.userList, event.target.id]}))
              
            }else {
            this.setState(prevState => ({ userList: prevState.userList.filter(user => user !== event.target.id) }));
          
          } 

    }

  
    render() {
        const {wallet_holder, submitted, loading, errorMessage} = this.state;
        return (
            <div className="container">
                 <Header title='Utwórz portfel:' />
                <div className="form-container">
                 

                    {errorMessage &&
                    <div className="alert alert-danger" role="alert">
                        {errorMessage}
                    </div>
                    }
                <div className="box-content">
                    <form
                        name="form"
                        method="post"
                        onSubmit={(e) =>{
                            this.setState({submitted: true});
                            this.walletHolderHelper(e);   
                            this.handleCreateWallet(e)
                            //window.location.href='/home'


                        }}>
                        <div className={'form-group'}>
                            <label className="form-label text-size"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę..."
                                required
                                minLength="1"
                                maxLength="45"
                                onChange={(e) => this.handleChangeName(e)}/>
                            <div className="invalid-feedback">
                                Nazwa jest wymagana.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label text-size" htmlFor="Description">Opis: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="description"
                                placeholder="Wpisz opis..."
                                required 
                                maxLength="1000"
                                onChange={(e) => this.handleChangeDescription(e)}/>
                            <div className="invalid-feedback">
                                Opis jest wymagany.
                            </div>
                        </div>
                        <div className={'form-group'}>
                            <AddUsersToNewWalletComponent createUsersList={this.createUsersList} currentList = {this.state.userList}/>
                        </div>
                        <div>
                            <WalletCategoryComponent  readWalletCategory={this.readWalletCategory} />
                        </div>
                    
                        <br></br>
                        <br></br>
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type="submit"
                        >
                            Utwórz
                        </button>
                    
                    </form>
                  </div>
                </div>
            </div>
        );
    }
}

export {CreateWalletPage};