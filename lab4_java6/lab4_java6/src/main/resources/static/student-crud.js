    // ============================================
// LAB 4 - BÀI 3 & 4: STUDENT CRUD JAVASCRIPT
// Firebase Realtime Database Integration
// ============================================

// URL Firebase Realtime Database của bạn
const host = "https://lab4-1a7bf-default-rtdb.firebaseio.com/student";

const $api = {
    key: null,

    get student() {
        return {
            id: $("#id").val(),
            name: $("#name").val(),
            mark: $("#mark").val(),
            gender: $("#male").prop("checked")
        };
    },

    set student(e) {
        $("#id").val(e.id || "");
        $("#name").val(e.name || "");
        $("#mark").val(e.mark || "");
        $("#male").prop("checked", e.gender === true);
        $("#female").prop("checked", e.gender === false);
    },

    fillToTable() {
        axios.get(`${host}/students.json`)
            .then(res => {
                $("tbody").empty();
                let data = res.data || {};

                Object.keys(data).forEach(key => {
                    let e = data[key];
                    let tr = `
                        <tr>
                            <td>${e.id}</td>
                            <td>${e.name}</td>
                            <td>${e.mark}</td>
                            <td>${e.gender ? 'Male' : 'Female'}</td>
                            <td>
                                <a href="#" onclick="$api.edit('${key}')">Edit</a>
                                |
                                <a href="#" onclick="$api.delete('${key}')">Delete</a>
                            </td>
                        </tr>`;
                    $("tbody").append(tr);
                });
            })
            .catch(() => alert("Lỗi tải danh sách!"));
    },

    edit(key) {
        this.key = key;
        axios.get(`${host}/students/${key}.json`)
            .then(res => this.student = res.data)
            .catch(() => alert("Lỗi tải sinh viên!"));
    },

    create() {
        axios.post(`${host}/students.json`, this.student)
            .then(() => {
                this.fillToTable();
                this.reset();
            })
            .catch(() => alert("Lỗi thêm mới!"));
    },

    update() {
        axios.put(`${host}/students/${this.key}.json`, this.student)
            .then(() => this.fillToTable())
            .catch(() => alert("Lỗi cập nhật!"));
    },

    delete(key) {
        axios.delete(`${host}/students/${key || this.key}.json`)
            .then(() => {
                this.fillToTable();
                this.reset();
            })
            .catch(() => alert("Lỗi xóa!"));
    },

    reset() {
        this.student = {};
        this.key = null;
    }
};

$api.fillToTable();

