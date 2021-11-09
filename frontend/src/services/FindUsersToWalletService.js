import axios from "axios";
const FIND_USERS_TO_WALLET_API_BASE_URL = "/find-users/"

class FindUsersToWalletService {

    getUsers(infix, tokenStr){
        return axios.get(FIND_USERS_TO_WALLET_API_BASE_URL + infix, {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

}

export default new FindUsersToWalletService();