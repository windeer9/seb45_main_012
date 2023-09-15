import React from "react";
import '../styles/ReEdit.css';

function ReEditFree() {

  

  return (
    <div className="ReEditFree-container">
      <div className="editer_wrapper wide reEdit">
        <button className="post_complete_btn wide reEdit">수정 완료</button>
        <input name='title' type="text" className="post_title_input" placeholder="제목을 입력하세요"/>
        <textarea name='body' className='post_text_area' placeholder="내용을 입력하세요."></textarea>
      </div>
    </div>
  );
}

function ReEditAuth() {


  return (
    <>
    
    </>
  );
}

export { ReEditFree, ReEditAuth };