import React from 'react';
import Header from '../../components/Header';
import ProfileDataComponent from '../../components/ProfileDataComponent';
import NotificationComponent from '../../components/NotificationComponent';
import UserService from '../../services/user.service';
import { Container, Row, Col } from 'react-bootstrap';



function ProfilePage () {
   
const userToken = UserService.getCurrentUser().token;
    
        return (
            <Container >
               <Row>
                    <Header title="Profil"/>
               </Row>
                <Col md={{span: 8, offset:2 }}  className="center-content"> 

                <Row>
                    <div className="center-content">
                        <ProfileDataComponent />
                        <div className="box-subcontent base-text text-size"> 
                            Powiadomienia
                            <div className="separator-line"></div>
                            <NotificationComponent />
                          
                        </div>
                    </div>
                    <br/>
                </Row>
                    <br/>
                <Row>
                    <div className="box-content center-content">
                        <a href="/statutes" className="card-link  href-text text-size">Przejdź do regulaminu</a>
                    </div>
                </Row>
                </Col>
            </Container>
        );
    
}

export { ProfilePage };