import axios from 'axios';

const MANAGE_WALLET_USERS_API_BASE_URL = "http://localhost:8080/wallet/"
//Manage wallet users api: "/wallet/{id}/users/{userLogin}"
//Leave wallet: "/wallet/{id}/current-logged-in-user"
const GET_WALLET_USERS_API_BASE_URL = "http://localhost:8080/wallet-users/"
class ManageWalletUsersService {

   
    /*
    editWalletUsers(id,userLogin, tokenStr){
        return axios.put(MANAGE_WALLET_USERS_API_BASE_URL + id + +"/users/"+userLogin, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
    */
    deleteWalletUser(id,userLogin, tokenStr){
        return axios.delete(MANAGE_WALLET_USERS_API_BASE_URL + id +"/user/"+userLogin, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
    getWalletUsers(id, tokenStr){
        return axios.get(GET_WALLET_USERS_API_BASE_URL + id , {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }
    leaveWallet(id, tokenStr){
        return axios.delete(MANAGE_WALLET_USERS_API_BASE_URL + id + "/current-logged-in-user", {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

    addNewUserToWallet(id, username, tokenStr){
        return axios.put(MANAGE_WALLET_USERS_API_BASE_URL + id + "/users/"+username,"",{headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

    
    findUsersToWallet(id, infix, tokenStr){
        return axios.get(MANAGE_WALLET_USERS_API_BASE_URL + id + "/"+ infix, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }


}   

export default new ManageWalletUsersService();