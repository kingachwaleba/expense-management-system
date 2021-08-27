import React from 'react';

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
            <div>Profile Page



            </div>
        );
    }
}

export { ProfilePage };