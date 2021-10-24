import axios from 'axios';
const CREATE_WALLET_API_BASE_URL = "/create-wallet"
const WALLET_DETAIL_API_BASE_URL = "/wallet/"
const EDIT_WALLET_API_BASE_URL = "/wallet/"
class WalletService {
/*
    create_wallet = async (wallet_holder) => {
        return axios.post("/create-wallet", wallet_holder);
    };
*/
    create_wallet(wallet_holder, tokenStr){
        return axios.post(CREATE_WALLET_API_BASE_URL, wallet_holder, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
        //return axios.get(FIND_USERS_TO_WALLET_API_BASE_URL + infix, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    };

    getWalletDetail(id,tokenStr){
        return axios.get(WALLET_DETAIL_API_BASE_URL+id, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

    editWallet(id,newWalletData, tokenStr){
        return axios.put(EDIT_WALLET_API_BASE_URL+id, newWalletData, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
      
    };
  
}

export default new WalletService();