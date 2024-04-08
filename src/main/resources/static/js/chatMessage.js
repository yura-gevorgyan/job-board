'use strict';

const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-merssages');
const userArea = document.querySelector('#userArea');
const userNameArea = document.querySelector('#userNameArea');

let stompClient = null;
let nickname = null;
let name = null;
let selectedUserId = null;
let toName = null;

async function connect(event) {
    try {
        const response = await fetch('/user/currentUser');
        if (!response.ok) {
            new Error('Не удалось получить информацию о текущем пользователе');
        }
        const currentUser = await response.json();

        nickname = currentUser.id;
        name = currentUser.name;

        if (nickname && name) {
            const socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }
    } catch (error) {
        console.error('Ошибка подключения к серверу:', error);
    }

}


function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/user/users');
    let connectedUsers = await connectedUsersResponse.json();
    connectedUsers = connectedUsers.filter(user => user.id !== nickname);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';
    connectedUsers.forEach(user => {
        const listItem = document.createElement('li');
        const anchorElement = document.createElement('a');
        anchorElement.href = "#!";
        anchorElement.innerHTML = `
            <div class="d-sm-flex justify-content-between">
                <div class="d-flex mb-2 mb-sm-0">
                    <div class="flex-shrink-0">
                        <div class="position-relative">
                            <div id=" ${user.id}" class="orange__avatar">${user.name[0].toUpperCase()}</div>
                        </div>
                    </div>
                    <div class="flex-grow-1 ms-3">
                        <h6 class="mb-1">${user.name}</h6>
                        <span class="display-31 text-muted font-weight-600">${user.email}</span>
                    </div>
                 
                </div>
            </div>
        `;

        listItem.id = user.id;
        listItem.setAttribute('data-name', user.name);
        listItem.classList.add('user-item');

        listItem.appendChild(anchorElement);
        listItem.addEventListener('click', userItemClick);
        connectedUsersList.appendChild(listItem);

        if (connectedUsers.indexOf(user) < connectedUsers.length - 1) {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}


function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    if (selectedUserId !== null) {
        let changeUs = ' ' + selectedUserId;
        const chn = document.getElementById(changeUs);
        chn.classList.remove('hard__orange__avatar');
        chn.classList.add('orange__avatar');
    }

    const clickedUser = event.currentTarget;

    selectedUserId = clickedUser.getAttribute('id');
    let changeUser = ' ' + selectedUserId;
    const chng = document.getElementById(changeUser);
    chng.classList.remove('orange__avatar');
    chng.classList.add('hard__orange__avatar');
    fetchAndDisplayUserChat(clickedUser.getAttribute('data-name')).then();

    const chatArea = document.querySelector('.chat-area');
    chatArea.classList.remove('hidden');

    messageForm.classList.remove('hidden');

    const or = document.getElementById(' ' + selectedUserId);
    or.classList.remove('sender__avatar');
    or.classList.add('orange__avatar');
}


function displayMessage(senderId, toUserName, content, date) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === nickname) {
        const senderBlock = document.createElement('div');
        senderBlock.classList.add("sender__block");

        const senderContainer = document.createElement('div');
        senderContainer.classList.add("sender");

        const senderText = document.createElement('p');
        senderText.classList.add("text");
        senderText.textContent = content;

        const senderTime = document.createElement('p');
        senderTime.classList.add("sender__time");
        if (date !== undefined) {
            let s = date.toString().substring(4, 10);
            let m = date.toString().substring(16, 21);
            senderTime.textContent = s + ' ' + m;
        } else {
            senderTime.textContent = formatDate(new Date());
        }
        messageContainer.append(senderBlock);
        senderBlock.append(senderContainer);
        senderContainer.append(senderText);
        senderContainer.append(senderTime);
    } else {
        const receiverBlock = document.createElement('div');
        receiverBlock.classList.add("receiver__block");

        const receiverContainer = document.createElement('div');
        receiverContainer.classList.add("receiver");

        const receiverTitle = document.createElement('h6');
        receiverTitle.textContent = toUserName;

        const receiverText = document.createElement('p');
        receiverText.classList.add("text");
        receiverText.textContent = content;

        const receiverAvatar = document.createElement('div');
        receiverAvatar.textContent = toUserName[0].toUpperCase();
        receiverAvatar.classList.add("receiver__avatar");

        const receiverTime = document.createElement('p');
        receiverTime.classList.add("receiver__time");
        if (date !== undefined) {
            let s = date.toString().substring(4, 10);
            let m = date.toString().substring(16, 21);
            receiverTime.textContent = s + ' ' + m;
        } else {
            receiverTime.textContent = formatDate(new Date());
        }
        messageContainer.append(receiverBlock);
        receiverBlock.append(receiverAvatar);
        receiverBlock.append(receiverContainer);
        receiverContainer.append(receiverTitle)
        receiverContainer.append(receiverText);
        receiverContainer.append(receiverTime);
    }


    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat(userName) {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    if (userName === name) {
        toName = userName;
    } else {
        toName = userName;
    }
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.fromUser.id, chat.fromUser.name, chat.messageText, new Date(chat.messageDate));
    });
    chatArea.scrollTop = chatArea.scrollHeight;

    userArea.innerHTML = '';
    const senderAvatar = document.createElement('div');
    senderAvatar.textContent = toName[0].toUpperCase();
    senderAvatar.classList.add("sender__avatar");
    userArea.append(senderAvatar);
    userNameArea.innerHTML = '';
    const some = document.createElement('h6');
    some.classList.add("mb-0");
    some.textContent = toName;
    userNameArea.append(some);
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickname,
            recipientId: +selectedUserId,
            content: messageInput.value.trim(),
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(nickname, selectedUserId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}


