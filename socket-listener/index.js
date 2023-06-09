const WebSocket = require('sockjs-client');
const Stomp = require('stompjs');

const username = process.argv[2]; // Get username from command line argument

// Connect to WebSocket endpoint
const socket = new WebSocket('http://localhost:8181/notifications/ws');
const stompClient = Stomp.over(socket);
console.log(username)
stompClient.connect({}, function(frame) {
    console.log('Connected to the WebSocket server');

    // Subscribe to the public chatroom
    stompClient.subscribe('/chatroom/public', function(message) {
    });

    // Subscribe to private messages for this user
    stompClient.subscribe('/user/' + username + '/private', function(message) {
        console.log('New Notification Received!');
        var notification = JSON.parse(message.body);
        console.log (notification.sender+" "+notification.message);
    });

    /* Send a public message
    stompClient.send('/app/notification', {}, JSON.stringify({
        sender: username,
        message: 'Hello, everyone!',
        date: null,
        status: 'MESSAGE'
    }));*/

    /* Send a private message
    stompClient.send('/app/private-notification', {}, JSON.stringify({
        sender: username,
        receiver: 'someOtherUser',
        message: 'Hi, how are you doing?',
        date: null,
        status: 'MESSAGE'
    }));*/
});
