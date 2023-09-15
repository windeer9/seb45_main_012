import React, { useState, useEffect } from 'react';
import '../styles/MyPost.css';
import { instance } from 'api/api';
import jwtDecode from 'jwt-decode';

const MyPost = ( ) => {
  
  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const memberId = decodedToken.memberId;

  // const [ post, setPost ] = useState({});

  // useEffect(() => {
  //   async function getPost() {
  //     try {
  //       // const res = await instance.get('/post' + postId);
  //       const res = await instance.get('/post/');
  //       setPost(res.data);
  //       console.log(post)
  //     }
  //     catch ( err ) {
  //       console.log('err: ', err);
  //     }
  //   }

  //   getPost();
  // }, []
  
  // 유저 정보 가져오는 Post로 username 가져오기

  const post = {
    "postId": 8,
    "userId": 10,
    "propfileImage": "https://greenearthforus.s3.ap-northeast-2.amazonaws.com/images/8e97553d-289e-4dc3-95e8-62a669df3b0b-KakaoTalk_20230324_180814091.jpg",
    "username": "charlie",
    "type": "free",
    "title": "open",
    "body": "helloWorld",
    "open": "true",
    "imageUrls": [
      "https://greenearthforus.s3.ap-northeast-2.amazonaws.com/images/65f4fa9c-5041-4380-b0d2-483e14c115d5-KakaoTalk_20211203_211319669.jpg"
    ],
    "createdAt": "2023-09-13T08:31:14"
  }

  const date = new Date(post.createdAt);
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const dayOfWeek = daysOfWeek[date.getDay()];

  return (
    <main className="container">
      <h2 className="my_post">내가 쓴 글</h2>
      <ul>
        <li>
          <div className={`openOrNot ${post.open === "true" ? 'active' : ''}`}>
            공개
          </div>
        </li>
        <li>
          <div className={`openOrNot ${post.open === "false" ? 'active' : ''}`}>
            비공개
          </div>
        </li>
      </ul>
      <article className="post">
        <div className='post_info'>
          <h2 className="post_title">{post.title}</h2>
          <div className='post_date'>{
            date
              .toISOString()
              .split("T")[0]
              .replace(/-/g, '.')
            }
            {` ${dayOfWeek}요일`}
          </div>
        </div>
        <div className='user_info'>
          <img
            className='profile_image'
            src={post.propfileImage}
            alt="profile" />
          <div className='username'>{post.username}</div>
        </div>
        <div className='post_content'>
          <img className="uploaded" src={post.imageUrls} alt="uploded_image" />
          <p className='post_writing'>{post.body}</p>
        </div>
      </article>
    </main>
  )
}

export default MyPost;