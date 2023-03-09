const Stomp = require('stompjs');
const SockJS = require('sockjs-client');

var stompClient =null;
const connect =()=>{
    let Sock = new SockJS('http://localhost:8090/ws');
    stompClient = Stomp.over(Sock);
    stompClient.connect({},onConnected, onError);
}

const onConnected = () => {
    setUserData({...userData,"connected": true});
    stompClient.subscribe('/chatroom/public', onMessageReceived);
    stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage);
    userJoin();
}

const userJoin=()=>{
    var chatMessage = {
        senderName: userData.username,
        status:"JOIN"
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
}

const onMessageReceived = (payload)=>{
    var payloadData = JSON.parse(payload.body);
    switch(payloadData.status){
        case "JOIN":
            if(!privateChats.get(payloadData.senderName)){
                privateChats.set(payloadData.senderName,[]);
                setPrivateChats(new Map(privateChats));
            }
            break;
        case "MESSAGE":
            publicChats.push(payloadData);
            setPublicChats([...publicChats]);
            break;
    }
}

const onPrivateMessage = (payload)=>{
    console.log(payload);
    var payloadData = JSON.parse(payload.body);
    if(privateChats.get(payloadData.senderName)){
        privateChats.get(payloadData.senderName).push(payloadData);
        setPrivateChats(new Map(privateChats));
    }else{
        let list =[];
        list.push(payloadData);
        privateChats.set(payloadData.senderName,list);
        setPrivateChats(new Map(privateChats));
    }
}

const onError = (err) => {
    console.log(err);

}

const handleMessage =(event)=>{
    const {value}=event.target;
    setUserData({...userData,"message": value});
}
const sendValue=()=>{
    if (stompClient) {
        var chatMessage = {
            senderName: userData.username,
            message: userData.message,
            status:"MESSAGE"
        };
        console.log(chatMessage);
        stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
        setUserData({...userData,"message": ""});
    }
}

const sendPrivateValue=()=>{
    if (stompClient) {
        var chatMessage = {
            senderName: userData.username,
            receiverName:tab,
            message: userData.message,
            status:"MESSAGE"
        };

        if(userData.username !== tab){
            privateChats.get(tab).push(chatMessage);
            setPrivateChats(new Map(privateChats));
        }
        stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
        setUserData({...userData,"message": ""});
    }
}

const handleUsername=(event)=>{
    const {value}=event.target;
    setUserData({...userData,"username": value});
}
connect();
