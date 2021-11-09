import axios from "axios";
const STATUS_API_BASE_URL = "/statues"

class StatusService {

    getStatues(){
        return axios.get(STATUS_API_BASE_URL);
    }

}

export default new StatusService();