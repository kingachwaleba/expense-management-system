import axios from 'axios';

class UserService {

    login = (login, password) => {
        return axios.post("/login", {login, password})
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            })
            .catch(err => {
                console.log(err);
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