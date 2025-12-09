<template>
  <div>
    <h2>Thêm mới sản phẩm</h2>
    <form @submit.prevent="handleSubmit">
      <label>Mã sản phẩm:</label>
      <input v-model="form.maSanPham" required /><br><br>

      <label>Tên sản phẩm:</label>
      <input v-model="form.tenSanPham" required /><br><br>

      <label>Đơn giá:</label>
      <input v-model.number="form.donGia" type="number" min="0" required /><br><br>

      <label>Hạn sử dụng:</label>
      <input v-model="form.hanSuDung" type="date" required /><br><br>

      <label>Hình ảnh (URL):</label>
      <input v-model="form.hinhAnh" placeholder="https://example.com/image.jpg" /><br><br>

      <label>Nguồn gốc:</label><br>
      <label><input type="radio" v-model="form.nguonGoc" value="Cao cấp" /> Cao cấp</label>
      <label><input type="radio" v-model="form.nguonGoc" value="Bình dân" /> Bình dân</label><br><br>

      <button type="submit">Thêm mới</button>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      form: {
        maSanPham: '',
        tenSanPham: '',
        donGia: null,
        hanSuDung: '',
        hinhAnh: '',
        nguonGoc: 'Cao cấp'
      }
    }
  },
  methods: {
    async handleSubmit() {
      try {
        await axios.post('http://localhost:8080/api/sanpham', this.form)
        alert('Thêm sản phẩm thành công!')
        this.$router.push('/lietke')
      } catch (error) {
        console.error(error)
        alert('Lỗi khi thêm sản phẩm!')
      }
    }
  }
}
</script>