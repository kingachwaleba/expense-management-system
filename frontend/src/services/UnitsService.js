import axios from 'axios';

const UNIT_API_BASE_URL = "http://localhost:8080/units"

class UnitsService {

    getUnits(tokenStr){
        return axios.get(UNIT_API_BASE_URL,  {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    };
    
    
}   

export default new UnitsService();