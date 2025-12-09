<template>
  <div id="app">
    <nav style="margin-bottom: 20px; padding: 10px; background: #f0f0f0;">
      <router-link to="/themmoi" style="margin-right: 15px;">Thêm mới</router-link>
      <router-link to="/lietke">Liệt kê</router-link>
    </nav>

    <router-view />
  </div>
</template>

<style>
#app {
  font-family: Arial, sans-serif;
  padding: 20px;
}
</style>