<template>
  <div style="padding:20px; width:450px; margin:auto; border:5px solid #1ba7c3">
    <input v-model="username" placeholder="Your name" />
    <button @click="connect" :disabled="connected">Connect</button>
    <button @click="disconnect" :disabled="!connected">Disconnect</button>

    <h3>Online Users</h3>
    <ul>
      <li v-for="(u, i) in onlineUsers" :key="i">{{ u }}</li>
    </ul>

    <h3>OUTPUT</h3>
    <ul>
      <li v-for="(m, index) in messages" :key="index">
        <b>{{ m.from }}:</b> {{ m.content }}
      </li>
    </ul>

    <h3>INPUT</h3>
    <input v-model="message" placeholder="Message" />
    <button @click="sendMessage" :disabled="!connected">Send</button>
  </div>
</template>

<script setup>
import { ref } from "vue";

const username = ref("");
const message = ref("");
const messages = ref([]);
const onlineUsers = ref([]);

let stomp = null;
const connected = ref(false);

const connect = () => {
  const socket = new SockJS("http://localhost:8080/ws");
  stomp = Stomp.over(socket);

  stomp.connect({}, () => {
    connected.value = true;

    // nhận tin chat
    stomp.subscribe("/topic/messages", (msg) => {
      messages.value.push(JSON.parse(msg.body));
    });

    // nhận danh sách user online
    stomp.subscribe("/topic/users", (msg) => {
      const data = JSON.parse(msg.body);
      onlineUsers.value = data.content.split(",");
    });

    stomp.send("/app/username", {}, JSON.stringify({
      from: username.value
    }));
  });
};

const disconnect = () => {
  stomp.send("/app/leave", {}, JSON.stringify({
    from: username.value
  }));

  stomp.disconnect();
  connected.value = false;
};

const sendMessage = () => {
  stomp.send("/app/chat", {}, JSON.stringify({
    from: username.value,
    content: message.value
  }));
  message.value = "";
};
</script>
