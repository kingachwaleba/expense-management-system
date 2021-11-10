import axios from 'axios';

const EXPENSE_API_BASE_URL = "/wallet/"
const EXPENSE_DETAIL_API_BASE_URL = "/expense/"
class ExpenseService {

    addExpense(id, expenseHolder, tokenStr){
        return axios.post(EXPENSE_API_BASE_URL + id +"/add-expense",expenseHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
    
    getWalletExpenses(id, tokenStr){
        return axios.get(EXPENSE_API_BASE_URL + id +"/expenses", {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    };

    getExpenseDetail(id,tokenStr){
        return axios.get(EXPENSE_DETAIL_API_BASE_URL + id, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    };

    deleteExpense(id, tokenStr){
    return axios.delete(EXPENSE_DETAIL_API_BASE_URL + id, {headers: {"Authorization" : `Bearer ${tokenStr} `}});
    }

    editExpense(id, expenseHolder ,tokenStr){
        return axios.put(EXPENSE_DETAIL_API_BASE_URL + id,expenseHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    }
}   

export default new ExpenseService();