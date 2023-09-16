import React, { useState } from 'react';
import LoginFunc from '../auth/LoginFunc.js';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Login.css';

const LogIn = () => {
    
  const navigate = useNavigate();

  const [ id, setId ] = useState("");
  const [ password, setPassword ] = useState("");

  const emailHandler = (e) => {
    setId(e.target.value);
  }

  const passwordHandler = (e) => {
    setPassword(e.target.value);
  }

  const submitHandler = async (e) => {
    e.preventDefault();
    try {
      const result = await LoginFunc(id, password);
      if (result) {
        navigate('/');
      }
    }
    catch (err) {
      console.error()
    }
  }

  return (
    <div >  
      <div className='container'>  
        <div className='logo_container'>
          <img className="logo_leaf" src={require('../assets/logo_only_image.png')} alt='logo'/>
        </div>
        <div className='form_container'>
          <form className='login_form' onSubmit={submitHandler}>
              <div className='id'>
                  <label htmlFor="id">ID</label>
                  <input id="id" type="text" onChange={emailHandler}></input>
              </div>
              <div className='password'>
                  <label htmlFor="password">Password</label>
                  <input id="password" type="password" onChange={passwordHandler} />
              </div>
              <div className='submit_button'>
                  <input className='submit' type="submit" value="로그인" />
              </div>
          </form>
        </div>
        <div className='links'>
          <div>비밀번호 찾기 </div>
          <div className='center'> | </div>
          <Link to={'/signup'}>회원가입</Link>
        </div>
      </div>
    </div>
  )
}

export default LogIn;