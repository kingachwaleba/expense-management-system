
import React from 'react';
import WalletService from '../../services/WalletService';
import {User} from '../../models/user';
import {Wallet} from '../../models/wallet';
import {WalletCategory} from '../../models/walletCategory';
import {WalletHolder} from '../../models/helpers/wallet-holder';
import Header from '../../components/Header';
import WalletCategoryComponent from '../../components/WalletCategoryComponent';
import AddUsersToNewWalletComponent from '../../components/AddUsersToNewWalletComponent';
class CreateWalletPage extends React.Component {

    constructor(props, context) {
        super(props, context);

     }

        state = {
            wallet_holder: new WalletHolder('', ''),
            wallet: new Wallet('','',''), 
            userList: new Array,
           
            submitted: false,
            loading: false,
            errorMessage: '',
        };

       
   
    
    


    handleChange(e) {
        var {name, value} = e.target;
        var wallet_holder = this.state.wallet_holder;
        var wallet = this.state.wallet;
        var userList = this.state.userList;
       
        wallet_holder[wallet.name] = value;
        wallet_holder[wallet.description] = value;
         
        wallet_holder[wallet.walletCategory] = value;
        /*
        wallet_holder[] = value;
        */
        wallet_holder[userList] = value;
        this.setState({wallet_holder: wallet_holder});
    }

    handleCreateWallet(e) {
        e.preventDefault();
        this.setState({submitted: true});
        const {wallet_holder} = this.state;

        //validate form
        if (!wallet_holder.wallet.name || !wallet_holder.wallet.description) {
            return;
        }

        this.setState(({loading: true}));
        WalletService.create_wallet(wallet_holder)
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
    }
    changeWalletCategory(checkedID){
        var wallet_category = this.wallet_holder.walletCategory;
        this.setState({
            //wallet_category = checkedID
            //Dane między komponentami można pozyskiwać jako props gdy mają np wspólnego parenta, pozystakć dane z komponentu Wallet category (dziecko), tutaj (rodzic)
        })
        console.log({wallet_category})
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
                        onSubmit={(e) => this.handleRegister(e)}>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę..."
                                required
                                value={wallet_holder.wallet.name}
                                onChange={(e) => this.handleChange(e)}/>
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
                                value={wallet_holder.wallet.description}
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>
                        <div className={'form-group'}>
                        <AddUsersToNewWalletComponent/>
                        </div>
                        <div>
                        <WalletCategoryComponent />
                    
                        
                        
                        </div>
                    
                        <br></br>
                        <br></br>
                        <button
                            className="btn btn-primary btn-block form-button"
                            id = "mainbuttonstyle"
                            onClick={() =>  this.setState({submitted: true})
                                            

                                    }
                            disabled={loading}>
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