import React from 'react';
import { Link } from 'react-router-dom';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil, faRightToBracket } from "@fortawesome/free-solid-svg-icons";

const GuestHeader = ( { isLoggedIn } ) => {

  if (isLoggedIn) {
    return null;
  }
  
  return (
    <header className="header_container">
      <div className="header_bar">
        <Link to='/' className="header_logo">
          <img src={require("../assets/logo.png")} alt="logo" />
        </Link>
        <div className='header_bar_user'>
          <Link to='/login' className="header_icon">
              <FontAwesomeIcon icon={faPencil} className="header_icon" />
          </Link>
          <Link to='/login' className="header_icon">
              <FontAwesomeIcon icon={faRightToBracket} className="header_icon" />
          </Link>
        </div>
      </div>
    </header>
  )
}

GuestHeader.propTypes = {
  isLoggedIn: PropTypes.bool.isRequired,
}

export default GuestHeader;