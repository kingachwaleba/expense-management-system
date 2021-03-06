import axios from "axios";
const ACCOUNT_DATA_API_BASE_URL = "/account";

class AccountService {

    getProfileInfo(tokenStr){
        return axios.get(ACCOUNT_DATA_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr}`}});
    }

  
}

export default new AccountService();