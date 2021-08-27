import axios from 'axios';

class WalletService {

    create_wallet = async (wallet_holder) => {
        return axios.post("/create-wallet", wallet_holder);
    };

  
}

export default new WalletService();