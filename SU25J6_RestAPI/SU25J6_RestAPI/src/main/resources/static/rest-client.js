var ctrl = {
	host: "http://localhost:8080/api",
    getAll() {
        var url = `${this.host}/students`;
        axios.get(url).then(resp => {
			console.log(resp.data);
        }).catch(error => alert("Error: loadAll()"))
    },
    getById() {
		var id = "SV01";
        var url = `${this.host}/students/${id}`;
        axios.get(url).then(resp => {
            console.log(resp.data);
        }).catch(error => alert("Error: edit()"))
    },
    post() {
		var data = {
			id: "SV06",
			name:"Sinh Viên 06",
			gender: true,
			mark: 10
		}
        var url = `${this.host}/students`;
        axios.post(url, data).then(resp => {
            console.log(resp.data);
        }).catch(error => alert("Error: create()"))
    },
    put() {
		var data = {
			id: "SV06",
			name:"Sinh Viên 06",
			gender: true,
			mark: 10
		}
        var url = `${this.host}/students/${data.id}`;
        axios.put(url, data).then(resp => {
            console.log(resp.data);
        }).catch(error => alert("Error: update()"))
    },
    delete() {
		var id = "SV06";
        var url = `${this.host}/students/${id}`;
        axios.delete(url).then(resp => {
            console.log(resp.data);
        }).catch(error => alert("Error: delete()"))
    }
}