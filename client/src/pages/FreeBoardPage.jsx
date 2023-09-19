import React from 'react';
import '../styles/Button.css';
import PostList from '../components/PostList.jsx';
import NavBar from '../components/NavBar.jsx';

const FreeBoardPage = () => {
  return (
  <>
    <div><NavBar /></div>
      <div className='page_container'>
          
      <button className="custom_board_button confirm_button">자유 게시판</button>
          <div className='free_board_container'>
              <PostList type="free"/>
          </div>
      </div>
  </>
  )
}

export default FreeBoardPage;