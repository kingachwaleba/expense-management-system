import React from 'react';
import Header from '../../components/Header';
import UserService from "../../services/user.service";
import WalletSumComponent from '../../components/WalletSumComponent';
class HomePage extends React.Component {

    constructor(props, context) {
        super(props, context);
        
        this.state = {
            username: undefined,
            s1: "Witaj ",
            s2: "!"
        }
    }

    

    componentDidMount() {
        const user = UserService.getCurrentUser();
        if (user) {
            this.setState({
                
                username: user.login,
                
            });

            console.log(user.login);
        }
        console.log(user);
       
    }

    render() {
        return (
          <div className="container">
              <Header title={this.state.s1.concat(this.state.username, this.state.s2)}/>
              <div className="grid-container center-content">
                    
                        <WalletSumComponent />
                     
                 
                        <WalletSumComponent />   
                   
                   
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

