import axios from 'axios';
import { BehaviorSubject } from 'rxjs';

const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));

class UserService {

    get currentUserValue() {
        return currentUserSubject.value;
    }

    get currentUser() {
        return currentUserSubject.asObservable();
    }

    login(user) {
        // btoa: Basic64 encryption
        // noinspection JSDeprecatedSymbols
        const headers = {
            authorization: 'Basic ' + btoa(user.login + ':' + user.password),
        };

        return axios.get("http://localhost:8080/login", { headers: headers})
            .then(response => {
                response.data.password = user.password; // Store pure password.
                localStorage.setItem('currentUser', JSON.stringify(response.data));
                currentUserSubject.next(response.data);
            })
    }

    logOut() {
        return axios.post("http://localhost:8080/logout", {})
            .then(() => {
                localStorage.removeItem('currentUser');
                currentUserSubject.next(null);
            })
    }

    register(user) {
        return axios.post("http://localhost:8080/register", user);
    }
}

export default new UserService();