import React from 'react';
import Header from '../../components/Header';
import { Container, Row, Col} from 'reactstrap';
import UserService from '../../services/user.service';
import WalletDetailService from '../../services/WalletDetailService';
import { useState } from 'react';
import { useEffect } from 'react';
import { category } from '../../models/category';
import ExpenseService from '../../services/ExpenseService';
import CategoriesService from '../../services/CategoriesService';
import { Expense } from '../../models/expense';
import { ExpenseHolder } from '../../models/helpers/expenseHolder';
import ImageService from '../../services/ImageService';

function EditExpensePage () {
    var submitted = false;
    let walletID = '';
    if (sessionStorage && sessionStorage.getItem('walletID')) {
        walletID = JSON.parse(sessionStorage.getItem('walletID'));
    }
    let expenseID = '';
    if (sessionStorage && sessionStorage.getItem('expenseID')) {
        expenseID = JSON.parse(sessionStorage.getItem('expenseID'));
    }
    let currentExpenseUsersList= '';
    if (sessionStorage && sessionStorage.getItem('currentExpenseUsersList')) {
        currentExpenseUsersList = JSON.parse(sessionStorage.getItem('currentExpenseUsersList'));
    }
    const userData = UserService.getCurrentUser()

    const userToken = UserService.getCurrentUser()
    const [expenseCategories, getExpenseCategories] = useState([])
    const [walletUsers, setWalletUsers] = useState([])

    const [expenseName, setExpenseName] = useState("")
    const [currentExpenseName, setCurrentExpenseName] = useState("")

    const [expensePrice, setExpensePrice] = useState("")
    const [currentExpensePrice, setCurrentExpensePrice] = useState("")

    const [expenseCategory, setExpenseCategory] = useState([])
    const [currentExpenseCategory, setCurrentExpenseCategory] = useState([])

    const [expenseUsersList, setExpenseUsersList] = useState([])
 

    const [expenseReceiptImg, setExpenseReceiptImg] = useState()
    const [currentExpenseReceiptImg, setCurrentExpenseReceiptImg] = useState()

    

    const [imagePreview, setImagePreview]=useState(null)
    const [displayImg, setDisplayImg] = useState (null)
    const [message, setMessage] = useState("")
    const [errorMessage, setErrorMessage]=useState("")
    useEffect(()=>{
        const userData = UserService.getCurrentUser();
        
       
        WalletDetailService.getWalletDetail(walletID,userData.token)
        .then((response)=>{
            
            setWalletUsers(response.data.userList)
        })
        .catch(error=>{
            console.error({error})
        });
        
       


            ExpenseService.getExpenseDetail(expenseID,userData.token)
            .then((response)=>{
                //--------------------------------------------
                CategoriesService.getCategories(userData.token)
                .then((response)=>{
                    const allCategories = response.data
                    getExpenseCategories(allCategories)
                 
    
                   
                })
                .catch(error=>{
    
                    console.log({error})
                   
                })
                 //--------------------------------------------
                console.log("Get expenseDetail data (responseData)")
                console.log(response.data)
        
                console.log("Expense category (responseData)")
                console.log(response.data.expense.category)
                setCurrentExpenseCategory(response.data.expense.category)
                setExpenseCategory(response.data.expense.category)

                setCurrentExpensePrice(response.data.expense.total_cost)
                setExpensePrice(response.data.expense.total_cost)

                setCurrentExpenseName(response.data.expense.name)
                setExpenseName(response.data.expense.name)
                setExpenseUsersList(currentExpenseUsersList)
                setCurrentExpenseReceiptImg(response.data.expense.receipt_image)
           
            })
           
            .catch(error=>{
                console.error({error})
            });
                   
          
    },[])

 


    useEffect(()=>{
        if(currentExpenseReceiptImg != null){
          setMessage("")
          document.getElementById('img-preview-div').style.display = 'block';
          /*
          var formData = new FormData();
              formData.append('imageName',receiptImg)
          console.log(receiptImg)
          console.log(formData.get('imageName')) 
  
          */
          ImageService.getImg(expenseID, userData.token)
          .then(response=>{
              console.log(response)
              const url = URL.createObjectURL(response.data)
              setDisplayImg(url)
              console.log(url)
          })
          .catch(error=>{
              console.error(error)
          });
         
  
        }
        else{
            setMessage("Brak zdjęcia paragonu")
            document.getElementById('img-preview-div').style.display = 'none';
        }
      },[currentExpenseReceiptImg])

    useEffect(()=>{
       
    },[expenseCategory])
    useEffect(()=>{
       
    },[currentExpenseCategory])
    useEffect(()=>{
       
    },[imagePreview])

   
    function handleDefaultCheckedUsers(data){
        if(currentExpenseUsersList.includes(data)){
            console.log("AKTUALNA LISTA ZAWIERA TEGO USERA")
        }
        else{
            console.log("AKTUALNA LISTA NIE ZAWIERA TEGO USERA")
            return false
        }
    }
    function handleChangeName(e) {
        var {value} = e.target;
        setExpenseName(value)
        setErrorMessage("")
    }
    function handleChangePrice(e) {
        var {value} = e.target;
        setExpensePrice(value)
        setErrorMessage("")
    }

    function readExpenseCategory (e){
        var {id, value} = e.target;
        
        setExpenseCategory(new category(id,value)) 
        setErrorMessage("")
    }

    function handleCreateExpenseUsersList(e){
        let index
        const currentList = expenseUsersList
        console.log("Lista przed")
        console.log(expenseUsersList)
        if(e.target.checked){
            if(!currentList.includes(e.target.value)){
            currentList.push(e.target.value)
            setErrorMessage("")
             }
        }
        else {
            if(currentList.includes(e.target.value)){

                index = currentList.indexOf(e.target.value)
            currentList.splice(index, 1)
            if(currentList.length == 0) setErrorMessage("Lista musi zawierać co najmniej jednego użytkownika.")
            }

        }
        setExpenseUsersList(currentList)  
        console.log("Lista po")
        console.log(expenseUsersList)
        
    }

    function handleImageChange(e){
        
        if (e.target.files && e.target.files[0]) {
            setDisplayImg(URL.createObjectURL(e.target.files[0]))
           
            document.getElementById('img-preview-div').style.display = 'block';
            let img = e.target.files[0];
            var formData = new FormData();
            formData.append('image',e.target.files[0])
            console.log(formData)
            ImageService.uploadImg(formData, userToken.token)
            .then((response)=>{
                setExpenseReceiptImg(response.data)
            })
            .catch((error)=>{
                console.log(error)
                 setErrorMessage(error.response.data)
            })
           
           
          
          }
    }
    function handleDefaultCheck(data){
        if(data === currentExpenseCategory.name){
            
           return true; 
        }
        else{
            
           return false; 
        } 
    }
    function handleClearChoosenImg(e){
        document.getElementById("insert-photo-button").value = "";
        setDisplayImg(null)
        setExpenseReceiptImg(null)
        document.getElementById('img-preview-div').style.display = 'none';
    }
    function handleCreateExpense(e){
        e.preventDefault();
        submitted = true;
        if(expenseUsersList.length == 0){setErrorMessage("Lista musi zawierać conajmniej jednego użytkownika.")} 
        else{
        const expense = new Expense("",expenseName,expenseReceiptImg,expensePrice,expenseCategory)
        const expenseHolder = new ExpenseHolder(expense, expenseUsersList);
        ExpenseService.editExpense(expenseID, expenseHolder, userToken.token)
        .catch(error=>{
            console.error({error})
            setErrorMessage(error.response.data)
        });
        console.log("ExpenseHolder:")
        console.log(expenseHolder)
    }
        
    }
        return (
            <Container className="container">
                <Row>
                  <Header title="Edytuj wydatek"/>  
                </Row>
                <Col md="7" className="box-content base-text text-size form-container">
                    <div className = "href-text center-content">Edytuj wydatek</div>
                    <div className="separator-line"/>
                    <form name="form"
                        method="post"
                        onSubmit={(e)=>handleCreateExpense(e)}
                        >
                        <div className={'form-group'}>
                            <label className="form-label"  htmlFor="Name">Nazwa: </label>
                            <input
                               
                                type="text"
                                className="form-control"
                                name="name"
                                placeholder={currentExpenseName}
                                minLength="1"
                                maxLength="45"
                                onChange={(e)=>handleChangeName(e)}
                            />
                            
                        </div>

                        <div className={'form-group'}>
                            <label className="form-label" htmlFor="Description">Kwota: </label>
                            <input
                                
                                type="number"
                                step="0.01"
                                className="form-control"
                                name="price"
                                placeholder={currentExpensePrice}
                                maxLength="1000"
                                pattern="^\d{0,8}(\.\d{1,2})?$"
                                onChange={(e)=>handleChangePrice(e)}
                            />
                            
                        </div>
                    <div className = "box-subcontent">
                        <div className="base-text text-size">
                        Kategoria:
                
                        </div>
                        
                        {
                         
                         expenseCategories.map(
                             category =>
                            
                             <div key = {category.id} className = "left-content custom-radiobuttons margin-left-text">
                               
                             <label className = "form-label text-size" htmlFor={category.id}>
                               <input type="radio" id={category.id} name="category" value={category.name} 
                                    defaultChecked = {handleDefaultCheck(category.name)}
                                   
                                   onChange={(e)=>readExpenseCategory(e)}
                                   >
                                       
                               </input>
                               
                               <div className="checkmark icons-size-2"></div>
                                {category.name}</label>
                               
                           </div> 
                                
                             
                         )   
             }
                    
                </div>    
                <div className = "box-subcontent">
                        <div className="base-text text-size">
                        Kogo dotyczy wydatek:
                
                        </div>
                        
                        {
                         
                         walletUsers.map(
                            user =>
                            
                             <div key = {user.userId} className = "left-content custom-checkboxes margin-left-text">
                           
                             <label className = "form-label text-size" htmlFor={user.userId}>
                               <input type="checkbox" id={user.userId} name="users" value={user.login} 
                                   defaultChecked={currentExpenseUsersList.includes(user.login)}
                                   
                                   onChange={(e)=>handleCreateExpenseUsersList(e)}
                                   >
                                       
                               </input>
                               
                               <div className="checkmark-checkbox icons-size-2"></div>
                                {user.login}</label>
                               
                           </div> 
                                
                             
                         )   
             }
                    
                </div>    
                <div className = "box-subcontent">
                        <div className="base-text text-size">
                        Zdjęcie paragonu:
                        <div className="center-content">
                                    {message}
                        <div id="img-preview-div" style={{display:'none'}}>
                            <img src={displayImg} className="img-preview"  alt="img" />
                        </div>         
                        

                        </div>        
                        
                        </div>
                        
                        <input  
                            className="btn btn-primary btn-block form-button "
                            id = "insert-photo-button" 
                            type="file"
                            onChange={(e)=>handleImageChange(e)} />
                   <br /><br />
                   <div id="img-preview-div">
                        
                        <button
                        className="delete-user-icon icons-size"
                        onClick={(e)=>handleClearChoosenImg(e)}
                        ></button>
                   </div>
              
                </div>    
                <div className="error-text text-size">
                    {errorMessage}
                </div>
                <br />
                        <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "submit"
                            onClick={e =>  {submitted = true
                                window.location.href='/wallet' 
                            }}
                            >
                            Zapisz zmiany
                        </button>
                </form>
                <br />
                  <button
                            className="btn btn-primary btn-block form-button text-size"
                            id = "mainbuttonstyle"
                            type = "button"
                            onClick={e =>  {
                                window.location.href='/wallet'
                            }}
                            >
                            Anuluj
                        </button>
                </Col>
                
                
                                
            </Container>
        );
    
}

export default EditExpensePage;