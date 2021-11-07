import axios from 'axios';
const CREATE_WALLET_API_BASE_URL = "/create-wallet"
const WALLET_DETAIL_API_BASE_URL = "/wallet/"
const EDIT_WALLET_API_BASE_URL = "/wallet/"

const GET_WALLET_STATS_API_BASE_URL = "http://localhost:8080/wallet/"
const PAY_DEBT_API_BASE_URL = "/pay-debt/wallet/"
class WalletService {

    create_wallet(wallet_holder, tokenStr){
        return axios.post(CREATE_WALLET_API_BASE_URL, wallet_holder, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    };

    getWalletDetail(id,tokenStr){
        return axios.get(WALLET_DETAIL_API_BASE_URL+id, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

    editWallet(id,newWalletData, tokenStr){
        return axios.put(EDIT_WALLET_API_BASE_URL+id, newWalletData, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
      
    };
    getWalletStats(id,dateFrom,dateTo,tokenStr){
        return axios.get(GET_WALLET_STATS_API_BASE_URL+id+"/stats/"+dateFrom+"/"+dateTo, {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "application/json"}});
    }
    payDebt(id, debtHolder, tokenStr){
        return axios.put(PAY_DEBT_API_BASE_URL+id, debtHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    }
}

export default new WalletService();