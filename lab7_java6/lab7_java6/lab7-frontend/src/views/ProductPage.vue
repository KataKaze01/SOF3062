<template>
  <div class="container">
    <h1>Product Manager</h1>

    <!-- FORM -->
    <div class="form">
      <input v-model="form.id" placeholder="ID">
      <input v-model="form.name" placeholder="Name">
      <input v-model="form.price" type="number" placeholder="Price">
      <input v-model="form.categoryId" placeholder="Category ID">

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
          <th>Price</th>
          <th>Category</th>
          <th></th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in products" :key="p.id">
          <td>{{ p.id }}</td>
          <td>{{ p.name }}</td>
          <td>{{ p.price }}</td>
          <td>{{ p.categoryId }}</td>
          <td><button @click="loadToForm(p)">Edit</button></td>
          <td><button @click="remove(p.id)">Delete</button></td>
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
      products: [],
      form: {
        id: "",
        name: "",
        price: "",
        categoryId: ""
      }
    };
  },

  mounted() {
    this.load();
  },

  methods: {
    load() {
      axios.get("http://localhost:8080/api/products")
        .then(res => this.products = res.data)
        .catch(err => console.log(err));
    },

    create() {
      axios.post("http://localhost:8080/api/products", this.form)
        .then(() => this.load())
        .catch(err => console.log(err));
    },

    update() {
      axios.put("http://localhost:8080/api/products/" + this.form.id, this.form)
        .then(() => this.load())
        .catch(err => console.log(err));
    },

    remove(id) {
      axios.delete("http://localhost:8080/api/products/" + id)
        .then(() => this.load());
    },

    loadToForm(p) {
      this.form = { ...p };
    },

    clearForm() {
      this.form = { id: "", name: "", price: "", categoryId: "" };
    }
  }
};
</script>

<style>
.container {
  max-width: 900px;
  margin: auto;
  font-family: Arial;
}

.form {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.form input {
  padding: 5px;
}
</style>
