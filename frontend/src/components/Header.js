import React from 'react'
import PropTypes from 'prop-types'
import "./components.css"

const Header = ({title, pretitle}) => {
    const keys = [1,2,3,4,5]
    const text = title;
    const newText = text.split('\n').map(str=><h1 key={keys} className="title-text">{str}</h1>);
    return (
        <header key = {keys}>
            <h6 key={keys} className="text-size title-text">{pretitle}</h6>
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