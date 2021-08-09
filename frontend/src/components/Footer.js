import React from "react";
import { MDBCol, MDBContainer, MDBRow, MDBFooter } from "mdbreact";
import "./components.css"
const FooterPage = () => {
  return (
    <MDBFooter color="blue" className="font-small pt-4 mt-4">
      
      <div className="footer footer-copyright text-center py-3">
        <MDBContainer fluid>
          &copy; {new Date().getFullYear()} Copyright: Choma Chwaleba Chwil
        </MDBContainer>
      </div>
    </MDBFooter>
  );
}

export default FooterPage;

