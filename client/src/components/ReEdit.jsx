import React, { useState, useRef } from 'react';
import '../styles/ReEdit.css';
import { getCustomerPost, patchPost } from 'api/api.js';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus } from "@fortawesome/free-solid-svg-icons";
import { postPosts } from "api/api.js";

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


  
  const [formData, setFormData] = useState({
    type: 'auth',
    title: '',
    body: '',
    open: 'true',
    img: ''
  });

  const handlePatchPost = () => {
    
    if (formData.title !== '' && formData.body !== '' && formData.img !== '') {
      patchPost(10, 66, formData.title, formData.body, formData.img)
        .then((resp)=>{
          console.log('성공', resp.data);
        })
        .catch((err)=>{
          console.error(err);
        })
    } else {
      alert('이미지 업로드와 제목 및 내용을 모두 작성해주세요.');
    }
  }

  const handleGetPostData = () => {
    
    getCustomerPost(10)
      .then((resp)=>{
        setFormData({
          ...formData,
          title: resp.data[51].title,
          body: resp.data[51].body,
          img: resp.data[51].imageUrl
          
        });
        setPreviewImage(resp.data[51].imageUrl);
        
        console.log('성공', resp.data);
      })
      .catch((err)=>{
        console.error(err);
      });
  } 

  const [previewImage, setPreviewImage] = useState(null);
  const imageInputRef = useRef(null);



  const handleFileInputChange = (e) => {
    const file = e.target.files[0];
    setFormData({ ...formData, file });

    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setPreviewImage(e.target.result);
      };
      reader.readAsDataURL(file);

      e.target.value = null;

      setFormData({ ...formData, img: file });
      
    } 
  };

  const handleImageClick = () => {
    if (imageInputRef.current) {
      imageInputRef.current.click();
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };


  return (
    <>
      <div className='ReEditAuth_container'>


        <button onClick={handleGetPostData} >불러오기</button>
        <div className="post_editer_with_image reEdit">

          <div className="image_upload_form" >
            <input
              type="file"
              accept="image/*"
              onChange={handleFileInputChange}
              ref={imageInputRef}
            />
            {previewImage && <img src={previewImage} alt="미리보기" onClick={handleImageClick} aria-hidden="true" />}
            <div className={`plus_image_icon ${previewImage ? 'clear' : ''}`} >
              <FontAwesomeIcon className='plus_icon' icon={faPlus}/>이미지
            </div>
          </div>

          <div className="editer_wrapper">
          <button onClick={handlePatchPost} className="post_complete_btn wide">수정 완료</button>
            <input onChange={handleInputChange} name='title' value={formData.title} type="text" className="post_title_input" placeholder="제목을 입력하세요"/>
            <textarea onChange={handleInputChange} name='body' value={formData.body} className='post_text_area' placeholder="내용을 입력하세요."></textarea>
          </div>

        </div>
      </div>
    </>
  );
}

export { ReEditFree, ReEditAuth };