import React, { Component } from 'react';

class DisplayWalletDataComponent extends Component {
    render() {
        return (
            <div>
                <h3 className="text-label center-content">Portfel</h3>
                <div className="separator-line"></div>
                <h4>Opis:....</h4>
                <div className="separator-line"></div>
                <h4>Kategoria:....</h4>
                <h4>Właściciel:....</h4>
                <h4>Liczba członków:....</h4>
                <h4>Saldo:....</h4>
                <h4 className="center-content">Wydatki.... Twoje Wydatki:.... Bilans:....</h4>
                <div className="separator-line"></div>
            </div>
        );
    }
}

export default DisplayWalletDataComponent;