import React, { useState, useEffect } from 'react';
import "../styles/Header.css";
import { Link, useNavigate } from 'react-router-dom';
import jwtDecode from 'jwt-decode';
import PropTypes from 'prop-types';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil, faRightToBracket, faRightFromBracket, faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";


const HeaderLoggedOut = ( {isLoggedIn} ) => {

  return (
    isLoggedIn && (
      <header className="header_container">
        <div className="header_bar">
          <Link to='/' className="logout_logo">
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
  )
}

HeaderLoggedOut.propTypes = {
  isLoggedIn: PropTypes.bool.isRequired
}

const HeaderLoggedIn = ( {isLoggedIn} ) => {

  // const accessToken = localStorage.getItem('accessToken');
  // const decodedToken = jwtDecode(accessToken);
  // const username = decodedToken.username;
  const username = 'charlie';

  const navigate = useNavigate();

  const [ isLoggedOut, setIsLoggedOut ] = useState(false);
  
  const handleLogout = () => {
    try {
      localStorage.removeItem('accessToken');
      setIsLoggedOut(true);
      navigate('/');
    } catch (error) {
      console.error('로그아웃 실패: ', error);
    }
  };

  useEffect(() => {
    if (isLoggedOut) {
      console.log('로그아웃 성공');
    }
  }, [isLoggedOut]);
  
  return (
    isLoggedIn && (
      <header className="header_container">
        <div className="header_bar">
          <Link to='/' className="logo">
            <img src={require("../assets/logo.png")} alt="logo" />
          </Link>
          <div className="search">
            <FontAwesomeIcon icon={faMagnifyingGlass} />
          </div>
          <div className='header_bar_user'>
            <Link to='/mypage/posts' className="profile">
              <img className="w55 user_info" src={require("../assets/user_shadow.png")} alt="user profile" />
              <span className="user_info">{username} 님</span>
            </Link>
            <Link to={'/posts/write'}>
              <FontAwesomeIcon icon={faPencil} className="header_icon" />
            </Link>
            <div className="header_icon">
              <FontAwesomeIcon icon={faRightFromBracket} className="header_icon" onClick={handleLogout} />
            </div>
          </div>
        </div>
      </header>
    )
  )
}

HeaderLoggedIn.propTypes = {
  isLoggedIn: PropTypes.bool.isRequired
}

const Header = () => {

  const accessToken = localStorage.getItem('accessToken')

  const [ isLoggedIn, setIsLoggedIn] = useState(!accessToken);

  useEffect(() => {
    if (accessToken) {
      setIsLoggedIn(true)
    } else {
      setIsLoggedIn(false);
    }
  }, [accessToken]);

  return (
    (isLoggedIn)
      ? <HeaderLoggedIn isLoggedIn={isLoggedIn} />
      : <HeaderLoggedOut isLoggedIn={isLoggedIn} /> 
  )
  // const fakeLogin = () => {
  //   localStorage.setItem('aT', 1);
  //   const aT = localStorage.getItem('aT');
  //   setIsLoggedIn(true);
  //   console.log(`aT: ${aT}입니다. 로그인 되었습니다!`);

  // }

  // const fakeLogout = () => {
  //   localStorage.removeItem('aT');
  //   setIsLoggedIn(false)
  //   console.log('aT가 로컬 스토리지에서 삭제되었습니다. 로그아웃 되었습니다.')
  // }
  }


export default Header;



