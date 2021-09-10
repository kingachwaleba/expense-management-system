import React, { Component } from 'react';
import DisplayUsersByName from './DisplayUsersByName';
class AddUsersToNewWalletComponent extends Component {
    render() {
        return (
            <div>
                <label className="form-label"> Użytkownicy: </label>
                            <input
                                type="text"
                                className="form-control"
                                name="description"
                                placeholder="Znajdź użytkownika..."
                                required
                                value=""
                                onChange={(e) => this.handleChange(e)}
                            />
                            <DisplayUsersByName/>

                
            </div>
        );
    }
}

export default AddUsersToNewWalletComponent;