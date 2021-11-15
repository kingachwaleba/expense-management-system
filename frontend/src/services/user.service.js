import axios from 'axios';
const CHANGE_PASSWORD_API_BASE_URL = "/account/change-password"
const DELETE_ACCOUNT_API_BASE_URL = "/mobile/delete-account"
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
}

export default new UserService();