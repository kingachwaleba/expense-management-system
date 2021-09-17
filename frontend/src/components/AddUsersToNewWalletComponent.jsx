import React, { Component } from 'react';
import DisplayUsersByName from './DisplayUsersByName';
import FindUsersToWalletService from '../services/FindUsersToWalletService';
import UserService from '../services/user.service';
class AddUsersToNewWalletComponent extends Component {


    constructor(props){

        super(props)
        this.state={
            
            usertoken: undefined,
            users: [],
            classNameHelper: ""

        }
    }


    componentDidMount() {
        const currentUser = UserService.getCurrentUser();
        if (currentUser) {

            
            this.setState({
                
                username: currentUser.login,
                usertoken: currentUser.token,
                
            });

            console.log(currentUser.login);
        }
        console.log(currentUser);
       // console.log("Token to: " + this.state.username);
        //console.log("Type to: " + user.type);
    }


    handleChange(e) {
        
        var {value} = e.target;
        
        console.log(value)
        console.log('USER TOKEN: ' + this.state.usertoken)

        FindUsersToWalletService.getUsers(value, this.state.usertoken).then((response)=>{
            this.setState({users: response.data})
        var users = this.state.users;
        console.log({users})
        
        

        })
        
    }

    setHelperClassname(user){
        var classNameHelper = this.state.classNameHelper;
        if(this.props.currentList.includes(user)){
            this.state.classNameHelper = "delete-user-from-list"
        }
        else{
            this.state.classNameHelper = "add-user-to-list"
           // console.log(this.state.classNameHelper)
        }
    }
    render() {
        return (
            <div>
                <label className="form-label"> Użytkownicy: </label>

                
                            <input
                                type="text"
                                className="form-control"
                                name="searchForUser"
                                placeholder="Znajdź użytkownika..."
                                required
                                //value=""
                                onChange={(e) => {
                                    
                                    
                                    this.handleChange(e);
                                    console.log("Aktualni użytkownicy do dodania: " + this.props.currentList)
                                    
                                }}
                            />

{
                         
                         this.state.users.map(
                             user =>
                             
                             <div key = {user} className = "center-content grid-container-3">
                               {
                                  this.setHelperClassname(user)
                               }

                                 <div>
                                     #ProfPic
                                 </div>
                                <div>
                                <label className = "form-label" htmlFor={user}>

                                        {user}
                                    
                                        
                                    </label>
                                </div>

                                <div>
                                    <input type="button"
                                        id={user} 
                                        //className = "DisplayedUsersList add-user-to-list"
                                        className = { this.state.classNameHelper}
                                        name ="user" 
                                        value="Click" 
                                        //onChange={this.props.createUsersList} 
                                        onReset={e=>{
                                            console.log('Załadowano')


                                        }}
                                        onClick={e => {
                                            this.props.createUsersList(e);
                                            console.log('Klik')

                                            if ( !document.getElementById(user).classList.contains('add-user-to-list') ){
                                                //document.getElementById('showing-content-lists').style.display = 'block';
                                                document.getElementById(user).classList.add('add-user-to-list');
                                                document.getElementById(user).classList.remove('delete-user-from-list');
                                            }
                                            else {
                                                //document.getElementById('showing-content-lists').style.display = 'none';
                                                
                                                document.getElementById(user).classList.remove('add-user-to-list');
                                                document.getElementById(user).classList.add('delete-user-from-list');
                                            }
                                            
                                        }}
                                    >
                                            
                                    </input>
                                 </div>
                                 
                                  
                                 
                             </div>
                             
                         )   
             }
                          

                
            </div>
        );
    }
}

export default AddUsersToNewWalletComponent;