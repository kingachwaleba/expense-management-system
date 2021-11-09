import axios from "axios";
const WALLET_CATEGORY_API_BASE_URL = "/wallet-categories"

class WalletCategoryService {

    getCategories(){
        return axios.get(WALLET_CATEGORY_API_BASE_URL);
    }

}

export default new WalletCategoryService();