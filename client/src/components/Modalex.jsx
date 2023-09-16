//모달 example입니다.
//props로 ({ content, onCancel, onConfirm })를 전달받고,
//여기서 이런 식으로 사용하면 됩니다.

import React, { useState } from 'react';
import '../styles/Button.css';
import NavBar from '../components/NavBar.jsx';
import Modal from '../components/Modal.jsx';

const Modalex = () => {
  const [showModal, setShowModal] = useState(false);
  const [agreed, setAgreed] = useState(false);

  const handleOpenModal = () => {
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleCheckboxChange = () => {
    setAgreed(!agreed);
  };

  const handleConfirm = () => {
    if (agreed) {
      // 동의한 경우에만 처리
      // 탈퇴 또는 다른 동작 수행
      // handleConfirm 함수 내부에 탈퇴 로직을 추가하세요.
      // 모달을 닫을 수도 있습니다.
      handleCloseModal();
    } else {
      // 동의하지 않은 경우에 대한 처리
      alert('안내 사항에 동의해야 합니다.');
    }
  };

return (
  <>
    <div><NavBar /></div>
      <div className='page_container'>
          
      <button className="custom_board_button confirm_button" onClick={handleOpenModal}>
        자유 게시판
      </button>
      {showModal && (
        <Modal
          content={
            <>
              <h3>탈퇴 안내</h3>
              <h4>회원탈퇴를 신청하기 전에 안내 사항을 꼭 확인해주세요.</h4><br />
                <p>✔ 사용하고 계신 아이디는 탈퇴 시 복구가 불가능합니다.</p><br />
                <p>
                  ✔ 탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다.
                  <br />
                  &emsp;삭제된 데이터는 복구되지 않습니다. 삭제되는 내용을 확인하시고
                  <br />
                  &emsp;필요한 데이터는 미리 백업을 해주세요.
                </p><br />
                <p>
                  ✔ 탈퇴 후에도 게시판형 서비스에 등록한 게시물은 그대로 남아 있습니다.
                  <br />
                  &emsp;게시판형 서비스에 남아 있는 게시글은 탈퇴 후 삭제할 수 없습니다.
                </p><br />
                <h4>
                  <input type="checkbox" checked={agreed} onChange={handleCheckboxChange} />
                  안내 사항을 모두 확인하였으며, 이에 동의합니다.
                </h4>
            </>
          }
          onCancel={handleCloseModal}
          onConfirm={handleConfirm}
        />
      )}
    </div>
  </>
  )
}

export default Modalex;