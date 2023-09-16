import React, { useState } from 'react';
import '../styles/ReEdit.css';
import { getCustomerPost, patchPost } from '../api/api.js';

function ReEditFree() {

  const [formData, setFormData] = useState({
    title: '',
    body: '',
    img: ''
  });
  
  const handleGetPostData = () => {
    getCustomerPost(10)
      .then((resp)=>{
        setFormData({
          ...formData,
          title: resp.data[0].title,
          body: resp.data[0].body
        });
      })
      .catch((err)=>{
        console.error(err);
      });
  } 

  
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
  
  const handlePatchPost = () => {
    
    if (formData.title !== '' && formData.body !== '') {
      patchPost(10, 2, formData.title, formData.body, formData.img)
        .then((resp)=>{
          console.log('성공', resp.data);
        })
        .catch((err)=>{
          console.error(err);
        })
    } 
  }


  return (
    <div className="ReEditFree_container">

      <button onClick={handleGetPostData} >불러오기</button>

      <div className="editer_wrapper wide reEdit">
        <button onClick={handlePatchPost} className="post_complete_btn wide reEdit">수정 완료</button>
        <input 
          name='title' 
          value={formData.title} 
          type="text" 
          className="post_title_input" 
          placeholder="제목을 입력하세요"
          onChange={handleInputChange}
        />
        <textarea 
          name='body' 
          value={formData.body} 
          className='post_text_area' 
          placeholder="내용을 입력하세요."
          onChange={handleInputChange}
        ></textarea>
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