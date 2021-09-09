import React, { Component } from 'react';
import Header from '../../components/Header';
class CreateListPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title ="Tworzenie listy"/> 
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Wydatki</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <h4> komponent z aktualnymi pozycjami na liście </h4>

                </div>
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Dodaj produkt do listy</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <h4> komponent z formularzem dodawania </h4>

                </div>
                <div className="center-content">
                    <a href="" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Utwórz</a>
                </div>
                <div className="center-content">
                    <a href="/wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Wróć</a>
                </div>
                
                
            </div>
        );
    }
}

export default CreateListPage;