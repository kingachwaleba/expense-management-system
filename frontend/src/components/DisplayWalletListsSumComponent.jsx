import React, { Component } from 'react';

class DisplayWalletListsSumComponent extends Component {
    render() {
        return (
            <div className="container  box-subcontent-2">
                 <h3 className="center-content text-label">Nazwa</h3>
                 <div className="separator-line"></div>
                 <div className="grid-container">
                    <div className="right-content">
                       <h4 className="text-label"> Liczba pozycji:</h4>  
                    </div>
                    <div className="left-content">
                       <h4>...</h4>  
                    </div>
                   
                </div>
                <div className='separator-line'></div>
                <br />
                <div className="center-content">
                    <a className="center-content href-text" href="/list-detail">Zobacz szczegóły</a> 
                </div>
            </div>
        );
    }
}

export default DisplayWalletListsSumComponent;