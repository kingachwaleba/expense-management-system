import axios from 'axios';

const EXPENSE_API_BASE_URL = "http://localhost:8080/wallet/"
const EXPENSE_DETAIL_API_BASE_URL = "http://localhost:8080/expense/"
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
}   

export default new ExpenseService();