import React, { Component } from 'react';

class NotificationComponent extends Component {
    render() {
        return (
            <div className="container">
                <div className="box-content">
                <h3 className="text-label">Powiadomienie</h3>
                <div className="separator-line"></div>
                <h5>Właściciel: ...</h5>
                <h5>Liczba członków: ...</h5>
                <a href="/" className="card-link center-content btn btn-primary" id="mainbuttonstyle" >Zaakceptuj</a>
                
                <a href="/" className="card-link center-content btn btn-primary" id="mainbuttonstyle" >Odrzuć</a>
               </div>
            </div>
        );
    }
}

export default NotificationComponent;