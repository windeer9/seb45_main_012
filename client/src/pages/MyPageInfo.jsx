import React, { useState } from 'react';
import NavBar from '../components/NavBar.jsx';
import '../styles/Button.css';
import '../styles/MyPageInfo.css';
import jwtDecode from 'jwt-decode';

const MyPageInfo = () => {
  const [previewImageUrl, setPreviewImageUrl] = useState('');
  const [selectedImage, setSelectedImage] = useState(null);

  // 아이디와 비밀번호를 정의합니다.
  // 'mini', 'mini!1234'

  //로그인 시 localStorge에 저장했던 memberId를 받아옵니다.
  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const userId = decodedToken.userId;
  console.log(userId);

  const handleImageChange = (e) => {
    const file = e.target.files[0];

    if (file) {
      const reader = new FileReader();

      reader.onload = (event) => {
        const imageUrl = event.target.result;
        setPreviewImageUrl(imageUrl);
        setSelectedImage(file);
      };

      reader.readAsDataURL(file);
    }
  };
  const handleButtonClick = () => {
    alert('서비스 준비중입니다.');
  }

  return (
    <>
      <div><NavBar /></div>
      <div className='page_container'>
        <h4>내 정보</h4>
        <div className='my_custom_container'>

          <div className="circle_container">
            <input
              type="file"
              id="imageInput"
              className="circle_input"
              accept="image/*"
              onChange={handleImageChange}
            />
            <div></div>
            <img
              src={previewImageUrl}
              alt=""
              className="circle_image"
            />
          </div>

          <div className="input_container">
            <input
              type="text"
              id="nicknameInput"
              className="nickname_input"
              placeholder="닉네임"
            />
          </div>
          <button
          className='custom_mypage_button confirm_button'
          onClick={handleButtonClick}
          >
            수정
          </button>
        </div>

        <div className='my_custom_container id_container'>
        <label htmlFor="idInput" className="custom_label_id">아이디</label>
          <div className='input_container'>
            <input
              type="text"
              id="idInput"
              className="nickname_input"
              placeholder="아이디"
              />
            </div>
            <button
            className='custom_mypage_button confirm_button'
            onClick={handleButtonClick}
            >
            수정
            </button>
        
          </div>
      
          <div className='my_custom_container'>
        <label htmlFor="pwInput" className="custom_label_pw">비밀번호</label>
          <button
          className='custom_mypage_button confirm_button'
          onClick={handleButtonClick}
          >
            변경 페이지로 이동
          </button>        
        </div>
        
      </div>
    </>
  );
  
};

export default MyPageInfo;
