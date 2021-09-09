import React, { Component } from 'react';

class ProfileDataComponent extends Component {
    render() {
        return (
            <div className="container content-center">
                <div className="profile-picture">

                </div>
                <div className="text-color box-content">
                    <p>Login: ...</p>
                    <p>Email: ...</p>
                    <p>Liczba porfeli: ...</p>
                    <p>Saldo: ...</p>
                    <a href="/edit-profile" className="card-link center-content btn btn-primary" id="mainbuttonstyle">Edytuj profil</a>
                </div>
            </div>
        );
    }
}

export default ProfileDataComponent;