async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();

    console.log('Message received', payload);
    const message = JSON.parse(payload.body);
    if (selectedUserId && +selectedUserId === +message.senderId) {
        displayMessage(message.senderId, message.senderName, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    const or = document.getElementById(' ' + message.senderId);
    or.classList.remove('orange__avatar');
    or.classList.add('sender__avatar');

    if (selectedUserId !== null) {
        let changeUser = ' ' + selectedUserId;
        const chng = document.getElementById(changeUser);
        chng.classList.remove('orange__avatar');
        chng.classList.add('hard__orange__avatar');
    }

}

const fromUserId = document.getElementById('fromUserId').innerText;
const toUserId = document.getElementById('toUserId').innerText;
const userName = document.getElementById('userName').innerText;


async function getChat() {
    if (fromUserId !== null && fromUserId.trim() !== ''
        && toUserId !== null && toUserId.trim() !== ''
        && userName !== null && userName.trim() !== '') {
        nickname = fromUserId;
        selectedUserId = toUserId;
        fetchAndDisplayUserChat(userName).then();

        const chatArea = document.querySelector('.chat-area');
        chatArea.classList.remove('hidden');

        messageForm.classList.remove('hidden');
    }
}

const searchChat = document.querySelector(".search__chat");
searchChat.addEventListener('input', () => {
    const searchText = searchChat.value.toLowerCase();
    const userItems = document.querySelectorAll('.user-item');
    userItems.forEach(item => {
        const name = item.getAttribute('data-name').toLowerCase();
        const email = item.querySelector('.display-31').innerText.toLowerCase();
        if (name.includes(searchText) || email.includes(searchText) || searchText.length === 0) {
            item.style.display = "";
        } else {
            item.style.display = "none";
        }
    });
});

document.getElementById('deleteBtn').addEventListener('click', function () {
    const selectedUser = selectedUserId + '';
    deleteConversation(selectedUser).then();
});

async function deleteConversation(selectedUser) {
    const url = `/delete-conversation/${selectedUser}`;
    await fetch(url, {
        method: 'GET',
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/profile/message';
            }
        })
        .catch(error => {
            console.error('Error deleting conversation:', error);
        });
}

function formatDate(date) {
    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const day = date.getDate().toString().padStart(2, '0');
    const month = months[date.getMonth()];
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    return `${month} ${day} ${hours}:${minutes}`;
}

connect().then(() => {

}).catch(error => {

    console.error('Failed to establish connection:', error);
});

getChat().then(() => {

}).catch(error => {

    console.error('Failed to establish connection:', error);
});
messageForm.addEventListener('submit', sendMessage, true);