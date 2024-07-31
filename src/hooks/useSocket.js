import { useCallback, useEffect, useState } from "react";
import * as io from 'socket.io-client'

export const useSocket = (room, username) => {
    const [socket, setSocket] = useState();
    const [socketResponse, setSocketResponse] = useState({
        room: "",
        message: "",
        username: "",
        messageType:"",
        createdAt: ""
    });
    const [isConnected, setConnected] = useState(false);

    const sendData = useCallback((payload) => {
        socket.emit("send_message", {
            room: room,
            message: payload.message,
            username: username,
            messageType: "CLIENT"
        });
    }, [socket, room]);

    useEffect(() => {
        // 환경변수에서 소켓서버 주소 불러오기 
        const socketBaseUrl = process.env.REACT_APP_SOCKET_BASE_URL;
        console.log (process.env.REACT_APP_SOCKET_BASE_URL);
        //  소켓 서버에, 유저 이름과 룸을 연결
        const s = io(socketBaseUrl, {
            query: `username=${username}&room=${room}`,
            forceNew:   true
        });
        setSocket(s);
        s.on("connect", () => {
            setConnected(true);
        });
        s.on("connect_error", (error) => {
            console.error("SOCKET CONNECTION ERROR", error);
        });
        s.on("read_message", (res) => {
            console.log('test: ', res);
            setSocketResponse({
                room: res.room,
                message: res.message,
                username: res.username,
                messageType: res.messageType,
                createdAt: res.createdAt
            });
        });

        return () => {
            s.disconnect();
        };
    }, [room, username]);

    return { isConnected, socketResponse, sendData };
}