import React, { Component } from 'react';
import Header from '../../components/Header';
class ChangePasswordPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title={"Zmiana \n hasła"}/>

                <div className="content form-container center-content">
                <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Podaj aktualne hasło: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder=""
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}/>
                            
                            <label className="form-label"  htmlFor="Name">Podaj nowe hasło: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder=""
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}/>

                            <label className="form-label"  htmlFor="Name">Powtórz nowe hasło: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder=""
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}/>
                            <br />
                            <a href="/profile" className="card-link center-content btn btn-primary" id="mainbuttonstyle" >Zapisz zmiany</a> 


                        </div>
                </div>
            </div>
        );
    }
}

export default ChangePasswordPage;