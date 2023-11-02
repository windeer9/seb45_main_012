import React from 'react';
import '../styles/Button.css';
import '../components/PostList';
import EnvPostList from '../components/EnvPostList';
import NavBar from '../components/NavBar';

const EnvBoardPage: React.FC = () => {
  return (
    <>
      <div>
        <NavBar />
      </div>
      <div className="page_container">
        <div>
          <button className="custom_board_button confirm_button">환경 정보 게시판</button>
          <div className="env_board_container">
            <EnvPostList />
          </div>
        </div>
      </div>
    </>
  );
};

export default EnvBoardPage;
