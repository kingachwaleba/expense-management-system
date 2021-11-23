import axios from 'axios';
const CHANGE_PASSWORD_API_BASE_URL = "/account/change-password"
const DELETE_ACCOUNT_API_BASE_URL = "/mobile/delete-account"
const FORGOT_PASSWORD_API_BASE_URL = "/account/forgot-password"
const RESET_PASSWORD_API_BASE_URL = "/account/reset-password"
class UserService {


    login = (email, password) => {
        return axios.post("/login", {email, password})
            .then(response => {
                if (response.data.token) {
                    sessionStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            })
            .catch(err => {
                console.log("From user service error: " + err.message);
                throw err;
            });
    };

    logOut() {
        sessionStorage.removeItem('user');
    }

    register = async (user) => {
        return axios.post("/register", user);
    };

    getCurrentUser() {
        return JSON.parse(sessionStorage.getItem('user'));
    }
    
    changePassword(updatePasswordHolder,tokenStr){
        return axios.put(CHANGE_PASSWORD_API_BASE_URL, updatePasswordHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }

    deleteAccount(password,tokenStr){
        return axios.put(DELETE_ACCOUNT_API_BASE_URL, password, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "multipart/form-data"}});
    }

    forgotPassword(email){
        return axios.post(FORGOT_PASSWORD_API_BASE_URL,email,{headers: {"Content-Type" : "multipart/form-data"}});
    }
    resetPassword(data){
        return axios.post(RESET_PASSWORD_API_BASE_URL, data ,{headers: {"Content-Type" : "multipart/form-data"}});
    }
}

export default new UserService();