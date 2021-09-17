
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

           // console.log(currentUser.login);
        }
        console.log(currentUser);
       // console.log("Token to: " + this.state.username);
        //console.log("Type to: " + user.type);
    }

    
    


    handleChangeName(e) {
        var {value} = e.target;
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        //var userList = this.state.userList;
       
         /*
         wallet_holder[wallet.name] = value;
        wallet_holder[wallet.description] = value;
         
        wallet_holder[wallet.walletCategory] = value;
       
        wallet_holder[] = value;
        */
        wallet.name = value;
        wallet.walletCategory = this.state.walletCategory;

        
        this.setState({wallet_holder: wallet_holder});
        
    }

    handleChangeDescription(e) {
        var {name, value} = e.target;
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        
        
        wallet.description = value;
        //wallet.walletCategory = this.state.walletCategory; 
        //wallet_holder.wallet = this.state.wallet;
       
        //this.setState({wallet_holder: wallet_holder});
       // console.log(wallet_holder)
        
    }
    //---------------------------------------------------
    testFunction(e){
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        var userList = this.state.userList;

    
       wallet_holder.wallet = this.state.wallet;
       wallet_holder.userList = this.state.userList;
       console.log(wallet_holder);
    }
    //----------------------------------------------------
    handleCreateWallet(e) {

        e.preventDefault();
        this.setState({
            submitted: true
        //---
        
        //---
        
        });
       // const {wallet_holder} = this.state;
       var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        var userList = this.state.userList;
        var userToken = this.state.usertoken;
    
       wallet_holder.wallet = this.state.wallet;
       wallet_holder.userList = this.state.userList;
       console.log("Wallet_holder: " + wallet_holder)
       //console.log(userList)
        //validate form
        if (!wallet_holder.wallet.name || !wallet_holder.wallet.description) {
            return;
        }

        this.setState(({loading: true}));
        WalletService.create_wallet(wallet_holder,this.state.usertoken)
        console.log(this.state.usertoken)
        /*
            .then(data => {
                    this.props.history.push('/');
                },
                error => {
                    if (error && error.response && error.response.status === 409) {
                        this.setState({
                            errorMessage: 'This login is not available.',
                            loading: false,
                        });
                    } else {
                        this.setState({
                            errorMessage: 'Unexpected error occurred.',
                            loading: false,
                        });
                    }
                });
                */
    }
    readWalletCategory = (event) => {
        var {id, value} = event.target;
        var wallet = this.state.wallet;
        var walletCategory = this.state.walletCategory
        walletCategory.id = id;
        walletCategory.name = value;
        this.setState({
          
          [event.target.id]: event.target.id,
        
            //Dane między komponentami można pozyskiwać jako props gdy mają np wspólnego parenta, pozystakć dane z komponentu Wallet category (dziecko), tutaj (rodzic)
        });
        console.log([event.target.id]);
        console.log([wallet]);
        console.log([walletCategory]);
        console.log(this.state.userList)
    }
    
    createUsersList = (event) =>{
        var{value} = event.target;
        var wallet = this.state.wallet;
        var userList = this.state.userList;
         /*
        if (event.target.checked) {
            if (!this.state.userList.includes(event.target.value)) {
              this.setState(prevState => ({ userList: [...prevState.userList, event.target.value]}))
              console.log(event.target.value)
              console.log(userList);
            }
          } else {
            this.setState(prevState => ({ userList: prevState.userList.filter(user => user !== event.target.value) }));
          }
        */
         
            if (!this.state.userList.includes(event.target.id)) {
              this.setState(prevState => ({ userList: [...prevState.userList, event.target.id]}))
              console.log(event.target.id)
              console.log(userList);
            }else {
            this.setState(prevState => ({ userList: prevState.userList.filter(user => user !== event.target.id) }));
          
          } 

          
        
       //userList = Array.from(document.querySelectorAll('input[class=DisplayedUsersList]:checked'), el=>el.value);
       

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
                        onSubmit={(e) => this.handleCreateWallet(e)}>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę..."
                                required
                                //value={wallet_holder.wallet.name || ''}
                                onChange={(e) => this.handleChangeName(e)}/>
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
                                //value={wallet_holder.wallet.description || ''}
                                onChange={(e) => this.handleChangeDescription(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
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
                            className="btn btn-primary btn-block form-button"
                            id = "mainbuttonstyle"
                            onClick={e =>{ 

                                        this.setState({submitted: true});
                                         this.testFunction(e);   

                                    }}
                            //disabled={loading}
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