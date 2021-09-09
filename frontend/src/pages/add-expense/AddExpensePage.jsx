import React, { Component } from 'react';
import Header from '../../components/Header';
import UsersToExpenseComponent from '../../components/UsersToExpenseComponent';
import ExpenseCategoryComponent from '../../components/ExpenseCategoryComponent';
class AddExpensePage extends Component {
    render() {
        return (
            <div className="container">
                <Header title="Dodaj wydatek"/>
                <div className="box-content">
                    <h4 className="text-label center-content">Dodaj wydatek</h4>
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
                                            
                                        </div>

                                        <div className={'form-group'}>
                                            <label className="form-label" htmlFor="Value">Kwota: </label>
                                            <input
                                                type="text"
                                                className="form-control"
                                                name="value"
                                                placeholder="Podaj kwote..."
                                                required
                                                value=""
                                                onChange={(e) => this.handleChange(e)}/>
                                           
                                        </div>
                                        <div className={'form-group'}>
                                           
                                               <ExpenseCategoryComponent/>
                                          
                                            
                                        </div>
                                        <div className="box-subcontent-2">
                                                <h5 className="label-text left-content">Cykliczność:</h5>
                                                <div>
                                                #Cykliczność
                                                </div>
                                        </div>
                                        <div>
                                        
                                            <UsersToExpenseComponent/>
                                        
                                        
                                        </div>
                                    
                                        <br></br>
                                        <br></br>
                                        <button
                                            className="btn btn-primary btn-block form-button"
                                            id = "mainbuttonstyle">
                                            Utwórz
                                        </button>
                                
                                    </form>
                                   
                                </div>
                                
                        </div>
        );
    }
}

export default AddExpensePage;