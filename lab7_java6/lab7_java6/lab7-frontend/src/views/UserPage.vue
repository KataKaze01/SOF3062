<template>
  <div class="container">
    <h1>User Manager</h1>

    <!-- FORM -->
    <div class="form">
      <input v-model="form.username" placeholder="Username">
      <input v-model="form.fullname" placeholder="Fullname">
      <input v-model="form.password" placeholder="Password" type="password">
      <input v-model="form.role" placeholder="Role">
      <button @click="create">Create</button>
      <button @click="update">Update</button>
      <button @click="clearForm">Clear</button>
    </div>

    <hr />

    <!-- TABLE -->
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>Username</th>
          <th>Fullname</th>
          <th>Role</th>
          <th></th>
          <th></th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="u in users" :key="u.username">
          <td>{{ u.username }}</td>
          <td>{{ u.fullname }}</td>
          <td>{{ u.role }}</td>
          <td><button @click="loadToForm(u)">Edit</button></td>
          <td><button @click="remove(u.username)">Delete</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      form: { username: "", fullname: "", password: "", role: "" }
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    load() {
      axios.get("http://localhost:8080/api/users")
        .then(res => this.users = res.data);
    },

    create() {
      axios.post("http://localhost:8080/api/users", this.form)
        .then(() => this.load());
    },

    update() {
      axios.put("http://localhost:8080/api/users/" + this.form.username, this.form)
        .then(() => this.load());
    },

    remove(username) {
      axios.delete("http://localhost:8080/api/users/" + username)
        .then(() => this.load());
    },

    loadToForm(u) {
      this.form = { ...u };
    },

    clearForm() {
      this.form = { username: "", fullname: "", password: "", role: "" };
    }
  }
};
</script>

<style>
.container { max-width: 900px; margin: auto; font-family: Arial; }
.form { display: flex; gap: 8px; margin-bottom: 20px; }
.form input { padding: 5px; }
</style>
