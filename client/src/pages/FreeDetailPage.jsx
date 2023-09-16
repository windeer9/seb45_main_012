import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import '../styles/Button.css';
import '../styles/BoardDetailPage.css';
import NavBar from '../components/NavBar.jsx';
import {
  getPost,
  getUser,
  getComment,
  postComment,
  getVote,
  patchVote,
} from '../api/api.js';

const FreeDetailPage = () => {
  const { postId, userId, voteId } = useParams();

  const [post, setPost] = useState({});
  const [user, setUser] = useState({});

  const [vote, setVote] = useState({});
  const [liked, setLiked] = useState({});
  const [commentText, setCommentText] = useState('');

  const [allComments, setAllComments] = useState([]);
  const [visibleComments, setVisibleComments] = useState([]);

  const intersectionRef = useRef(null);


  useEffect(() => {
    // 좋아요 상태 초기화
    getVote(postId, voteId)
      .then((response) => {
        const voteData = response.data;
        console.log(voteData);
        console.log("liked: ", liked);
        setVote(voteData);
      })
      .catch((error) => {
        console.error('좋아요 정보 가져오기 오류:', error);
      });
  }, [postId, voteId, user.userId, liked]);


  const handleVoteClick = async () => {
    try {
      // 만약 liked가 true인 경우, 좋아요를 취소해야 합니다.
      // 그외의 경우에는 좋아요를 추가해야 합니다.
      const voteType = liked ? 'Cancel' : 'Like';
  
      // API 요청 보내기
      const response = await patchVote(postId, userId, voteId, { voteType: voteType });
      console.log("API 보낸 후",response.data);
      console.log("API 후 liked: ", liked);
      // API 요청이 성공적으로 완료된 경우에만 UI를 업데이트합니다.
      if (response.status === 200) {
        // 좋아요 상태 업데이트
        setLiked(!liked);
  
        // 좋아요 카운트 업데이트
        const updatedVoteCount = liked ? vote.voteCount - 1 : vote.voteCount + 1;
        setVote({ ...vote, voteCount: updatedVoteCount });
      } else {
        console.error('좋아요 버튼 기능 오류');
      }
    } catch (error) {
      // 오류 처리
      console.error('좋아요 오류', error);
    }
  };


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

    postComment(postId, userId, commentText)
      .then((response) => {
        console.log('댓글 작성 완료:', response.data);
        // window.location.reload();
        // 댓글 새로고침 보다 갱신이 더 자연스러워서 수정합니다.
        getComment(postId)
          .then((response) => {
            const sortedComments = response.data.sort((a, b) => {
              return new Date(b.createdAt) - new Date(a.createdAt);
            });
            setAllComments(sortedComments);
            setVisibleComments(sortedComments.slice(0, 10));
          })
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
        setUser(response.data);
      })
      .catch((error) => {
        console.error('유저 데이터 가져오기 오류:', error);
      });

    // 댓글 데이터 가져오기
    getComment(postId)
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
        setTimeout(() => {
          const endVisibleIndex = visibleComments.length;
          const newVisibleComments = [...visibleComments, ...allComments.slice(endVisibleIndex, endVisibleIndex + 10)];
          setVisibleComments(newVisibleComments);
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
          <button onClick={handleVoteClick} className='vote_button'>
            {liked ? `❤️ ${vote.voteCount}` : `🤍 ${vote.voteCount}`}
          </button>
        </div>
        <div className='free_detail_container'>
          <div className='detail_comment_container'>
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
          </div>
          
          {visibleComments.map((comment) => (
            <div key={comment.commentId} className='post_detail_header'>
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
