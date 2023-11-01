import React, { useState, useEffect, useRef } from 'react';
import '../styles/PostList.css';
import { Link } from 'react-router-dom';
import { getAllPosts, getAlltypePosts } from 'api/api.js';
import PropTypes from 'prop-types';
import { Post, PostListProps } from '../types/types';

const PostList: React.FC<PostListProps> = props => {
  const [allPosts, setAllPosts] = useState<Post[]>([]);
  const [visiblePosts, setVisiblePosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  const intersectionRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    if (props.type === 'all') {
      getAllPosts() //모든 게시글 가져오기
        .then(res => {
          const sortedData = res.data.sort((a: { createdAt: string }, b: { createdAt: string }) => {
            return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
          });
          setAllPosts(sortedData);
          setVisiblePosts(sortedData.slice(0, 10));
        })
        .catch(error => console.error('Error:', error));
    } else {
      getAlltypePosts(props.type) //게시판 별 게시글 가져오기
        .then(res => {
          const sortedData = res.data.sort((a: { createdAt: string }, b: { createdAt: string }) => {
            return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
          });
          setAllPosts(sortedData);
          setVisiblePosts(sortedData.slice(0, 10));
        })
        .catch(error => console.error('Error:', error));
    }
  }, [props.type]);

  useEffect(() => {
    const handleIntersect = (entries: IntersectionObserverEntry[]) => {
      if (entries[0].isIntersecting) {
        setLoading(true);
        setTimeout(() => {
          const endVisibleIndex = visiblePosts.length;
          const newVisiblePosts = [...visiblePosts, ...allPosts.slice(endVisibleIndex, endVisibleIndex + 10)];
          setVisiblePosts(newVisiblePosts);
          setLoading(false);
        }, 0);
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
      {visiblePosts.map((post: Post) => (
        <div className="post_item" key={post.postId}>
          <Link
            to={post.type === 'free' ? `/free/${post.postId}/${post.userId}` : `/auth/${post.postId}/${post.userId}`}>
            <div className="post_header">
              <div className="post_title">{post.title}</div>
              <div className="post_date">{new Date(post.createdAt).toLocaleDateString()}</div>
            </div>
            <div className="post_content">{post.body.length > 90 ? `${post.body.slice(0, 90)}...` : post.body}</div>
          </Link>
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
