'use strict';

const startButton = document.getElementById('startButton');
const hangupButton = document.getElementById('hangupButton');
const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');
const signalingStatus = document.getElementById('signalingStatus');
const iceStatus = document.getElementById('iceStatus');

let pc;
let localStream;
const peerId = 'peer-' + Math.random().toString(36).substr(2, 9);
console.log(`My Peer ID: ${peerId}`);

// --- Signaling Server Connection ---
const sock = new SockJS('http://localhost:8081/ws');
const stompClient = Stomp.over(sock);

stompClient.connect({},
    () => { // On Connected
        console.log('✅ Signaling Server Connected');
        signalingStatus.textContent = 'Connected';
        signalingStatus.style.color = 'green';

        stompClient.subscribe('/topic/signal', message => {
            handleSignalingMessage(JSON.parse(message.body));
        });
    },
    (error) => { // On Error
        console.error('Signal server connection error:', error);
        signalingStatus.textContent = 'Error';
        signalingStatus.style.color = 'red';
    }
);

startButton.addEventListener('click', startCall);
hangupButton.addEventListener('click', hangUp);

// --- Main Functions ---
async function startCall() {
    console.log('Start button clicked');
    startButton.disabled = true;
    hangupButton.disabled = false;

    try {
        localStream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
        localVideo.srcObject = localStream;

        createPeerConnection();

        localStream.getTracks().forEach(track => pc.addTrack(track, localStream));

        const offer = await pc.createOffer();
        await pc.setLocalDescription(offer);
        sendMessage({ type: 'offer', sdp: offer });

    } catch (err) {
        console.error('Error starting call:', err);
    }
}

function createPeerConnection() {
    console.log('Creating Peer Connection...');
    const configuration = { iceServers: [{ 'urls': 'stun:stun.l.google.com:19302' }] };
    pc = new RTCPeerConnection(configuration);

    pc.ontrack = (event) => {
        console.log('✅ Remote track received');
        if (remoteVideo.srcObject !== event.streams[0]) {
            remoteVideo.srcObject = event.streams[0];
        }
    };

    pc.onicecandidate = (event) => {
        if (event.candidate) {
            sendMessage({ type: 'candidate', candidate: event.candidate });
        }
    };

    pc.oniceconnectionstatechange = () => {
        console.log(`ICE Connection State: ${pc.iceConnectionState}`);
        iceStatus.textContent = pc.iceConnectionState;
        if (pc.iceConnectionState === 'connected' || pc.iceConnectionState === 'completed') {
            iceStatus.style.color = 'green';
        } else if (pc.iceConnectionState === 'failed') {
            iceStatus.style.color = 'red';
        } else {
            iceStatus.style.color = 'orange';
        }
    };
}

async function handleSignalingMessage(message) {
    if (message.senderId === peerId) return;

    console.log(`📥 Message received:`, message);

    if (!pc) {
        createPeerConnection();
    }

    try {
        if (message.type === 'offer') {
            await pc.setRemoteDescription(new RTCSessionDescription(message.sdp));

            if (localStream) {
                localStream.getTracks().forEach(track => {
                    if (!pc.getSenders().find(s => s.track === track)) {
                        pc.addTrack(track, localStream);
                    }
                });
            }

            const answer = await pc.createAnswer();
            await pc.setLocalDescription(answer);
            sendMessage({ type: 'answer', sdp: answer });

        } else if (message.type === 'answer') {
            await pc.setRemoteDescription(new RTCSessionDescription(message.sdp));

        } else if (message.type === 'candidate') {
            await pc.addIceCandidate(new RTCIceCandidate(message.candidate));

        }
    } catch (err) {
        console.error('Error handling signaling message:', err);
    }
}

function sendMessage(payload) {
    const message = {
        senderId: peerId,
        ...payload
    };
    console.log(`📤 Sending message:`, message);
    stompClient.send("/app/signal", {}, JSON.stringify(message));
}

function hangUp() {
    console.log('Hanging up.');
    if (pc) {
        pc.close();
        pc = null;
    }
    if (localStream) {
        localStream.getTracks().forEach(track => track.stop());
    }
    localVideo.srcObject = null;
    remoteVideo.srcObject = null;
    startButton.disabled = false;
    hangupButton.disabled = true;
    iceStatus.textContent = 'Closed';
    iceStatus.style.color = 'red';
}