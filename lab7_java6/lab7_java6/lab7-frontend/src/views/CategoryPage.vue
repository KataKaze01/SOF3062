<template>
  <div class="container">
    <h1>Category Manager</h1>

    <!-- FORM -->
    <div class="form">
      <input v-model="form.id" placeholder="ID">
      <input v-model="form.name" placeholder="Name">
      <button @click="create">Create</button>
      <button @click="update">Update</button>
      <button @click="clearForm">Clear</button>
    </div>

    <hr />

    <!-- TABLE -->
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="c in categories" :key="c.id">
          <td>{{ c.id }}</td>
          <td>{{ c.name }}</td>
          <td><button @click="loadToForm(c)">Edit</button></td>
          <td><button @click="remove(c.id)">Delete</button></td>
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
      categories: [],
      form: { id: "", name: "" }
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    load() {
      axios.get("http://localhost:8080/api/categories")
        .then(res => this.categories = res.data);
    },

    create() {
      axios.post("http://localhost:8080/api/categories", this.form)
        .then(() => this.load());
    },

    update() {
      axios.put("http://localhost:8080/api/categories/" + this.form.id, this.form)
        .then(() => this.load());
    },

    remove(id) {
      axios.delete("http://localhost:8080/api/categories/" + id)
        .then(() => this.load());
    },

    loadToForm(c) {
      this.form = { ...c };
    },

    clearForm() {
      this.form = { id: "", name: "" };
    }
  }
};
</script>

<style>
.container { max-width: 900px; margin: auto; font-family: Arial; }
.form { display: flex; gap: 10px; margin-bottom: 20px; }
.form input { padding: 5px; }
</style>
