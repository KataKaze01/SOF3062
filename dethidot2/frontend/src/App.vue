<template>
  <div class="container">
    <h2>Quản lý Sản phẩm</h2>

    <!-- Form nhập liệu -->
    <div class="form-group">
      <label>Mã sản phẩm</label>
      <input v-model="currentProduct.id" type="number" readonly />
    </div>

    <div class="form-group">
      <label>Tên sản phẩm</label>
      <input v-model="currentProduct.name" type="text" placeholder="Nhập tên sản phẩm" />
    </div>

    <div class="form-group">
      <label>Đơn giá</label>
      <input v-model="currentProduct.price" type="number" step="0.01" placeholder="Nhập giá" />
    </div>

    <div class="form-group">
      <label>Hình ảnh</label>
      <input v-model="currentProduct.image" type="text" placeholder="URL hình ảnh" />
    </div>

    <div class="form-group">
      <label>Mô tả</label>
      <textarea v-model="currentProduct.description" placeholder="Mô tả sản phẩm"></textarea>
    </div>

    <div class="button-group">
      <button @click="addProduct">Thêm</button>
      <button @click="updateProduct">Cập nhật</button>
      <button @click="resetForm">Nhập lại</button>
    </div>

    <!-- Tìm kiếm theo khoảng giá -->
    <div class="search-group">
      <label>Tìm theo khoảng giá:</label>
      <input v-model="searchMinPrice" type="number" step="0.01" placeholder="Giá tối thiểu" />
      <input v-model="searchMaxPrice" type="number" step="0.01" placeholder="Giá tối đa" />
      <button @click="searchByPriceRange">Tìm</button>
    </div>

    <!-- Table danh sách sản phẩm -->
    <table class="product-table">
      <thead>
        <tr>
          <th>MÃ SP</th>
          <th>TÊN SP</th>
          <th>ĐƠN GIÁ</th>
          <th>HÌNH ẢNH</th>
          <th>THAO TÁC</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="product in products" :key="product.id" @click="selectProduct(product)">
          <td>{{ product.id }}</td>
          <td>{{ product.name }}</td>
          <td>{{ product.price | currency }}</td>
          <td><img :src="product.image" alt="Hình ảnh" width="50" /></td>
          <td><button @click.stop="editProduct(product)">Sửa</button></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// Khai báo state
const products = ref([]);
const currentProduct = ref({
  id: null,
  name: '',
  price: 0,
  image: '',
  description: ''
});

const searchMinPrice = ref(null);
const searchMaxPrice = ref(null);

// Hàm lấy danh sách sản phẩm
const fetchProducts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/products', {
      params: {
        minPrice: searchMinPrice.value,
        maxPrice: searchMaxPrice.value
      }
    });
    products.value = response.data;
  } catch (error) {
    console.error('Lỗi khi tải sản phẩm:', error);
  }
};

// Hàm chọn sản phẩm để sửa
const selectProduct = (product) => {
  currentProduct.value = { ...product };
};

// Hàm thêm sản phẩm
const addProduct = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/products', currentProduct.value);
    products.value.push(response.data);
    resetForm();
  } catch (error) {
    alert('Lỗi khi thêm sản phẩm!');
  }
};

// Hàm cập nhật sản phẩm
const updateProduct = async () => {
  if (!currentProduct.value.id) {
    alert('Vui lòng chọn sản phẩm để cập nhật!');
    return;
  }
  try {
    const response = await axios.put(`http://localhost:8080/api/products/${currentProduct.value.id}`, currentProduct.value);
    const index = products.value.findIndex(p => p.id === currentProduct.value.id);
    if (index !== -1) {
      products.value[index] = response.data;
    }
    resetForm();
  } catch (error) {
    alert('Lỗi khi cập nhật sản phẩm!');
  }
};

// Hàm xóa trắng form
const resetForm = () => {
  currentProduct.value = {
    id: null,
    name: '',
    price: 0,
    image: '',
    description: ''
  };
};

// Hàm tìm kiếm theo khoảng giá
const searchByPriceRange = () => {
  fetchProducts();
};

// Gọi API khi component mounted
onMounted(() => {
  fetchProducts();
});
</script>

<style scoped>
.container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group input, .form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-group button, .search-group button {
  padding: 8px 16px;
  margin-right: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.button-group button:hover, .search-group button:hover {
  background-color: #0056b3;
}

.search-group {
  margin: 20px 0;
  display: flex;
  align-items: center;
}

.search-group input {
  margin-right: 10px;
  width: 120px;
}

.product-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.product-table th, .product-table td {
  padding: 10px;
  text-align: left;
  border: 1px solid #ddd;
}

.product-table th {
  background-color: #007bff;
  color: white;
}

.product-table tr:hover {
  background-color: #f5f5f5;
  cursor: pointer;
}

.product-table img {
  border-radius: 4px;
}

/* Filter nút "Sửa" */
.product-table button {
  padding: 2px 8px;
  background-color: #ffc107;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.product-table button:hover {
  background-color: #e0a800;
}
</style>

<!-- Filter số tiền -->
<script>
export default {
  filters: {
    currency(value) {
      if (!value) return '';
      return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
    }
  }
}
</script>