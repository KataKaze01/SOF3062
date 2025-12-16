<!-- src/components/StudentManager.vue -->
<template>
  <div class="student-manager">
    <h2>Quản Lý Sinh Viên</h2>

    <!-- Form thêm/sửa sinh viên -->
    <form @submit.prevent="handleSubmit">
      <div>
        <label>ID:</label>
        <input v-model="currentStudent.id" type="text" required />
      </div>
      <div>
        <label>Họ tên:</label>
        <input v-model="currentStudent.fullname" type="text" required />
      </div>
      <div>
        <label>Điểm:</label>
        <input v-model.number="currentStudent.mark" type="number" step="0.01" required />
      </div>
      <div>
        <label>Ngày sinh:</label>
        <input v-model="currentStudent.birthday" type="date" required />
      </div>
      <button type="submit">{{ isEditing ? 'Cập Nhật' : 'Tạo Mới' }}</button>
      <button type="button" @click="resetForm">Reset</button>
    </form>

    <!-- Bảng hiển thị danh sách sinh viên -->
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Họ tên</th>
          <th>Điểm</th>
          <th>Ngày sinh</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student.id }}</td>
          <td>{{ student.fullname }}</td>
          <td>{{ student.mark }}</td>
          <td>{{ formatDate(student.birthday) }}</td>
          <td>
            <button @click="editStudent(student)">Sửa</button>
            <button @click="deleteStudent(student.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'StudentManager',
  data() {
    return {
      students: [],
      currentStudent: {
        id: '',
        fullname: '',
        mark: null,
        birthday: ''
      },
      isEditing: false,
      apiUrl: 'http://localhost:8080/api/students' // URL API backend
    };
  },
  mounted() {
    this.fetchStudents(); // Gọi API khi component mount
  },
  methods: {
    async fetchStudents() {
      try {
        const response = await axios.get(this.apiUrl);
        this.students = response.data;
      } catch (error) {
        console.error('Lỗi khi lấy danh sách sinh viên:', error);
      }
    },
    async handleSubmit() {
      try {
        if (this.isEditing) {
          // Cập nhật sinh viên
          await axios.put(`${this.apiUrl}/${this.currentStudent.id}`, this.currentStudent);
          this.isEditing = false;
        } else {
          // Tạo sinh viên mới
          await axios.post(this.apiUrl, this.currentStudent);
        }
        this.resetForm(); // Reset form sau khi tạo/cập nhật
        this.fetchStudents(); // Làm mới danh sách
      } catch (error) {
        console.error('Lỗi khi lưu sinh viên:', error);
      }
    },
    editStudent(student) {
      this.currentStudent = { ...student }; // Sao chép đối tượng sinh viên để sửa
      this.isEditing = true;
    },
    async deleteStudent(id) {
      try {
        await axios.delete(`${this.apiUrl}/${id}`);
        this.fetchStudents(); // Làm mới danh sách sau khi xóa
      } catch (error) {
        console.error('Lỗi khi xóa sinh viên:', error);
      }
    },
    resetForm() {
      this.currentStudent = {
        id: '',
        fullname: '',
        mark: null,
        birthday: ''
      };
      this.isEditing = false;
    },
    formatDate(dateString) {
      // Chuyển đổi ngày tháng từ chuỗi ISO sang định dạng DD/MM/YYYY
      const date = new Date(dateString);
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
      const year = date.getFullYear();
      return `${day}/${month}/${year}`;
    }
  }
};
</script>

<style scoped>
.student-manager {
  padding: 20px;
}

form {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 400px;
}

input, button {
  padding: 8px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

button {
  cursor: pointer;
  margin-right: 5px;
}
</style>