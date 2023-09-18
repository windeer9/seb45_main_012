import React, { useState, useEffect } from 'react';
import '../styles/Button.css';
import '../styles/MyPageMain.css';
import Pagination from 'components/Pagination.jsx';
import jwtDecode from 'jwt-decode';
import { instance } from 'api/api';
import NavBar from 'components/NavBar.jsx';
import { Link } from 'react-router-dom';

const MyPageMain = () => {

  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const userId = decodedToken.userId;

  const [ userData, setUserData ] = useState([]);

  useEffect(() => {
    async function getUserData() {
      try {
        const res = await instance.get('/post/customer/' + userId);
        const sortedUserData = res.data.sort((a, b) => {
          return new Date(b.createdAt) - new Date(a.createdAt);
        });
        setUserData(sortedUserData);
      } catch (err) {
        console.log('error: ', err);
      }
    }

    getUserData();
  }, []);

  const [ filter, setFilter ] = useState('all');
  const [ currentPage, setCurrentPage ] = useState(1);

  const filteredPosts = userData.filter((post) => {
    if ( filter === 'all') return true;
    if (filter === 'public') return post.open === "true";
    return post.open === "false";
  });

  const handleFilterChange = ( newFilter ) => {
    setFilter(newFilter);
    setCurrentPage(1);
  }

  const viewType = {
    ALL: '전체',
    PUBLIC: '공개',
    PRIVATE: '비공개'
  }

  const [ selectedButton, setSelectedButton ] = useState(viewType.ALL);
  const handleButtonClick = (buttonName) => {
    setSelectedButton(buttonName)
  }
  
  const postsPerPage = 5;
  const startPostIndex = ( currentPage - 1 ) * postsPerPage;
  const endPostIndex = Math.min(startPostIndex + postsPerPage);
  const currentPosts = filteredPosts.slice(startPostIndex, endPostIndex);

  return (
    <>
    <NavBar />
      <main className="page_container">
        <h2 className="my_posts">내가 쓴 글</h2>
        <ul>
          <li>
            <button
              className={`custom_button ${selectedButton === viewType.ALL ? 'active' : ''}`}
              onClick={() => {
                handleButtonClick(viewType.ALL);
                handleFilterChange('all');
              }}
              >
              {viewType.ALL}
            </button>
          </li>
          <li>
            <button
              className={`custom_button ${selectedButton === viewType.PUBLIC ? 'active' : ''}`}
              onClick={() => {
                handleButtonClick(viewType.PUBLIC);
                handleFilterChange('public');
              }}
              >
              {viewType.PUBLIC}
            </button>
          </li>
          <li>
            <button
              className={`custom_button ${selectedButton === viewType.PRIVATE ? 'active' : ''}`}
              onClick={() => {
                handleButtonClick(viewType.PRIVATE)
                handleFilterChange('private');
              }}
              >
              {viewType.PRIVATE}
            </button>
          </li>
        </ul>
        <section className='posts_container'>
          {currentPosts.map((post) => (
            <article className="post" key={post.postId}>
              <div className='post_info'>
                <div className="post_title">
                  <Link to={`/mypage/${userId}/posts/${post.postId}`}>
                    {post.title}
                  </Link>
                </div>
                <div className='post_date'>{
                  new Date(post.createdAt).toLocaleDateString()
                }</div>
              </div>
              <p className='post_content'>{ post.body.length >= 50 ? post.body.slice(0, 50) + '...' : post.body}</p>
            </article>
          ))}
          <Pagination
            posts={filteredPosts}
            postsPerPage={postsPerPage}
            pagesPerGroup={5}
            currentPage={currentPage}
            setCurrentPage={setCurrentPage}
          />
        </section>
      </main>
    </>
  )
}

export default MyPageMain;