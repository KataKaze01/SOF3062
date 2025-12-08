<script setup></script>

<template>
  <div>
    <h1>Lab 7 - Quản lý</h1>
    <nav>
      <router-link to="/categories">Categories</router-link> |
      <router-link to="/products">Products</router-link> |
      <router-link to="/users">Users</router-link>
    </nav>
    <router-view />
  </div>
</template>

<style>
nav { margin-bottom: 10px; }
</style>

