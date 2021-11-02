import axios from 'axios';

const LISTS_API_BASE_URL = "http://localhost:8080/wallet/"
const EXPENSE_DETAIL_API_BASE_URL = "http://localhost:8080/expense/"
class ExpenseService {

    createList(id, ListHolder, tokenStr){
        return axios.post(LISTS_API_BASE_URL + id +"/create-shopping-list", ListHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    };
    
    
}   

export default new ExpenseService();