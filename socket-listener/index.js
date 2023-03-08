const io = require('socket.io-client');
const socket = io.connect('http://localhost:3000?room=11');

socket.on('connect', function() {
  console.log('Connected to SocketIO server!');
});

socket.on('disconnect', function() {
  console.log('Disconnected from SocketIO server!');
});

socket.on('message', function(data) {
  console.log('Received message:', data);
});

// To send a message to the server
socket.emit('message', 'Hello, SocketIO server!');
