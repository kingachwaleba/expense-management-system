import axios from 'axios';

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
}

export default new UserService();