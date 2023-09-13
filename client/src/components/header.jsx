import React, { useState, useEffect } from 'react';
import "../styles/Header.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil, faRightToBracket, faRightFromBracket, faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import PropTypes from 'prop-types';
import Cookies from 'js-cookie';
// import { Link } from 'react-router-dom';

const LogoutHeader = ( {fakeLogin}) => {

  const accessToken = localStorage.getItem('accessToken');

  return (
    <>
      <div className="header_container">
        <div className="header_bar">
          <div className="logo">
            {/* <Link to='/'> */}
            <img src={require("../assets/logo.png")} alt="logo" />
            {/* </Link> */}
          </div>
          <div className="header_icon search disabled">
              {/* <Link to={'/search'}> */}
                <FontAwesomeIcon icon={faMagnifyingGlass} />
              {/* </Link> */}
          </div>
          <div className='header_bar user'>
            <div className="header_icon black disabled">
              {/* <Link to={'/member-id'}> */}
                <img src={require("../assets/user_shadow.png")} className="w70" alt="user profile" />
              {/* </Link> */}
            </div>
            <div className="header_icon black">
              {/* <Link to={'/post/write'}> */}
                <FontAwesomeIcon icon={faPencil} className="header_icon" />
              {/* </Link> */}
            </div>
            <div className="header_icon black">
              {/* <Link to={'/logout'}> */}
                <FontAwesomeIcon icon={faRightToBracket} className="header_icon" onClick={fakeLogin}/>
              {/* </Link> */}
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

LogoutHeader.propTypes = {
  fakeLogin: PropTypes.func.isRequired
}

const LoginHeader = ( {fakeLogout}) => {
  return (
    <>
      <div className="header_container">
        <div className="header_bar">
          <div className="logo">
            {/* <Link to='/'> */}
            <img src={require("../assets/logo.png")} alt="logo" />
            {/* </Link> */}
          </div>
          <div className="header_icon search">
              {/* <Link to={'/search'}> */}
                <FontAwesomeIcon icon={faMagnifyingGlass} />
              {/* </Link> */}
          </div>
          <div className='header_bar user'>
            <div className="header_icon black">
              {/* <Link to={'/member-id'}> */}
                <img src={require("../assets/user_shadow.png")} className="w70" alt="user profile" />
              {/* </Link> */}
            </div>
            <div className="header_icon black">
              {/* <Link to={'/post/write'}> */}
                <FontAwesomeIcon icon={faPencil} className="header_icon" />
              {/* </Link> */}
            </div>
            <div className="header_icon black">
              {/* <Link to={'/logout'}> */}
                <FontAwesomeIcon icon={faRightFromBracket} className="header_icon" onClick={fakeLogout}/>
              {/* </Link> */}
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

LoginHeader.propTypes = {
  fakeLogout: PropTypes.func.isRequired
}

const Header = () => {
  const [ isLoggedIn, setIsLoggedIn ] = useState(false);
  
  const fakeLogin = () => {
    setIsLoggedIn(true);
  }

  const fakeLogout= () => {
    setIsLoggedIn(false);
  }

  // useEffect(() => {
  //   const cookie = Cookies.get('쿠키이름');
  //   console.log(cookie); // 쿠키 확인 후 지우기
  //   if (cookie) {
  //     setIsLoggedIn(true)
  //   } else {
  //     setIsLoggedIn(false);
  //   }
  // }, []);

  return (
    (isLoggedIn) ? <LoginHeader fakeLogout={fakeLogout} /> : <LogoutHeader fakeLogin={fakeLogin}/> 
  )
  
}

export default Header;