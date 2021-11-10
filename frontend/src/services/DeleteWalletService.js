import axios from 'axios';

const DELETE_WALLET_API_BASE_URL = "/wallet/"

class DeleteWalletService {

   

    deleteWallet(id,tokenStr){
        return axios.delete(DELETE_WALLET_API_BASE_URL+id, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }
  
}

export default new DeleteWalletService();