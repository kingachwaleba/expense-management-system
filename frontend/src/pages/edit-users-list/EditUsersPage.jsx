import React, { Component } from 'react';
import Header from '../../components/Header';
class EditUsersPage extends Component {
    render() {
        return (
            <div className="container">
                <Header title={"Edytuj \n członków"}/>
                <div className="box-content">
                    <h4 className="text-label center-content"> Portfel</h4>
                    <div className="separator-line"></div>
                    <div className="grid-container-3">
                        <div>
                            <h5 className="center-content"> #Picture</h5>
                        </div>
                        <div>
                             <h5 className="center-content"> #username</h5>
                        </div>
                        <div>
                            <h5 className="center-content"> #deleteIcon</h5>
                        </div>
                       
                    </div>
                    <div className="grid-container-3">
                        <div>
                            <h5 className="center-content"> #Picture</h5>
                        </div>
                        <div>
                             <h5 className="center-content"> #username</h5>
                        </div>
                        <div>
                            <h5 className="center-content"> #deleteIcon</h5>
                        </div>
                       
                    </div>
                    <div className="grid-container-3">
                        <div>
                            <h5 className="center-content"> #Picture</h5>
                        </div>
                        <div>
                             <h5 className="center-content"> #username</h5>
                        </div>
                        <div>
                            <h5 className="center-content"> #deleteIcon</h5>
                        </div>
                       
                    </div>
                    
                </div>
            </div>
        );
    }
}

export default EditUsersPage;