import React, { useState, useEffect } from 'react';
import '../styles/EnvPostList.css';
import { getEnvPosts } from '../api/api.js';

const EnvPostList = () => {
  const [allEnvPosts, setAllEnvPosts] = useState([]);
  const [visibleEnvPosts, setVisibleEnvPosts] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 6;
  const maxPagesToShow = 5;

  useEffect(() => {
    getEnvPosts()
      .then((res)=> {
        const sortedData = res.data.sort((a, b) => {
          return new Date(b.createdAt) - new Date(a.createdAt);
        });
        setAllEnvPosts(sortedData);
        setVisibleEnvPosts(sortedData.slice(0, 6));
      })
      .catch((error) => console.error('Error:', error));
  }, []);

  useEffect(() => {
    updateVisiblePosts(currentPage, allEnvPosts);
  }, [currentPage, allEnvPosts]);

  const updateVisiblePosts = (page, data) => {
    const startIndex = (page - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const pageData = data.slice(startIndex, endIndex);
    setVisibleEnvPosts(pageData);
  };

  const handlePageClick = (pageNumber) => {
    setCurrentPage(pageNumber);
  };

  const getPageNumbers = () => {
    const totalPages = Math.ceil(allEnvPosts.length / itemsPerPage);
    const halfMaxPages = Math.floor(maxPagesToShow / 2);
    let startPage, endPage;

    if (totalPages <= maxPagesToShow) {
      startPage = 1;
      endPage = totalPages;
    } else {
      if (currentPage <= halfMaxPages) {
        startPage = 1;
        endPage = maxPagesToShow;
      } else if (currentPage + halfMaxPages >= totalPages) {
        startPage = totalPages - maxPagesToShow + 1;
        endPage = totalPages;
      } else {
        startPage = currentPage - halfMaxPages;
        endPage = currentPage + halfMaxPages;
      }
    }

    return Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index);
  };

  const isPrevButtonDisabled = currentPage === 1;

  const isNextButtonDisabled = currentPage === Math.ceil(allEnvPosts.length / itemsPerPage);

  return (
    <>
      <div className="env_post_grid">
        {visibleEnvPosts.map((post, index) => (
          <div className="env_post_item_container" key={post.postId}>
            <img src={post.imageUrls} alt={`${post.postId}`} />
          </div>
        ))}
      </div>
      <div className="pagination">
        {!isPrevButtonDisabled && visibleEnvPosts.length > 0 && <button onClick={() => handlePageClick(1)}>&lt;&lt;</button>}
        {!isPrevButtonDisabled && visibleEnvPosts.length > 0 && <button onClick={() => handlePageClick(currentPage - 1)}>&lt;</button>}
        {getPageNumbers().map((pageNumber) => (
          <button
            key={pageNumber}
            onClick={() => handlePageClick(pageNumber)}
            className={currentPage === pageNumber ? 'active' : ''}
          >
            {pageNumber}
          </button>
        ))}
        {!isNextButtonDisabled && visibleEnvPosts.length > 0 && <button onClick={() => handlePageClick(currentPage + 1)}>&gt;</button>}
        {!isNextButtonDisabled && visibleEnvPosts.length > 0 && (
          <button onClick={() => handlePageClick(Math.ceil(allEnvPosts.length / itemsPerPage))}>
            &gt;&gt;
          </button>
        )}
      </div>
    </>
  );
};

export default EnvPostList;
