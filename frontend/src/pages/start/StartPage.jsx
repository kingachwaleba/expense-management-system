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
              <div className = "grid-container">
                <div id="vector-image"></div>
                <div id="piggy-image"></div>
             </div>
          </div>
        );
    }
}

export { StartPage };