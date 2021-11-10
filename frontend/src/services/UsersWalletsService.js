import axios from "axios";
const USERS_WALLETS_API_BASE_URL = "/wallets"

class UsersWalletsService {

    getUserWallets(tokenStr){
        return axios.get(USERS_WALLETS_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

}

export default new UsersWalletsService ();