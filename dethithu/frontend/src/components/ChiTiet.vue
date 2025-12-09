<template>
  <div v-if="sanPham">
    <h2>Chi tiết sản phẩm</h2>
    <img :src="sanPham.hinhAnh" alt="Hình" v-if="sanPham.hinhAnh" style="max-width: 200px;" /><br><br>

    <p><strong>Tên sản phẩm:</strong> {{ sanPham.tenSanPham }}</p>
    <p><strong>Đơn giá:</strong> {{ sanPham.donGia }}</p>
    <p><strong>Nguồn gốc:</strong> {{ sanPham.nguonGoc }}</p>
    <p><strong>Hạn sử dụng:</strong> {{ formatDate(sanPham.hanSuDung) }}</p>

    <router-link to="/lietke">← Quay lại danh sách</router-link>
  </div>
  <div v-else>
    <p>Đang tải...</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  props: ['ma'],
  data() {
    return {
      sanPham: null
    }
  },
  async mounted() {
    try {
      const res = await axios.get(`http://localhost:8080/api/sanpham/${this.ma}`)
      this.sanPham = res.data
    } catch (error) {
      console.error(error)
      alert('Không tìm thấy sản phẩm!')
      this.$router.push('/lietke')
    }
  },
  methods: {
    formatDate(dateStr) {
      if (!dateStr) return ''
      const d = new Date(dateStr)
      return d.toISOString().split('T')[0]
    }
  }
}
</script>