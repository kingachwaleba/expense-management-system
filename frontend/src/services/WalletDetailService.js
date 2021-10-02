import axios from 'axios';

const WALLET_DETAIL_API_BASE_URL = "/wallet/"

class WalletDetailService {

   

    getWalletDetail(id,tokenStr){
        return axios.get(WALLET_DETAIL_API_BASE_URL+id, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }
  
}

export default new WalletDetailService();