import React, { Component } from 'react';

class UsersToExpenseComponent extends Component {
    render() {
        return (
            <div className="box-subcontent-2">
                <h5 className="label-text left-content">Kogo dotyczy wydatek:</h5>
                <div>
                   <input type="checkbox" id="" name="User"/>
                    <label for="User">User</label> 
                </div>
                <div>
                   <input type="checkbox" id="" name="User"/>
                    <label for="User">User</label> 
                </div>
                
                <div>
                   <input type="checkbox" id="" name="User"/>
                    <label for="User">User</label> 
                </div>
                <div>
                   <input type="checkbox" id="" name="User"/>
                    <label for="User">User</label> 
                </div>
                
                
            </div>
        );
    }
}

export default UsersToExpenseComponent;