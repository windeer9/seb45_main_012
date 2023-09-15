import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import SignUpPage from './pages/SignUpPage.jsx';
import AllBoardPage from './pages/AllBoardPage.jsx';
import FreeBoardPage from './pages/FreeBoardPage.jsx';
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
      <Router>
        <Routes>
          <Route path="/" element={<AllBoardPage/>} />
          <Route path="/free" element={<FreeBoardPage/>} />
          <Route path="/post/:postId/:userId" element={<FreeDetailPage />} />
        </Routes>
    </Router>
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