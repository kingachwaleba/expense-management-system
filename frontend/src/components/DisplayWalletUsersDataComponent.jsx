import React, { Component } from 'react';

class DisplayWalletUsersDataComponent extends Component {
    render() {
        return (
            <div className="container">
                <div className="grid-container">
                    <h3 className="left-content text-label">Nazwa:</h3>
                    <h3 className="right-content text-label">Bilans:</h3>
                </div>
                <div className="separator-line"></div>
                
                <h4>Imie...    Bilans...</h4>
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
}

export default DisplayWalletUsersDataComponent;