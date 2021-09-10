import React from 'react';
import Header from '../../components/Header';
import ProfileDataComponent from '../../components/ProfileDataComponent';
import NotificationComponent from '../../components/NotificationComponent';



class ProfilePage extends React.Component {

    constructor(props, context) {
        super(props, context);
    }
    state = {
        data: []
    }

    componentDidMount(){
        fetch('http://localhost:8080/account')
        .then(response => response.json())
        .then(data => {
            console.log(data);
            this.setState({data})
        });

    }

    render() {
        return (
            <div id='container'>
               
                <Header title="Profil"/>
                <div className="center-content">
                    <ProfileDataComponent />
                
                    <div className="box-content">
                        <div className="box-subcontent"> 
                        Powiadomienia
                        <NotificationComponent />
                        <NotificationComponent />
                        <NotificationComponent />
                        </div>

                    </div>
                </div>


                
                <div className="box-content center-content">
                    
                <a href="/statutes" className="card-link  href-text">Przejdź do regulaminu</a>
                </div>
                

            </div>
        );
    }
}

export { ProfilePage };