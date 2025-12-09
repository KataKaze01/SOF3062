<template>
  <div>
    <h2>Danh sách sản phẩm</h2>
    <table border="1" style="width: 100%; border-collapse: collapse;">
      <thead>
        <tr>
          <th>Tên sản phẩm</th>
          <th>Đơn giá</th>
          <th>Hình</th>
          <th>Nguồn gốc</th>
          <th>Hạn sử dụng</th>
          <th>Chi tiết</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="sp in sanPhams" :key="sp.maSanPham">
          <td>{{ sp.tenSanPham }}</td>
          <td>{{ sp.donGia }}</td>
          <td><img :src="sp.hinhAnh" alt="Hình" width="50" v-if="sp.hinhAnh" /></td>
          <td>{{ sp.nguonGoc }}</td>
          <td>{{ formatDate(sp.hanSuDung) }}</td>
          <td>
            <router-link :to="`/chitiet/${sp.maSanPham}`">Chi tiết</router-link>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      sanPhams: []
    }
  },
  async mounted() {
    try {
      const res = await axios.get('http://localhost:8080/api/sanpham')
      this.sanPhams = res.data
    } catch (error) {
      console.error(error)
      alert('Không thể tải danh sách sản phẩm!')
    }
  },
  methods: {
    formatDate(dateStr) {
      if (!dateStr) return ''
      const d = new Date(dateStr)
      return d.toISOString().split('T')[0] // YYYY-MM-DD
    }
  }
}
</script>