import React, { Component } from 'react';
import Header from '../../components/Header';
class EditProfilePage extends Component {
    render() {
        return (
            <div className="container">
                <Header title="Edytuj profil"/>
                <div className="center-content">
                    <a href="/change-password" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Zmień hasło</a>
                    <br></br>
                    <br></br>
                    <a href="/delete-account" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Usuń konto</a>
                    <br></br>
                    <br></br>
                    <a href="/profile" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle" >Zapisz zmiany</a>
                </div>
            </div>
        );
    }
}

export default EditProfilePage;