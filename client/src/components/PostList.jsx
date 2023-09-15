import React, { useState, useEffect, useRef } from 'react';
import '../styles/PostList.css';
import { Link } from 'react-router-dom';
import { getAllPosts, getAlltypePosts } from '../api/api.js';
import PropTypes from 'prop-types';

const PostList = (props) => {
  const [allPosts, setAllPosts] = useState([]);
  const [visiblePosts, setVisiblePosts] = useState([]);
  const [loading, setLoading] = useState(false);

  const intersectionRef = useRef(null);

  useEffect(() => {
    if (props.type === 'all') {
      getAllPosts()
        .then((res) => {
          const sortedData = res.data.sort((a, b) => {
            return new Date(b.createdAt) - new Date(a.createdAt);
          });
          setAllPosts(sortedData);
          setVisiblePosts(sortedData.slice(0, 10));
        })
        .catch((error) => console.error('Error:', error));
    } else {
      getAlltypePosts(props.type)
        .then((res) => {
          const sortedData = res.data.sort((a, b) => {
            return new Date(b.createdAt) - new Date(a.createdAt);
          });
          setAllPosts(sortedData);
          setVisiblePosts(sortedData.slice(0, 10));
        })
        .catch((error) => console.error('Error:', error));
    }
  }, [props.type]);


  useEffect(() => {
    const handleIntersect = (entries) => {
      if (entries[0].isIntersecting) {
        setLoading(true);
        setTimeout(() => {
          const endVisibleIndex = visiblePosts.length;
          const newVisiblePosts = [...visiblePosts, ...allPosts.slice(endVisibleIndex, endVisibleIndex + 10)];
          setVisiblePosts(newVisiblePosts);
          setLoading(false);
        }, );
      }
    };
  
    const observer = new IntersectionObserver(handleIntersect, {
      root: null,
      rootMargin: '0px',
      threshold: 0.1,
    });
  
    if (intersectionRef.current) {
      observer.observe(intersectionRef.current);
    }
  
    return () => {
      observer.disconnect();
    };
  }, [allPosts, visiblePosts]);
  
  return (
    <div className="post_list_container">
      {visiblePosts.map((post) => (
        <div className="post_item" key={post.postId}>
          <div className="post_header">
            <Link to={`/post/${post.postId}/${post.userId}`} className="post_title">{post.title}</Link>
            <div className="post_date">
              {new Date(post.createdAt).toLocaleDateString()}
            </div>
          </div>
          <div className="post_content">{post.body}</div>
        </div>
      ))}
      {loading && <div>Loading...</div>}
      <div ref={intersectionRef}></div>
    </div>
  );
};

PostList.propTypes = {
  type: PropTypes.string.isRequired,
};

export default PostList;
