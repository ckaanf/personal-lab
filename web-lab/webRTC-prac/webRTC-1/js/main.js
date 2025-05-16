"use strict";

/*
 RTCPeerConnection을 통한 동영상 스트리밍
*/
const localVideo = document.getElementById("localVideo");
const startButton = document.getElementById("startButton");

var localStream;

function getLocalMediaStream(stream) {
  localVideo.srcObject = stream;
  localStream = stream;
}

function catchLocalMediaStream(error) {
  console.log(error)
}

function startAction() {
  startButton.disabled = true;

  const mediaStream = {
    video : true,
  };

  navigator.mediaDevices.getUserMedia(mediaStream).then().catch;
}

startButton.addEventListener("click", startAction);
