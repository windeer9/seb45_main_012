import React from 'react';
import '../styles/Button.css';
import PostList from '../components/PostList.jsx';
import NavBar from '../components/NavBar.jsx';

const AllBoardPage = () => {
  return (
  <>
    <div><NavBar /></div>
      <div className='page_container'>
          
      <button className="custom_board_button confirm_button">전체 게시판</button>
          <div className='free_board_container'>
              <PostList type="all"/>
          </div>
      </div>
  </>
  )
}

export default AllBoardPage;