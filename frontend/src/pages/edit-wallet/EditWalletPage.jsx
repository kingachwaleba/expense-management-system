import React, { Component } from 'react';
import Header from '../../components/Header';
import WalletCategoryComponent from '../../components/WalletCategoryComponent';
class EditWalletPage extends Component {
    render() {
        return (
            <div>
                <div className="container">
                 <Header title='Edytuj portfel:' />
                <div className="form-container">

                <div className="box-content">
                    <h4 className="text-label center-content" >Edytuj profil</h4>
                    <div className="separator-line"></div>
                    <form
                        name="form"
                        method="post"
                        onSubmit={(e) => this.handleRegister(e)}>
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder="Wpisz nazwę..."
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                A valid login is required.
                            </div>
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Description">Opis: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="description"
                                placeholder="Wpisz opis..."
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}/>
                            <div className="invalid-feedback">
                                Password is required.
                            </div>
                        </div>
                        
                        <div>
                        <WalletCategoryComponent />
                    
                        
                        
                        </div>
                    
                        <br></br>
                        <br></br>
                        <button
                            className="btn btn-primary btn-block form-button"
                            id = "mainbuttonstyle"
                            onClick={() =>  this.setState({submitted: true})
                                            

                                    }
                            >
                            Zapisz zmiany
                        </button>
                    
                    </form>
                  </div>
                </div>
            </div>
        </div>
        );
    }
}

export default EditWalletPage;