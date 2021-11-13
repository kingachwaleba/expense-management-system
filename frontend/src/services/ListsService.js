import axios from 'axios';

const LISTS_API_BASE_URL = "/wallet/"
const EXPENSE_DETAIL_API_BASE_URL = "/expense/"
const LISTS_SUM_API_BASE_URL = "/wallet/{id}/shopping-lists"
const LIST_DETAIL_API_BASE_URL = "/shopping-list/"
const EDIT_LIST_DETAIL_API_BASE_URL = "/edit-list-element/"
const DELETE_LIST_ELEMENT_API_BASE_URL = "/delete-list-element/"
const CHANGE_ELEMENT_STATUS_API_BASE_URL = "/change-element-status/"
const CHANGE_LIST_STATUS_API_BASE_URL = "/change-list-status/"
const CHANGE_LIST_NAME_API_BASE_URL = "/mobile/shopping-list/edit/"
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

    getListData(id,tokenStr){
        return axios.get(LIST_DETAIL_API_BASE_URL + id, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }

    editListElement(id, listDetail,tokenStr){
        return axios.put(EDIT_LIST_DETAIL_API_BASE_URL + id, listDetail, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }
    editListName(id, name, tokenStr){
        return axios.put(CHANGE_LIST_NAME_API_BASE_URL + id, name, {headers: {"Authorization" : `Bearer ${tokenStr}`,"Content-Type" : "multipart/form-data"}});
    }
    addListElement(id, listDetail, tokenStr){
        return axios.post(LIST_DETAIL_API_BASE_URL+ id, listDetail, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }
    deleteListElement(id, tokenStr){
        return axios.delete(DELETE_LIST_ELEMENT_API_BASE_URL+id ,{headers: {"Authorization" : `Bearer ${tokenStr} `}})
    }
    changeElementStatus(id, status, tokenStr){
        return axios.put(CHANGE_ELEMENT_STATUS_API_BASE_URL+id, status,{headers: {"Authorization" : `Bearer ${tokenStr}`, "Content-Type" : "application/json" }})
    }
    changeListStatus(id, status, tokenStr){
        return axios.put(CHANGE_LIST_STATUS_API_BASE_URL+id, status,{headers: {"Authorization" : `Bearer ${tokenStr}`, "Content-Type" : "application/json" }})
    }

    deleteList(id,tokenStr){
        return axios.delete(LIST_DETAIL_API_BASE_URL + id, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }
}   

export default new ExpenseService();