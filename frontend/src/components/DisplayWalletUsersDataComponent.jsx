import React, { Component } from 'react';

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
                                 {
                                   
                                    }
                                 <div className=" grid-container-3">

                                      <div className="left-content">
                                        <h4>{ userData.login }</h4>
                                    </div>
                                    <div className="right-content">
                                            <h4>{ userData.balance }</h4>
                                    </div>   
                                    <div className="right-content">
                                        <button></button>
                                    </div>
                                    

                                 </div>


                                 <div className = "box-subcontent">
                                        
                                 </div>
                                   
                                
                               

                               
                                 
                                  
                                 
                             </div>
                             
                         )   
             }
               
               
                <h4>Imie...    Bilans...</h4>
                <h4>Imie...    Bilans...</h4>
                <h4>Imie...    Bilans...</h4>
                <div className="separator-line"></div>
                <div className="center-content">
                    <a className="center-content href-text" href="/edit-users-list">Edytuj listę członków</a> 
                </div>
               
            </div>
        );
    
}

export default DisplayWalletUsersDataComponent;