import React, { Component } from 'react';
import Header from '../../components/Header';

class DeleteAccountPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title={"USUWANIE \n KONTA"}/>
                <div className="center-content form-container"> 
                    <h4>Czy na pewno chcesz usunąć konto?</h4>
                    <h6 className="text-label">UWAGA: Proces jest nieodwracalny.</h6>
                    <br />
                    <input
                                type="text"
                                className="form-control"
                                name="password"
                                placeholder="Podaj hasło"
                                required
                                value=""
                    />
                    <br />
                    <a href="/profile" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Tak-usuń konto</a>
                    <br />
                    <a href="/profile" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Nie-zabierz mnie stąd</a>
                </div>
            </div>
        );
    }
}

export default DeleteAccountPage;