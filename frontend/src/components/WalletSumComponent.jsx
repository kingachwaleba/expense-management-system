import React, { Component } from 'react';

class WalletSumComponent extends Component {


    
    render() {
        return (
            <div className = "container box-content">
                <div className = "row grid-container">
                    <div>
                        <h3 className="center-content text-label" >Portfel </h3>
                        <div className="separator-line" ></div>
                        <h4>Line1</h4>
                        <h4>Line2</h4>
                        <h4>Line3</h4>
                        <h4>Line 4</h4>
                        <a href="/wallet" className="card-link center-content btn btn-primary" id="mainbuttonstyle">Przejdz do portfela</a>
                    </div>
                   
                </div>
               
            </div>
        );
    }
}

export default WalletSumComponent;