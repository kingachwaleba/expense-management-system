import axios from 'axios';

const ADD_EXPENSE_API_BASE_URL = "http://localhost:8080/wallet/"
class ExpenseService {

    addExpense(id, expenseHolder, tokenStr){
        return axios.post(ADD_EXPENSE_API_BASE_URL + id +"/add-expense",expenseHolder, {headers: {"Authorization" : `Bearer ${tokenStr} `, "Content-Type" : "application/json"}});
    };
    


}   

export default new ExpenseService();