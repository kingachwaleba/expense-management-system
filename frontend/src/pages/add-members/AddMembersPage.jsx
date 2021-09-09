import React, { Component } from 'react';
import AddUsersToNewWalletComponent from '../../components/AddUsersToNewWalletComponent';
import Header from '../../components/Header';
class AddMembersPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title="Dodaj osoby"/>
                <div className="box-content">
                    <h4 className="text-label center-content">Portfel</h4>
                    <div className="separator-line"></div>
                    <AddUsersToNewWalletComponent/>  
                </div>
                
            </div>
        );
    }
}

export default AddMembersPage;