import React from 'react';
import Header from '../../components/Header';
import UserService from "../../services/user.service";
import WalletSumComponent from '../../components/WalletSumComponent';
import UsersWalletsService from '../../services/UsersWalletsService';
class HomePage extends React.Component {

    constructor(props, context) {
        super(props, context);
        
        this.state = {
            username: undefined,
            userToken: undefined,
            s1: "Witaj ",
            s2: "!"
        }
    }

    

    componentDidMount() {
        const user = UserService.getCurrentUser();
        if (user) {
            this.setState({
                
                username: user.login,
                userToken: user.token,
                
            });

            console.log(user.login);
        }
        
        
        console.log(user);
        console.log("Token to: " + user.token);
        console.log("Type to: " + user.type);
    }

    render() {
        return (
          <div className="container">
              <Header title={this.state.s1.concat(this.state.username, this.state.s2)}/>
              <div className="center-content">
                    
                        <WalletSumComponent />
                     
                 
                         
                  
                </div>
            
                <div className="center-content">
                    <a href="/create-wallet" className="card-link center-content btn btn-primary" id="mainbuttonstyle">Dodaj nowy portfel</a>
                </div>
          </div>
        );
    }
}

export { HomePage };

