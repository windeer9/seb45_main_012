import React, { useState, useEffect } from 'react';
import '../styles/MyPost.css';
import { instance } from 'api/api.js';
import jwtDecode from 'jwt-decode';
import NavBar from 'components/NavBar.jsx';
import { useParams , Link } from 'react-router-dom';

const MyPost = ( ) => {
  
  const { postId } = useParams();
  const [ post, setPost ] = useState({});
  const [ userData, setUserData ] = useState({});

  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const userId = decodedToken.userId;
  const userName = decodedToken.userName;

  useEffect(() => {
    async function getPost() {
      try {
        const res = await instance.get('/post/' + postId);
        setPost(res.data);
      }
      catch (err) {
        console.log('err: ', err);
      }
    }

    getPost();
    
  }, [postId]);


  useEffect(() => {
    async function getUserData() {
      try {
        console.log(userId);
        const res = await instance.get('/user/' + userId);
        console.log(res.data);
        setUserData(res.data);
      } catch (err) {
        console.error('getUserData err: ', err);
      }
    }

    getUserData();

  }, [userId]);

  const date = new Date(post.createdAt);
  const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
  const dayOfWeek = daysOfWeek[date.getDay()] + "요일";
  const displayDate = date.toLocaleDateString();

  const userPicture = userData.imageUrl || require("../assets/user_shadow.png");
  console.log(userPicture);


  return (
    <>
      <NavBar />
      <main className="post_container">
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
        <article className="each_post">
          <div className="buttons">
            <Link to={`/mypage/posts/${postId}/edit`} aria-label="edit page">
              <button className='edit_button'>수정</button>
            </Link>
            <span>|</span>
            <button className='delete_button'>삭제</button>
          </div>
          <div className='each_post_info'>
            <h2>{post.title}</h2>
            <div className='each_post_date'>{
              `${displayDate} ${dayOfWeek}`
            }
            </div>
          </div>
          <div className='user_info'>
            <img
              className='profile_image' 
              src={userPicture}
              alt="profile" />
            <div className='user_name'>{userName}</div>
          </div>
          <div className='post_content'>
            {post.imageUrls && (
              <img className="uploaded" src={post.imageUrls} alt="uploded_image" />
            )}
            <p className={`post_writing ${!post.imageUrls ? 'no_image' : ''})`}>
              {post.body}
            </p>
          </div>
        </article>
      </main>
    </>
  )
}

export default MyPost;