import React, { Component } from 'react';
import UsersWalletsService from '../services/UsersWalletsService';
import UserService from '../services/user.service';
import { Link } from 'react-router-dom';
import WalletPage from '../pages/wallet/WalletPage'
class WalletSumComponent extends Component {
    constructor(props){

        super(props)
        this.state={
            wallets: [],
            username: undefined,
            

        }
    }

componentDidMount(){
//----------------
const user = UserService.getCurrentUser();
        if (user) {
            this.setState({
                
                username: user.login,
                userToken: user.token,
                
            });

            console.log(user.login);
        }
        this.state.userToken = user.token
        
        console.log(user);
        console.log("Token komponentu to: " + user.token);
        console.log("userToken komponentu to: " + this.state.userToken);
//----------------


    UsersWalletsService.getUserWallets(user.token).then((response)=>{
        this.setState({wallets: response.data})

} )

}
    
    render() {
        return (
            <div className = "container">
                <div>
                    <div  className="grid-container">

                    {
                         
                         this.state.wallets.map(
                             wallet =>
                             <div key = {wallet.id} className = "center-content  box-content width-100">
                                 
                                    <h3 className="center-content text-label" > {wallet.name}  </h3>
                                <div className="separator-line" ></div>
                                    <h4>Właściciel: {wallet.owner}</h4>
                                    <h4>Kategoria: {wallet.walletCategory.name}</h4>
                                    <h4>Liczba członków: {wallet.userListCounter}</h4>
                                    <h4>Wydatki w portfelu: {wallet.walletExpensesCost}</h4>
                                  {console.log(wallet.id)}       
                                 
                                 
                              
                               
                                 
                                 <Link className="card-link center-content btn btn-primary"  id="mainbuttonstyle"
                                        to={{
                                        pathname: '/wallet', 
                                        state:{
                                            walletID: wallet.id
                                        }
                                            

                                        }}>Przejdź do portfela
                                       
                                            
                                </Link>
                             </div>
                             
                         )   
                        
             }

                    </div>
                   
                </div>
               
            </div>
        );
    }

   
}

export default WalletSumComponent;