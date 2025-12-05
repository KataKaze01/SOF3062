package com.example.demo.swing;

import com.example.demo.client.HttpClient;
import com.example.demo.entity.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.HttpURLConnection;
import java.util.List;

public class StudentManagerFrame extends JFrame {
    JTextField txtId = new JTextField(10);
    JTextField txtName = new JTextField(20);
    JTextField txtMark = new JTextField(5);
    JRadioButton rdoMale = new JRadioButton("Male", true);
    JRadioButton rdoFemale = new JRadioButton("Female");
    JTable table = new JTable();
    DefaultTableModel model;

    JButton btnCreate = new JButton("Create");
    JButton btnUpdate = new JButton("Update");
    JButton btnDelete = new JButton("Delete");
    JButton btnReset = new JButton("Reset");

    // LƯU Ý: Chọn đúng HOST API bạn đang chạy (Bài 3 dùng /simple/students, Bài 4 dùng /rest/students)
    // String HOST = "http://localhost:8080/simple/students"; 
    String HOST = "http://localhost:8080/rest/students"; 
    
    ObjectMapper mapper = new ObjectMapper();

    public StudentManagerFrame() {
        super("Quản lý sinh viên (REST Client)");
        initUI();
        loadData();
    }

    void initUI() {
        setLayout(new BorderLayout());
        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));
        pnlForm.add(new JLabel("ID:")); pnlForm.add(txtId);
        pnlForm.add(new JLabel("Name:")); pnlForm.add(txtName);
        pnlForm.add(new JLabel("Mark:")); pnlForm.add(txtMark);
        JPanel pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup grp = new ButtonGroup(); grp.add(rdoMale); grp.add(rdoFemale);
        pnlGender.add(new JLabel("Gender:")); pnlGender.add(rdoMale); pnlGender.add(rdoFemale);
        pnlForm.add(pnlGender);

        JPanel pnlButton = new JPanel();
        pnlButton.add(btnCreate);
        pnlButton.add(btnUpdate);
        pnlButton.add(btnDelete);
        pnlButton.add(btnReset);

        // --- CÁC SỰ KIỆN NÚT BẤM ---
        btnCreate.addActionListener(e -> createStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnReset.addActionListener(e -> resetForm());

        // --- SỰ KIỆN DOUBLE-CLICK LÊN BẢNG (EDIT) ---
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row >= 0) {
                        editStudent(row);
                    }
                }
            }
        });

        add(pnlForm, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);

        // Cấu hình bảng không cho phép edit trực tiếp trên ô
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Gender", "Mark"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Hàm hỗ trợ hiển thị lỗi
    void showError(String msg, Exception e) {
        JOptionPane.showMessageDialog(this, msg + "\nChi tiết: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    // 1. Tải dữ liệu (GET ALL)
    void loadData() {
        try {
            HttpURLConnection conn = HttpClient.openConnection("GET", HOST);
            String json = HttpClient.readData(conn);
            List<Student> list = mapper.readValue(json, new TypeReference<List<Student>>(){});
            model.setRowCount(0);
            for (Student s : list) {
                model.addRow(new Object[]{
                        s.getId(), 
                        s.getName(), 
                        s.getGender() ? "Male" : "Female", 
                        s.getMark()
                });
            }
        } catch (Exception e) {
            showError("Lỗi tải dữ liệu!", e);
        }
    }

    // 2. Thêm mới (POST)
    void createStudent() {
        try {
            Student s = getStudentFromForm();
            String json = mapper.writeValueAsString(s);
            HttpURLConnection conn = HttpClient.openConnection("POST", HOST);
            HttpClient.writeData(conn, json);
            JOptionPane.showMessageDialog(this, "Thêm mới thành công!");
            loadData();
            resetForm();
        } catch (Exception e) {
            showError("Lỗi thêm mới!", e);
        }
    }

    // 3. Cập nhật (PUT)
    void updateStudent() {
        try {
            Student s = getStudentFromForm();
            // URL cho PUT phải kèm ID: http://.../students/{id}
            String url = HOST + "/" + s.getId();
            String json = mapper.writeValueAsString(s);
            HttpURLConnection conn = HttpClient.openConnection("PUT", url);
            HttpClient.writeData(conn, json);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadData();
        } catch (Exception e) {
            showError("Lỗi cập nhật!", e);
        }
    }

    // 4. Xóa (DELETE)
    void deleteStudent() {
        try {
            String id = txtId.getText();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ID hoặc chọn sinh viên để xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn xóa sinh viên " + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String url = HOST + "/" + id;
                HttpURLConnection conn = HttpClient.openConnection("DELETE", url);
                HttpClient.readData(conn); // Gửi request đi
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                resetForm();
            }
        } catch (Exception e) {
            showError("Lỗi xóa!", e);
        }
    }

    // 5. Hiển thị lên form (Edit)
    void editStudent(int row) {
        txtId.setText(model.getValueAt(row, 0).toString());
        txtName.setText(model.getValueAt(row, 1).toString());
        String gender = model.getValueAt(row, 2).toString();
        rdoMale.setSelected(gender.equals("Male"));
        rdoFemale.setSelected(gender.equals("Female"));
        txtMark.setText(model.getValueAt(row, 3).toString());
        txtId.setEditable(false); // Khóa ID khi edit để tránh sửa ID
        btnCreate.setEnabled(false); // Tắt nút Create khi đang edit
    }

    // 6. Xóa trắng form (Reset)
    void resetForm() {
        txtId.setText(""); 
        txtName.setText(""); 
        txtMark.setText(""); 
        rdoMale.setSelected(true);
        txtId.setEditable(true); // Mở lại ID
        btnCreate.setEnabled(true); // Bật lại nút Create
        table.clearSelection();
    }

    // Hàm tiện ích để lấy dữ liệu từ form
    Student getStudentFromForm() {
        String id = txtId.getText();
        String name = txtName.getText();
        boolean gender = rdoMale.isSelected();
        Double mark = Double.parseDouble(txtMark.getText());
        return new Student(id, name, gender, mark);
    }

    public static void main(String[] args) {
        // Sử dụng giao diện hệ thống cho đẹp hơn (tùy chọn)
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        new StudentManagerFrame().setVisible(true);
    }
}