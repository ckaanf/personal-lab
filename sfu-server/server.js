const express = require('express');
const http = require('http');
const socketIO = require('socket.io');
const mediasoup = require('mediasoup');

const app = express();
const server = http.createServer(app);
const io = socketIO(server, {
    cors: {
        origin: "*", // 실제 프로덕션에서는 특정 도메인만 허용해야 합니다.
    }
});

let worker;
let router;
let producerTransport;
let consumerTransport;
let producer;

// --- Mediasoup Worker & Router 생성 ---
(async () => {
    try {
        // 1. Worker 생성: 하나의 CPU 코어에서 실행되는 Mediasoup C++ 프로세스
        worker = await mediasoup.createWorker({
            logLevel: 'warn'
        });
        console.log('✅ Mediasoup worker created');

        worker.on('died', () => {
            console.error('Mediasoup worker has died');
            setTimeout(() => process.exit(1), 2000);
        });

        // 2. Router 생성: Worker 내에서 미디어 트래픽을 라우팅하는 역할
        const mediaCodecs = [
            { kind: 'audio', mimeType: 'audio/opus', clockRate: 48000, channels: 2 },
            { kind: 'video', mimeType: 'video/VP8', clockRate: 90000, parameters: { 'x-google-start-bitrate': 1000 } }
        ];
        router = await worker.createRouter({ mediaCodecs });
        console.log('✅ Mediasoup router created');

    } catch (err) {
        console.error('Error creating mediasoup worker/router', err);
    }
})();

// --- Socket.IO 시그널링 로직 ---
io.on('connection', (socket) => {
    console.log(`🔌 New client connected: ${socket.id}`);

    socket.on('disconnect', () => {
        console.log(`👋 Client disconnected: ${socket.id}`);
    });

    // 클라이언트가 방에 참여하면 라우터의 RTP 스펙을 보내줌
    socket.on('getRouterRtpCapabilities', (callback) => {
        callback(router.rtpCapabilities);
    });

    // --- 여기부터 WebRTC Transport, Producer, Consumer 관련 로직이 추가될 예정입니다 ---
    // (지금은 비워둡니다)

});


const PORT = 3000;
server.listen(PORT, () => {
    console.log(`🚀 SFU Server is listening on port ${PORT}`);
});