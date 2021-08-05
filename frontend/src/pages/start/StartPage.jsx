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
          </div>
        );
    }
}

export { StartPage };