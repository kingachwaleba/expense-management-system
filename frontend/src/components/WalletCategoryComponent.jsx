import React, { Component } from 'react';
import WalletCategoryService from '../services/WalletCategoryService';
import '../App.css';
import { timestamp } from 'rxjs';
import WalletDetailService from '../services/WalletDetailService';
import UserService from '../services/user.service';
import { WalletCategory } from '../models/walletCategory';
class WalletCategoryComponent extends Component {

    constructor(props){

        super(props)
        this.state={
            categories: [],
            
           

        }
    }
  
    
    componentDidMount(){
       
        WalletCategoryService.getCategories()
        .then((response)=>{
                this.setState({categories: response.data})  
               
        })
        .catch(error=>{
            console.error({error})      
        });
        console.log(this.props.walletExists)
        
    }


    render() {
        return (
            <div className="box-subcontent">
                <div className="base-text text-size">
                Kategoria:
                
               </div>
              
                     {
                         
                                this.state.categories.map(
                                    category =>
                                   
                                    <div key = {category.id} className = "center-content custom-radiobuttons">
                                  
                                    <label className = "form-label text-size" htmlFor={category.id}>
                                      <input type="radio" id={category.id} name="category" value={category.name} 
                                          defaultChecked = {category.name === Object.values(this.state.categories)[0].name}
                                          
                                          onChange={this.props.readWalletCategory}
                                          >
                                              
                                      </input>
                                      
                                      <div className="checkmark icons-size-2"></div>
                                       {category.name}</label>
                                      
                                  </div> 
                                       
                                    
                                )   
                    }
                
                   
                    
                
            </div>
        );
    }
}

export default WalletCategoryComponent;