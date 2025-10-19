package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BangDiem;
import model.DiemChiTiet;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiemDAO {

    /**
     * Lấy danh sách điểm chi tiết (bao gồm họ tên SV) của một lớp và một môn học cụ thể.
     * Sử dụng LEFT JOIN để hiển thị cả những sinh viên chưa có điểm.
     * @param lop Tên lớp
     * @param maMon Mã môn học
     * @return Danh sách điểm chi tiết
     */
    public static ObservableList<DiemChiTiet> getDiemByLopAndMon(String lop, String maMon) {
        ObservableList<DiemChiTiet> list = FXCollections.observableArrayList();
        String sql = "SELECT sv.maSV, sv.hoTen, bd.diemQT, bd.diemThi, bd.diemTB " +
                     "FROM sinhvien sv LEFT JOIN bangdiem bd " +
                     "ON sv.maSV = bd.maSV AND bd.maMon = ? " +
                     "WHERE sv.lop = ? ORDER BY sv.maSV";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maMon);
            ps.setString(2, lop);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Lấy điểm. Nếu sinh viên chưa có điểm (giá trị là NULL trong DB),
                // getFloat sẽ trả về 0. Điều này phù hợp để hiển thị.
                float diemQT = rs.getFloat("diemQT");
                float diemThi = rs.getFloat("diemThi");
                float diemTB = rs.getFloat("diemTB");

                list.add(new DiemChiTiet(
                        rs.getString("maSV"),
                        rs.getString("hoTen"),
                        diemQT,
                        diemThi,
                        diemTB
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm mới hoặc cập nhật một bản ghi điểm.
     * Sử dụng cú pháp INSERT ... ON DUPLICATE KEY UPDATE của MySQL.
     * @param diem đối tượng BangDiem chứa thông tin điểm cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public static boolean upsertDiem(BangDiem diem) {
        String sql = "INSERT INTO bangdiem (maSV, maMon, diemQT, diemThi) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE diemQT = VALUES(diemQT), diemThi = VALUES(diemThi)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, diem.getMaSV());
            ps.setString(2, diem.getMaMon());
            ps.setFloat(3, diem.getDiemQT());
            ps.setFloat(4, diem.getDiemThi());

            return ps.executeUpdate() >= 1; // executeUpdate có thể trả về 1 (INSERT) hoặc 2 (UPDATE)
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}