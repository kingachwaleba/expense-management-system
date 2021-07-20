import React from "react";
import {Redirect} from "react-router-dom";
import UserService from "../services/user.service";

class AuthGuard extends React.Component {


    constructor(props, context) {
        super(props, context);
    }

    render() {
        const {component: Component} = this.props;
        const currentUser = UserService.currentUserValue;

        if (!currentUser) {
            return (<Redirect to={{pathname: '/login'}}/>)
        }

        return (<Component/>);
    }
}

export default AuthGuard;