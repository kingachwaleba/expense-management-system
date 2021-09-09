import React, { Component } from 'react';
import Header from '../../components/Header';
class ExpenseDetailPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title="Wydatek"/>
                <div className="box-content">
                                <div className="center-content">
                                    <h2 className="text-label ">Wydatki</h2> 
                                </div>
                                <div className="separator-line"></div>
                                <div>
                                    <h5>Komponent z informacjami o wydatku </h5>
                                </div>
                    
                </div>
            </div>
        );
    }
}

export default ExpenseDetailPage;