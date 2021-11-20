import React, { Component } from 'react';
import DisplayUsersByName from './DisplayUsersByName';
import FindUsersToWalletService from '../services/FindUsersToWalletService';
import UserService from '../services/user.service';
import ManageWalletUsersService from '../services/ManageWalletUsersService';
import picture from '../profile_picture_placeholder.jpg'
import ImageService from '../services/ImageService';
class AddUsersToNewWalletComponent extends Component {


    constructor(props){

        super(props)
        this.state={
            
            usertoken: undefined,
            users: [],
            classNameHelper: "",
            walletExist: this.props.walletExist,
            errorMessage: "",
            profileImgUrl:""
        }
    }


    componentDidMount() {
        const currentUser = UserService.getCurrentUser();
        if (currentUser) {
            this.setState({
                username: currentUser.login,
                usertoken: currentUser.token,
            });
        }
    }


    handleChange(e) {
        
        var {value} = e.target;
        if(value){
        
        if(!this.state.walletExist){
            FindUsersToWalletService
                .getUsers(value, this.state.usertoken)
                .then((response)=>{
                    this.setState({users: response.data})
                    var users = this.state.users;
                    if(users.length == 0){
                        this.setState({errorMessage: "Brak użytkowników"})
                       
                        console.log(this.state.errorMessage)
                    }
                    else{
                        this.setState({errorMessage: ""})
                        
                        console.log(this.state.errorMessage)
                    }
                })
                .catch((error)=>{
                    
                    console.log(error.response.data)
                    
                    this.setState({errorMessage: error.response.data})
                });
        }
        else{
            ManageWalletUsersService
                .findUsersToWallet(this.props.walletId,value, this.state.usertoken)
                .then((response)=>{
                    this.setState({users: response.data})
                    var users = this.state.users;
                    if(users.length == 0){
                        this.setState({errorMessage: "Brak użytkowników"})
                        console.log("NIE ZNALEZIONO")
                        console.log(this.state.errorMessage)
                    }
                    else{
                        this.setState({errorMessage: ""})
                        console.log("ZNALEZIONO")
                        console.log(this.state.errorMessage)
                    }
                })
                .catch((error)=>{
                    console.log(error)
                });
        }
       } 
    }

    setHelperClassname(user){
        var classNameHelper = this.state.classNameHelper;
        if(this.props.currentList.includes(user)){
            this.state.classNameHelper = "icons-size-2 delete-user-from-list"
        }
        else{
            this.state.classNameHelper = "icons-size-2 add-user-to-list"
          
        }
    }

    setProfilePicHelper(imageURL, login){
        if(imageURL === null){
            return (<img src={picture} className="profile-img-preview" alt="profilePic"/>)
        }
        else{
            ImageService.getUserProfileImg(login, this.state.usertoken)
            .then((response)=>{
                var profilePic = URL.createObjectURL(response.data)
            
                this.setState({profileImgUrl: profilePic})
                console.log("Response:",response.data)
                console.log("ProfilePic:",profilePic)
             
            })
            .catch((error)=>{
                console.log(error)
         
            })
            return (<img src={this.state.profileImgUrl} className="profile-img-preview" alt="profilePic"/>)
        }
        
    }
    render() {
        return (
            <div>
                <label className="form-label text-size"> Użytkownicy: </label>

                
                            <input
                                type="text"
                                className="form-control"
                                name="searchForUser"
                                placeholder="Znajdź użytkownika..."
                                pattern = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\d.-]{0,45}$"
                                onChange={(e) => {
                                    
                                    
                                    this.handleChange(e);
                                   // console.log("Aktualni użytkownicy do dodania: " + this.props.currentList)
                                    
                                }}
                            />
                       <div className="text-size error-text">
                           {this.state.errorMessage}
                        </div> 
                        <br />

{

                        
                         this.state.users.map(
                             user =>
                             
                             <div key = {user.login} className = "center-content grid-container-3 text-size">
                               {
                                  this.setHelperClassname(user.login)
                               }
                                    
                                 <div className="icons-size">
                                       {/*
                                           <img src={this.setProfilePicHelper(user.image, user.login)} className="profile-img-preview" alt="profilePic" />
                                       */ } 
                                       {
                                           this.setProfilePicHelper(user.image, user.login)
                                       }
                                 </div>
                                <div>
                                <label className = "form-label" htmlFor={user.login}>

                                        {user.login}
                                    
                                        
                                    </label>
                                </div>

                                <div>
                                    <input type="button"
                                        id={user.login} 
                                        className = { this.state.classNameHelper }
                                        name ="user" 
                                        value="Click" 
                                        
                                      
                                        onClick={e => {
                                            this.props.createUsersList(e);
                                           

                                            if ( !document.getElementById(user.login).classList.contains('add-user-to-list') ){
                                                
                                                document.getElementById(user.login).classList.add('add-user-to-list');
                                                document.getElementById(user.login).classList.remove('delete-user-from-list');
                                            }
                                            else {
                                                
                                                
                                                document.getElementById(user.login).classList.remove('add-user-to-list');
                                                document.getElementById(user.login).classList.add('delete-user-from-list');
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