import React from 'react';
import Header from '../../components/Header';
class StartPage extends React.Component {

    constructor(props, context) {
        super(props, context);
    }

    render() {
        return (
          <div>
              <Header title={'Kontrolowanie \nTwoich wydatków nigdy \nnie było tak \nproste'} pretitle='Załóż darmowe konto'/>
              <img src="../../logo.svg" alt="logopig" height="100" width="100" ></img>
          </div>
        );
    }
}

export { StartPage };