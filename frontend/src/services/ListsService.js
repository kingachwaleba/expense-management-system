import axios from 'axios';

const LISTS_API_BASE_URL = "http://localhost:8080/wallet/"
const EXPENSE_DETAIL_API_BASE_URL = "http://localhost:8080/expense/"
const LISTS_SUM_API_BASE_URL = "/wallet/{id}/shopping-lists"
const LIST_DETAIL_API_BASE_URL = "/shopping-list/"
class ExpenseService {

    createList(id, ListHolder, tokenStr){
        return axios.post(LISTS_API_BASE_URL + id +"/create-shopping-list", ListHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    };
    
    getWalletLists(id,tokenStr){
        return axios.get(LISTS_API_BASE_URL + id +"/shopping-lists", {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }
    
    getListDetail(id, tokenStr){
        return axios.get(LIST_DETAIL_API_BASE_URL + id, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }
    
}   

export default new ExpenseService();