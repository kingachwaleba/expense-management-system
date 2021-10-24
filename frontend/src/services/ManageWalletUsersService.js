import axios from 'axios';
const CREATE_WALLET_API_BASE_URL = "/create-wallet"
const WALLET_DETAIL_API_BASE_URL = "/wallet/"
const MANAGE_WALLET_USERS_API_BASE_URL = "/wallet/"
//Manage wallet users api: "/wallet/{id}/users/{userLogin}"
class ManageWalletUsersService {

   

    editWalletUsers(id,newWalletData, tokenStr){
        return axios.put(MANAGE_WALLET_USERS_API_BASE_URL + id + +"/users/"+userLogin , newWalletData, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
    deleteWalletUsers(id,newWalletData, tokenStr){
        return axios.delete(MANAGE_WALLET_USERS_API_BASE_URL + id +  +"/users/"+userLogin, newWalletData, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
}

export default new ManageWalletUsersService();