import "./App.css";
import { useEffect, useRef, useState } from "react";

const App = () => {
  const [socket, setSocket] = useState(null);
  const [roomId, setRoomId] = useState(null);

  const localVideoRef = useRef(null);
  const remoteVideoRef = useRef(null);
  const peerConnection = useRef(null);

  const servers = {
    iceServers: [{ url: "stun:stun.l.google.com:19302" }],
  };

  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8080");
    setSocket(ws);

    ws.onmessage = async (event) => {
      const data = JSON.parse(event.data);

      if (data.type === "offer") {
        await peerConnection.current.setRemoteDescription(data);
        const answer = await peerConnection.current.createAnswer();
        await peerConnection.current.setLocalDescription(answer);

        ws.send(
          JSON.stringify({
            type: "signal",
            roomId,
            signalData: peerConnection.current.localDescription,
          })
        );
      } else if (data.type === "answer") {
        await peerConnection.current.setRemoteDescription(data);
      } else if (data.type === "candidate") {
        await peerConnection.current.addIceCandidate(data.candidate);
      }
    };

    return () => {
      ws.close();
    };
  }, [roomId]);

  const joinRoom = async () => {
    if (!roomId) {
      alert("Please enter Room ID");
      return;
    }

    socket.send(JSON.stringify({ type: "join", roomId }));

    const stream = await navigator.mediaDevices.getUserMedia({
      video: true,
    });

    localVideoRef.current.srcObject = stream;

    peerConnection.current = new RTCPeerConnection(servers);

    stream.getTracks().forEach((track) => {
      peerConnection.current.addTrack(track, stream);
    });

    peerConnection.current.onicecandidate = (event) => {
      if (event.candidate) {
        socket.send(
          JSON.stringify({
            type: "signal",
            roomId,
            signalData: { type: "candidate", candidate: event.candidate },
          })
        );
      }
    };

    peerConnection.current.ontrack = (event) => {
      remoteVideoRef.current.srcObject = event.streams[0];
    };

    const offer = await peerConnection.current.createOffer();
    await peerConnection.current.setLocalDescription(offer);

    console.log(offer);
    socket.send(
      JSON.stringify({
        type: "signal",
        roomId,
        signalData: peerConnection.current.localDescription,
      })
    );
  };

  return (
    <div className="App">
      <h1>React WebRTC Video Chat</h1>
      <input
        type="text"
        placeholder="Enter Room ID"
        value={roomId}
        onChange={(e) => setRoomId(e.target.value)}
      />
      <button onClick={joinRoom}> Join Room</button>

      <div>
        <div>
          Local Video
          <video ref={localVideoRef} autoPlay></video>
        </div>

        <div>
          Remote Video
          <video ref={remoteVideoRef} autoPlay></video>
        </div>
      </div>
    </div>
  );
};

export default App;
