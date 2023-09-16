import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/Button.css';
import '../styles/FreeDetailPage.css';
import NavBar from '../components/NavBar.jsx';
import { getPost, getUser, getComment, postComment } from '../api/api.js';

const FreeDetailPage = () => {
  const { postId, userId } = useParams();

  const [post, setPost] = useState({});
  const [user, setUser] = useState({});
  const [vote, setVote] = useState({});
  const [commentText, setCommentText] = useState('');

  const [allComments, setAllComments] = useState([]);
  const [visibleComments, setVisibleComments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const intersectionRef = useRef(null);

  const handleCommentTextChange = (event) => {
    setCommentText(event.target.value);
  };

  const handleSubmitComment = () => {
    if (commentText.trim() === '') {
      return;
    }
    if (commentText.length > 500) {
      alert('댓글은 500자 이내로 작성해주세요.');
      return;
    }

    console.log('댓글 내용:', commentText);

    postComment(postId, userId, commentText)
      .then((response) => {
        console.log('댓글 작성 완료:', response.data);
        window.location.reload();
      })
      .catch((error) => {
        console.error('댓글 작성 오류:', error);
      });
  };

  useEffect(() => {
    // 포스트 데이터 가져오기
    getPost(postId)
      .then((response) => {
        setPost(response.data);
      })
      .catch((error) => {
        console.error('포스트 데이터 가져오기 오류:', error);
      });

    // 유저 데이터 가져오기
    getUser(userId)
      .then((response) => {
        console.log(response.data);
        setUser(response.data);
      })
      .catch((error) => {
        console.error('유저 데이터 가져오기 오류:', error);
      });

    // 투표 데이터 가져오기
    // let voteId; // 변수를 함수 범위로 이동

    // postVote(postId)
    //   .then((response) => {
    //     const voteData = response.data;
    //     console.log("voteData: ", voteData);
    //     voteId = voteData.voteId; // voteId를 할당
    //     // 이제 voteId를 사용하여 getVote 함수 호출
    //     return getVote(postId, voteId);
    //   })
    //   .then((response) => {
    //     const voteData = response.data;
    //     // const voteCount = voteData.voteCount;
    //     setVote(voteData);
    //   })
    //   .catch((error) => {
    //     console.error('투표 생성 또는 데이터 가져오기 오류:', error);
    //   });


    // 댓글 데이터 가져오기
    getComment(postId, userId)
      .then((response) => {
        const sortedComments = response.data.sort((a, b) => {
          return new Date(b.createdAt) - new Date(a.createdAt);
        });
        setAllComments(sortedComments);
        setVisibleComments(sortedComments.slice(0, 10));
      })
      .catch((error) => {
        console.error('댓글 데이터 가져오기 오류:', error);
      });
  }, []);

  useEffect(() => {
    const handleIntersect = (entries) => {
      if (entries[0].isIntersecting) {
        setIsLoading(true);
        setTimeout(() => {
          const endVisibleIndex = visibleComments.length;
          const newVisibleComments = [...visibleComments, ...allComments.slice(endVisibleIndex, endVisibleIndex + 10)];
          setVisibleComments(newVisibleComments);
          setIsLoading(false);
        },);
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
  }, [allComments, visibleComments]);

  return (
    <>
      <div><NavBar /></div>

      <div className='page_container'>

        <button className="custom_board_button cancel_button">자유 게시판</button>

        <div className='free_detail_container'>
          <div className="post_detail_header">
            <div>
              <h3 className="post_detail_title">{post.title}</h3>
              <p>{user.grade} {user.userName}</p>
            </div>
            <p>{new Date(post.createdAt).toLocaleDateString()}</p>
          </div>
          <p className='post_detail_content'>{post.body}</p>
          {/* <p className='post_detail_content'>❤️{vote.voteCount}</p> */}
        </div>
        <div className='free_detail_container'>
          <input
            className='comment_input'
            type="text"
            placeholder="내용을 입력해주세요."
            value={commentText}
            onChange={handleCommentTextChange}
          />
          <button className='comment_button' onClick={handleSubmitComment}>
            작성
          </button>
          {visibleComments.map((comment) => (
            <div key={comment.id} className='post_detail_header'>
              <div>
                <p>
                  {user.grade} {user.userName}
                </p>
                <p>{comment.body}</p>
              </div>
            </div>
          ))}
          <div ref={intersectionRef}></div>
        </div>
      </div>
    </>
  );
};

export default FreeDetailPage;
