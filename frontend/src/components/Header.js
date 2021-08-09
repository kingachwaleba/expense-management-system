import React from 'react'
import PropTypes from 'prop-types'
import "./components.css"

const Header = ({title, pretitle}) => {
    const text = title;
    const newText = text.split('\n').map(str=><h1 className="title-text">{str}</h1>);
    return (
        <header>
            <h6 className="text-size title-text">{pretitle}</h6>
            {newText}
        </header>
        
        )
}
Header.defaultProps={
    title:"Main text",
    pretitle: "",
    
}
Header.propTypes = {
    title: PropTypes.string.isRequired,
    pretitle: PropTypes.string.isRequired,
}


export default Header