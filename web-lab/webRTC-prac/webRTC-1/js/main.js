"use strict";

/*
    RTCPerrConnection을 통한 동영상 스트리밍
*/

const localVideo = document.getElementById("localVideo");
const startButton = document.getElementById("startButton");

var localStream;
var localPeerConnection;

const remoteButton = document.getElementById("remoteButton");
remoteButton.disabled = true;

const remoteVideo = document.getElementById("remoteVideo");

var remotePeerConnection;
var remoteStream;

const cancelButton = document.getElementById("cancelButton");
cancelButton.disabled = true;

function getLocalMediaStream(stream) {
  localVideo.srcObject = stream;
  localStream = stream;
  remoteButton.disabled = false;
}

function cathLocalMediaStream(error) {
  console.log(error);
}

function startAction() {
  startButton.disabled = true;

  const mediaStream = {
    video: true,
  };

  navigator.mediaDevices
    .getUserMedia(mediaStream)
    .then(getLocalMediaStream)
    .catch(cathLocalMediaStream);
}

function getOtherPeer(peer) {
  return peer === localPeerConnection
    ? remotePeerConnection
    : localPeerConnection;
}

function handleConnection(event) {
  const target = event.target;
  const ice = event.candidate;

  if (ice) {
    const newIce = new RTCIceCandidate(ice);
    const otherPeer = getOtherPeer(target);

    otherPeer
      .addIceCandidate(newIce)
      .then(() => {
        console.log("added ice candidate success");
      })
      .catch((err) => {
        console.log("error adding ice candidate success", err);
      });
  }
}

function handleConnectionChanged(event) {
  console.log(`ice state changed : ${event.target}`);
}

function createdOffer(des) {
  console.log(`created offer : ${des.sdp}`);

  // localPeerConnection에 대해서 sdp 정보를 설정
  //  로컬 피어가 offer정보를 내가 전송하고자 하는 정보로 설정을 하게 됩니다.
  localPeerConnection
    .setLocalDescription(des)
    .then(() => {
      console.log("connection established in localPeer connection");
    })
    .catch((err) => {
      console.log(`connection error: ${err}`);
    });

  remotePeerConnection
    .setRemoteDescription(des)
    .then(() => {
      console.log("connection established in remotePeer connection");
    })
    .catch((err) => {
      console.log(`connection in remote peerConnection error: ${err}`);
    });

  remotePeerConnection
    .createAnswer()
    .then((answer) => {
      remotePeerConnection
        .setLocalDescription(answer)
        .then(() => {
          console.log("success to send answer from remote peer connection");
        })
        .catch(() => {
          console.log("error to send answer from remote peer connection");
        });

      localPeerConnection
        .setRemoteDescription(answer)
        .then(() => {
          console.log("success to set answer local peer connection");
        })
        .catch(() => {
          console.log("error to set answer local peer connection");
        });
    })
    .catch((err) => {
      console.log("failed to answer to local peer connection");
    });
}

function catchOfferCreated(error) {
  console.log("err to create offer");
}

function getRemoteMedia(event) {
  const stream = event.stream;
  remoteVideo.srcObject = stream;
  remoteStream = stream;
  console.log("remote video show");
}

function remoteAction() {
  remoteButton.disabled = true;
  cancelButton.disabled = false;

  console.log("remove action called");

  const videoTracks = localStream.getVideoTracks();
  const audioTracks = localStream.getAudioTracks();

  if (videoTracks.length > 0) {
    console.log(`using video tracks : ${videoTracks[0].label}`);
  }

  if (audioTracks.length > 0) {
    console.log(`using audio tracks : ${audioTracks[0].label}`);
  }

  // server ICE
  // TURN

  const servers = null;

  localPeerConnection = new RTCPeerConnection(servers);

  localPeerConnection.addEventListener("icecandidate", handleConnection); // ICE 등록 과정
  localPeerConnection.addEventListener(
    "iceconnectionstatechange",
    handleConnectionChanged
  ); // 변경 감지 이벤트 핸들러

  remotePeerConnection = new RTCPeerConnection(servers);

  remotePeerConnection.addEventListener("icecandidate", handleConnection); // ICE 등록 과정
  remotePeerConnection.addEventListener(
    "iceconnectionstatechange",
    handleConnectionChanged
  ); // 변경 감지 이벤트 핸들러

  remotePeerConnection.addEventListener("addstream", getRemoteMedia);

  localPeerConnection.addStream(localStream);

  const offerOptions = {
    offerToReceiveVideo: 1,
  };

  console.log("offer created");
  localPeerConnection
    .createOffer(offerOptions)
    .then(createdOffer)
    .catch(catchOfferCreated);
}

function cancelAction() {
  localPeerConnection.close();
  remotePeerConnection.close();
  localPeerConnection = null;
  remotePeerConnection = null;

  cancelButton.disabled = true;
  remoteButton.disabled = false;

  console.log("cancelled");
}

startButton.addEventListener("click", startAction);
remoteButton.addEventListener("click", remoteAction);
cancelButton.addEventListener("click", cancelAction);
