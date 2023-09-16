import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
// import SignUpPage from './pages/SignUpPage.jsx';
import AllBoardPage from './pages/AllBoardPage.jsx';
import FreeBoardPage from './pages/FreeBoardPage.jsx';
import FreeDetailPage from './pages/FreeDetailPage.jsx';
<<<<<<< HEAD
import Header from './components/header.jsx';
import NavBar from './components/NavBar.jsx';
import MyPageMain from './pages/MyPageMain.jsx';
import { PostEditerWithImage, PostEditer } from './components/PostEditer.jsx';
import EditerPage from './pages/EditerPage.jsx';
=======
// import Header from './components/header.jsx';
// import NavBar from './components/NavBar.jsx';
import MyPageMain from 'pages/MyPageMain.jsx';
>>>>>>> 1f4f1d752f80030d702dbcf42df97956a968ab46

function App() {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<AllBoardPage/>} />
          <Route path="/free" element={<FreeBoardPage/>} />
          <Route path="/post/:postId/:userId" element={<FreeDetailPage />} />
          <Route path="/mypage/main" element={<MyPageMain />} />
        </Routes>
    </Router>
      {/* <FreeDetailPage/> */}
      {/* <SignUpPage /> */}
      {/* <Header /> */}
      {/* <NavBar /> */}
      {/* <FreeBoardPage/> */}
      {/* <MyPageMain /> */}
      {/* <EditerPage /> */}
    </div>
  );
}
import { formToJSON } from 'axios';

export default App;