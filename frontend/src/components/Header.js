import React from 'react'
import PropTypes from 'prop-types'

const Header = ({title, pretitle}) => {
    const text = title;
    const newText = text.split('\n').map(str=><h1>{str}</h1>);
    return (
        <header>
            <h6>{pretitle}</h6>
            <h1 id = 'idtitle'>{newText}</h1>
        </header>
        
        )
}
Header.defaultProps={
    title:"Main text",
    pretitle: "Small text above header",
    
}
Header.propTypes = {
    title: PropTypes.string.isRequired,
    pretitle: PropTypes.string.isRequired,
}


export default Header