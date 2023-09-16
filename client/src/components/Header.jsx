import React, { useState, useEffect } from 'react';
import "../styles/Header.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPencil, faRightToBracket, faRightFromBracket, faMagnifyingGlass } from "@fortawesome/free-solid-svg-icons";
import PropTypes from 'prop-types';
import Cookies from 'js-cookie';
import { Link, useNavigate } from 'react-router-dom';
import jwtDecode from 'jwt-decode';


const HeaderLoggedOut = () => {

  return (
    <>
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
    </>
  )
}

const HeaderLoggedIn = () => {

  // const accessToken = localStorage.getItem('accessToken');
  // const decodedToken = jwtDecode(accessToken);
  // const username = decodedToken.username;
  const username = 'charlie';

  const navigate = useNavigate();

  const [ isLoggedOut, setIsLoggedOut ] = useState(false);
  
  const handleLogout = () => {
    try {
      // localStorage.removeItem('accessToken');
      setIsLoggedOut(true);
      // navigate('/');
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
            <img className="w55" src={require("../assets/user_shadow.png")} alt="user profile" />
            {username} 님
          </Link>
          <Link to={'/posts/write'}>
            <FontAwesomeIcon icon={faPencil} className="header_icon" />
          </Link>
        </div>
        <div className="header_icon">
          <FontAwesomeIcon icon={faRightFromBracket} className="header_icon" onClick={handleLogout}/>
        </div>
      </div>
    </header>
  )
}

const Header = () => {


  // useEffect(() => {
  //   const cookie = Cookies.get('쿠키이름');
  //   console.log(cookie); // 쿠키 확인 후 지우기
  //   if (cookie) {
  //     setIsLoggedIn(true)
  //   } else {
  //     setIsLoggedIn(false);
  //   }
  // }, []);

  // return (
  //   (isLoggedIn) ? <LoginHeader fakeLogout={fakeLogout} /> : <LogoutHeader fakeLogin={fakeLogin}/> 
  // )
  
}

export default Header;





// LogoutHeader.propTypes = {
//   fakeLogin: PropTypes.func.isRequired
// }

// LoginHeader.propTypes = {
//   fakeLogout: PropTypes.func.isRequired
// }