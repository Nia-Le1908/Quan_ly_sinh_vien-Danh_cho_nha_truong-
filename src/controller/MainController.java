package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void moQuanLySinhVien(ActionEvent event) {
        System.out.println("Mở giao diện Sinh viên...");
        openWindow("/view/SinhVienView.fxml", "Quản lý Sinh viên");
    }

    @FXML
    private void moQuanLyMonHoc(ActionEvent event) {
        System.out.println("Mở giao diện Môn học...");
        openWindow("/view/MonHocView.fxml", "Quản lý Môn học");
    }

    @FXML
    private void moBangDiem(ActionEvent event) {
        System.out.println("Mở giao diện Điểm...");
        openWindow("/view/DiemView.fxml", "Quản lý Bảng điểm");
    }
    
    /**
     * Phương thức chung để mở một cửa sổ FXML mới.
     * Phiên bản này đã được sửa để không gây ra NullPointerException.
     * Việc áp dụng CSS sẽ do từng tệp FXML tự quản lý.
     * @param fxmlPath Đường dẫn tới tệp .fxml
     * @param title    Tiêu đề của cửa sổ
     */
    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            
            // Đặt cửa sổ mới là modal để ngăn tương tác với cửa sổ chính
            stage.initModality(Modality.APPLICATION_MODAL);
            // Hiển thị và đợi cho đến khi cửa sổ mới được đóng
            stage.showAndWait(); 
        } catch (IOException e) {
            System.err.println("Lỗi khi tải tệp FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void thoatChuongTrinh(ActionEvent event) {
        Platform.exit();
    }
}