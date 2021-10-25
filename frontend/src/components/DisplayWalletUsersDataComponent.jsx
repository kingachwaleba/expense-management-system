import React, { useEffect } from 'react';

import UserService from '../services/user.service';
const DisplayWalletUsersDataComponent = (props) =>{
   


    function setHelperClassname(user){
        var classNameHelper = this.state.classNameHelper;
        if(this.props.currentList.includes(user)){
            this.state.classNameHelper = "delete-user-from-list"
        }
        else{
            this.state.classNameHelper = "add-user-to-list"
          
        }
    }

    useEffect(()=>{
        
        const user = UserService.getCurrentUser();
       
    },[])


        return (
            <div className="container">
                <div className="grid-container-3">
                    <h3 className="left-content text-label">Nazwa:</h3>
                    <h3 className="right-content text-label">Bilans:</h3>
                    <h3 className="left-content text-label"></h3>
                </div>
                <div className="separator-line"></div>
                
               
                {
                         
                        props.usersData.map(
                             userData =>
                             
                             <div key = {userData.login} className = "center-content">
                                
                                 <div className=" grid-container-3 text-size">

                                      <div className="left-content">
                                        { userData.login }
                                    </div>
                                    <div className="right-content">
                                        { userData.balance }
                                    </div>   
                                    <div className="right-content">
                                        <button></button>
                                    </div>
                                    

                                 </div>

                                {/*
                                    <div className = "box-subcontent">
                                            
                                    </div>
                                */} 
                                
                               

                               
                                 
                                  
                                 
                             </div>
                             
                         )   
             }
            
          
                
            </div>
        );
    
}

export default DisplayWalletUsersDataComponent;