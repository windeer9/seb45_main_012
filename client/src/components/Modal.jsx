import React from 'react';
import '../styles/Button.css';
import '../styles/Modal.css';
import PropTypes from 'prop-types';

const Modal = ({ content, onCancel, onConfirm }) => {
   return (
   //   <div className='page_container'>
        <div className="modal_overlay">
            <div className="modal_container">
              <div className="modal_content">
                  {content}
               </div>
               <div className="modal_buttons">
               <button className="custom_button confirm_button" onClick={onConfirm}>
               확인
               </button>
               <button className="custom_button cancel_button" onClick={onCancel}>
               취소
               </button>
            </div>
            </div>
         </div>
      // </div>
    );
  };

  Modal.propTypes = {
    content: PropTypes.node.isRequired, // node 타입으로 content prop을 받음
    onCancel: PropTypes.func.isRequired, // 함수 타입으로 onCancel prop을 받음
    onConfirm: PropTypes.func.isRequired, // 함수 타입으로 onConfirm prop을 받음
  };

export default Modal;



//예시
{/* <Modal
  content={
    <>
      <p>탈퇴 안내</p>
      <p>회원탈퇴를 신청하기 전에 안내 사항을 꼭 확인해주세요.</p>
      <ul>
        <li>✔ 사용하고 계신 아이디는 탈퇴 시 복구가 불가능합니다.</li>
        <li>
          ✔ 탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다.
          <br />
          &emsp;삭제된 데이터는 복구되지 않습니다. 삭제되는 내용을 확인하시고
          <br />
          &emsp;필요한 데이터는 미리 백업을 해주세요.
        </li>
        <li>
          ✔ 탈퇴 후에도 게시판형 서비스에 등록한 게시물은 그대로 남아 있습니다.
          <br />
          &emsp;게시판형 서비스에 남아 있는 게시글은 탈퇴 후 삭제할 수 없습니다.
        </li>
      </ul>
    </>
  }
  onCancel={handleCancel}
  onConfirm={handleConfirm}
/> */}