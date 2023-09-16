import React, { useState, useEffect } from 'react';
import '../styles/Button.css';
import '../styles/MyPageMain.css';
import Pagination from 'components/Pagination.jsx';
import jwtDecode from 'jwt-decode';
import { instance } from 'api/api';

const MyPageMain = () => {
  const accessToken = localStorage.getItem('accessToken');
  const decodedToken = jwtDecode(accessToken);
  const memberId = decodedToken.memberId;

  const [ userData, setUserData ] = useState([]);

  useEffect(() => {
    async function getUserData() {
      try {
        const res = await instance.get('/post/customer/' + memberId);
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
    <main className="container">
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
            className={`custom_button ${selectedButton === viewType.CLOSE ? 'active' : ''}`}
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
          <article className="post" key={post.id}>
            <div className='post_info'>
              <h4 className="post_title">{post.title}</h4>
              <div className='post_date'>{
                new Date(post.createdAt).toISOString().split("T")[0].replace(/-/g, '.')
              }</div>
            </div>
            <div className='post_content'>{post.body.slice(0, 50) + " ..."}</div>
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
  )
}


export default MyPageMain;