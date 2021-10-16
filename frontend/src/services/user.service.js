import axios from 'axios';

class UserService {

    login = (email, password) => {
        return axios.post("/login", {email, password})
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            })
            .catch(err => {
                console.log("From user service error: " + err.message);
                throw err;
            });
    };

    logOut() {
        localStorage.removeItem('user');
    }

    register = async (user) => {
        return axios.post("/register", user);
    };

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }
}

export default new UserService();