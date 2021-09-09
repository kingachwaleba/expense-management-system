import React, { Component } from 'react';
import Header from '../../components/Header';
import DisplayStatisticsComponent from '../../components/DisplayStatisticsComponent';

class StatisticsPage extends Component {
    render() {
        return (
            <div className = "container">
                <Header title="Statystyki"/>
                <div className="box-content">
                    <h4 className="text-label center-content">Portfel - statystyki</h4>
                    <div className="separator-line"></div>
                    <div className="box-subcontent-2">

                    </div>
                    <div className="box-subcontent grid-container-3">
                        <div className="up-arrow left-content"></div>
                        <div>
                            <h4 className="text-label center-content">Sprawdzany czas</h4>
                        </div>
                        <div className="down-arrow right-content"></div>

                    </div>
                    <div className="box-subcontent-2">
                        <DisplayStatisticsComponent/>
                    </div>
                    <div className="center-content">
                    <a href="/wallet" className="card-link center-content btn btn-primary width-100" id="mainbuttonstyle">Wróć</a>
                </div>
                </div>
             
            </div>
        );
    }
}

export default StatisticsPage;