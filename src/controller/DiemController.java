package controller;

import dao.DiemDAO;
import dao.MonHocDAO;
import dao.SinhVienDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.BangDiem;
import model.DiemChiTiet;
import model.MonHoc;

public class DiemController {

    // --- FXML Components ---
    @FXML private ComboBox<String> cbLop;
    @FXML private ComboBox<MonHoc> cbMonHoc;
    @FXML private TableView<DiemChiTiet> tableDiem;
    @FXML private TableColumn<DiemChiTiet, String> colMaSV, colHoTen;
    @FXML private TableColumn<DiemChiTiet, Float> colDiemQT, colDiemThi, colDiemTB;
    @FXML private TextField tfMaSV, tfHoTen, tfDiemQT, tfDiemThi;

    @FXML
    public void initialize() {
        // --- Khởi tạo và cấu hình các component ---
        
        // 1. Nạp dữ liệu cho các ComboBox
        cbLop.setItems(SinhVienDAO.getAllLop());
        cbMonHoc.setItems(FXCollections.observableArrayList(MonHocDAO.getAllMonHoc()));

        // 2. Cấu hình cách hiển thị tên môn học trong ComboBox
        cbMonHoc.setConverter(new StringConverter<MonHoc>() {
            @Override
            public String toString(MonHoc monHoc) {
                return monHoc == null ? "" : monHoc.getMaMon() + " - " + monHoc.getTenMon();
            }
            @Override
            public MonHoc fromString(String string) { return null; }
        });

        // 3. Liên kết các cột của TableView với thuộc tính trong model DiemChiTiet
        colMaSV.setCellValueFactory(new PropertyValueFactory<>("maSV"));
        colHoTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colDiemQT.setCellValueFactory(new PropertyValueFactory<>("diemQT"));
        colDiemThi.setCellValueFactory(new PropertyValueFactory<>("diemThi"));
        colDiemTB.setCellValueFactory(new PropertyValueFactory<>("diemTB"));

        // 4. Bắt sự kiện khi người dùng chọn một hàng trong bảng
        tableDiem.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> showDiemDetails(newSelection)
        );
    }

    /**
     * Xử lý sự kiện khi nhấn nút "Xem điểm".
     * Lọc và hiển thị danh sách điểm của lớp và môn học đã chọn.
     */
    @FXML
    private void locDiem() {
        String selectedLop = cbLop.getValue();
        MonHoc selectedMonHoc = cbMonHoc.getValue();

        if (selectedLop == null || selectedMonHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn đầy đủ lớp và môn học.");
            return;
        }

        ObservableList<DiemChiTiet> diemList = DiemDAO.getDiemByLopAndMon(selectedLop, selectedMonHoc.getMaMon());
        tableDiem.setItems(diemList);
        clearInputFields();
    }
    
    /**
     * Xử lý sự kiện khi nhấn nút "Cập nhật điểm".
     * Thêm mới hoặc cập nhật điểm cho sinh viên đã chọn.
     */
    @FXML
    private void capNhatDiem() {
        String maSV = tfMaSV.getText();
        MonHoc selectedMonHoc = cbMonHoc.getValue();

        if (maSV.isEmpty() || selectedMonHoc == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn sinh viên", "Vui lòng chọn một sinh viên từ bảng và môn học để cập nhật.");
            return;
        }

        if (!isValidInput()) return;

        BangDiem diem = new BangDiem(
                maSV,
                selectedMonHoc.getMaMon(),
                Float.parseFloat(tfDiemQT.getText()),
                Float.parseFloat(tfDiemThi.getText()),
                0 // DiemTB được CSDL tự tính
        );

        if (DiemDAO.upsertDiem(diem)) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật điểm thành công!");
            locDiem(); // Tải lại bảng điểm sau khi cập nhật
        } else {
            showAlert(Alert.AlertType.ERROR, "Thất bại", "Cập nhật điểm thất bại.");
        }
    }

    /**
     * Hiển thị thông tin điểm của sinh viên được chọn lên các TextField.
     * @param diem đối tượng DiemChiTiet được chọn từ bảng.
     */
    private void showDiemDetails(DiemChiTiet diem) {
        if (diem != null) {
            tfMaSV.setText(diem.getMaSV());
            tfHoTen.setText(diem.getHoTen());
            tfDiemQT.setText(String.valueOf(diem.getDiemQT()));
            tfDiemThi.setText(String.valueOf(diem.getDiemThi()));
        } else {
            clearInputFields();
        }
    }
    
    private void clearInputFields() {
        tfMaSV.clear();
        tfHoTen.clear();
        tfDiemQT.clear();
        tfDiemThi.clear();
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu điểm nhập vào.
     * @return true nếu hợp lệ, ngược lại false.
     */
    private boolean isValidInput() {
        String errorMessage = "";
        try {
            float diemQT = Float.parseFloat(tfDiemQT.getText());
            if (diemQT < 0 || diemQT > 10) {
                errorMessage += "Điểm quá trình phải trong khoảng [0, 10]!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Điểm quá trình phải là một số!\n";
        }

        try {
            float diemThi = Float.parseFloat(tfDiemThi.getText());
            if (diemThi < 0 || diemThi > 10) {
                errorMessage += "Điểm thi phải trong khoảng [0, 10]!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Điểm thi phải là một số!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi Nhập Liệu", errorMessage);
            return false;
        }
    }

    /**
     * Hiển thị hộp thoại thông báo.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}