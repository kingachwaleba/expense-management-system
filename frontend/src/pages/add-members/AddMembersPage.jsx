import React, { Component } from 'react';
import AddUsersToNewWalletComponent from '../../components/AddUsersToNewWalletComponent';
import Header from '../../components/Header';
import { useLocation } from 'react-router';
import { Container } from 'react-bootstrap';
import UserService from '../../services/user.service';
import { Button } from 'reactstrap';
import ManageWalletUsersService from '../../services/ManageWalletUsersService';

class AddMembersPage extends Component {

    constructor(props, context) {
        super(props, context);
        console.dir(props);
        this.createUsersList = this.createUsersList.bind(this);
        

     };

     state = {
       
        userList: new Array,
        walletId: "",
        walletExist: "true"

     }

    
    componentDidMount(){
        this.setState({walletExist: "true"})
        console.log(this.state.walletExist)

        
        console.log(this.state.walletId)
    }
    componentWillMount(){
        let walletId = '';
        if (sessionStorage && sessionStorage.getItem('walletID')) {
           walletId = JSON.parse(sessionStorage.getItem('walletID'));
          }
         this.setState({walletId: walletId})
      }

    
    createUsersList = (event) =>{
        var{value} = event.target;
        //var wallet = this.state.wallet;
        var userList = this.state.userList;
     
         
            if (!this.state.userList.includes(event.target.id)) {
              this.setState(prevState => ({ userList: [...prevState.userList, event.target.id]}))
              
            }else {
            this.setState(prevState => ({ userList: prevState.userList.filter(user => user !== event.target.id) }));
          
          } 

    }
    render() {
       
        return (
            <Container>
                <Header title="Dodaj osoby"/>
                <div className="box-content">
                    <h4 className="text-label center-content">Portfel</h4>
                    <div className="separator-line"></div>
                        <AddUsersToNewWalletComponent createUsersList={this.createUsersList} currentList = {this.state.userList} walletExist={this.state.walletExist} walletId={this.state.walletId}/>  
                </div>
                <Button className="card-link main-button-style center-content btn btn-primary width-100 text-size" id="mainbuttonstyle"  
                                        onClick={e =>{ 
                                                
                                            const user = UserService.getCurrentUser();
                                            if (user) {
                                               var userToken = user.token
                                               var userName = user.name
                                             }
  
                                            if(this.state.userList.length != 0){
                                                console.log(this.state.userList)
                                                console.log(userToken)
                                                console.log("Wysłano zaproszenia")
                                                    this.state.userList.forEach((element)=>{
                                                        
                                                    ManageWalletUsersService
                                                    
                                                        .addNewUserToWallet(this.state.walletId,element,userToken)
                                                        .catch((error)=>{
                                                            console.log(error.response.data)

                                                        });
                                                    window.location.href='/wallet'
                                                })
                                            }
                                            else{
                                                console.log("Nie wysłano zaproszenia")
                                                alert("Musisz wybrać użytkownków dodania!");
                                            }

                                            

                                        }}> 
                                        Wyślij zaproszenia          
                </Button>
                            
            </Container>
        );
    }
}

export default AddMembersPage;