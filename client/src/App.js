import React from 'react';
import './App.css';
import FreeBoardPage from './pages/FreeBoardPage.jsx';
import SignUpPage from './pages/SignUpPage.jsx';
import FreeDetailPage from './pages/FreeDetailPage.jsx';
import Header from './components/header.jsx';
import NavBar from './components/NavBar.jsx';
import MyPageMain from './pages/MyPageMain.jsx';
import { PostEditerWithImage, PostEditer } from './components/PostEditer.jsx';
import EditerPage from './pages/EditerPage.jsx';
import { ReEditFree, ReEditAuth } from './components/ReEdit.jsx';

function App() {
  return (
    <div className="App">
      {/* <FreeDetailPage/> */}
      {/* <SignUpPage /> */}
      {/* <Header /> */}
      {/* <NavBar /> */}
      {/* <FreeBoardPage/> */}
      {/* <MyPageMain /> */}
      {/* <EditerPage /> */}
      <ReEditFree />
    </div>
  );
}
import { formToJSON } from 'axios';

export default App;
