import React from 'react';
import '../styles/Button.css';
import '../styles/Modal.css';
import PropTypes from 'prop-types';

const Modal = ({ content, onCancel, onConfirm }) => {
   return (
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
    );
  };

  Modal.propTypes = {
    content: PropTypes.node.isRequired, // node 타입으로 content prop을 받음
    onCancel: PropTypes.func.isRequired, // 함수 타입으로 onCancel prop을 받음
    onConfirm: PropTypes.func.isRequired, // 함수 타입으로 onConfirm prop을 받음
  };

export default Modal;