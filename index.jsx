import React from 'react';
import ReactDOM from 'react-dom';
import App from './App'; // App.jsx에서 ApexChart를 기본 내보내기로 가져옴
import './index.css'; // 스타일 파일 (선택 사항)

const domContainer = document.querySelector('#app');
ReactDOM.render(<App />, domContainer);
