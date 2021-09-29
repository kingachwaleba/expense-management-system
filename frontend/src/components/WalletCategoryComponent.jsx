import React, { Component } from 'react';
import WalletCategoryService from '../services/WalletCategoryService';
import '../App.css';
class WalletCategoryComponent extends Component {

    constructor(props){

        super(props)
        this.state={
            categories: [],
            

        }
    }
    
    
    componentDidMount(){
        WalletCategoryService.getCategories().then((response)=>{
                this.setState({categories: response.data})

        } 
        )
    }


    render() {
        return (
            <div className="box-subcontent">

                <h2>Kategoria:</h2>
               
                     {
                         
                                this.state.categories.map(
                                    category =>
                                    <div key = {category.id} className = "center-content custom-radiobuttons">
                                      <label className = "form-label" htmlFor={category.id}>
                                        <input type="radio" id={category.id} name  ="category" value={category.name} 
                                           
                                         
                                            onChange={this.props.readWalletCategory}
                                            >
                                                
                                        </input>
                                        
                                        <div className="checkmark"></div>
                                         {category.name} </label>
                                        
                                    </div>
                                    
                                )   
                    }
                
                    
                    
                
            </div>
        );
    }
}

export default WalletCategoryComponent;