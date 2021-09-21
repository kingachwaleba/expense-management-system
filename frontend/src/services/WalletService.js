import axios from 'axios';
const CREATE_WALLET_API_BASE_URL = "/create-wallet"
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
  
}

export default new WalletService();