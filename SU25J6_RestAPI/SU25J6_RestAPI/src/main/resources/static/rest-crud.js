var ctrl = {
    list: [],
    get entity() {
        return {
            id: $("#id").val(),
            name: $("#name").val(),
            gender: $("#male").prop("checked"),
            mark: +$("#mark").val()
        }
    },
    set entity(e) {
        $("#id").val(e.id);
        $("#name").val(e.name);
        $(e.gender?"#male":"#female").prop("checked", true);
        $("#mark").val(e.mark);
    },
    loadAll() {
        var url = `http://localhost:8080/api/students`;
        axios.get(url).then(resp => {
            this.list = resp.data;
            this.fillToTable();
        }).catch(error => alert("Error: loadAll()"))
    },
    fillToTable() {
        $("tbody").empty();
        this.list.forEach(e => {
            var tr = `<tr>
                <td>${e.id}</td>
                <td>${e.name}</td>
                <td>${e.gender ? 'Male' : 'Female'}</td>
                <td>${e.mark}</td>
                <td>
                    <a onclick="ctrl.edit('${e.id}')" href="#">Edit</a> |
                    <a onclick="ctrl.delete('${e.id}')" href="#">Delete</a>
                </td>
            </tr>`;
            $("tbody").append(tr);
        })
    },
    edit(id) {
        var url = `http://localhost:8080/api/students/${id}`;
        axios.get(url).then(resp => {
            this.entity = resp.data;
        }).catch(error => alert("Error: edit()"))
    },
    clear() {
        this.entity = { genger: true };
    },
    create() {
        var url = `http://localhost:8080/api/students`;
        axios.post(url, this.entity).then(resp => {
            this.loadAll();
            this.clear();
        }).catch(error => alert("Error: create()"))
    },
    update() {
        var url = `http://localhost:8080/api/students/${this.entity.id}`;
        axios.put(url, this.entity).then(resp => {
            this.loadAll();
        }).catch(error => alert("Error: update()"))
    },
    delete(id) {
        var url = `http://localhost:8080/api/students/${id || this.entity.id}`;
        axios.delete(url).then(resp => {
            this.loadAll();
            this.clear();
        }).catch(error => alert("Error: delete()"))
    }
}
ctrl.loadAll();