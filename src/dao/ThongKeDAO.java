package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Lớp DAO cho các chức năng thống kê
 */
public class ThongKeDAO {

    /**
     * Chuyển đổi điểm hệ 10 sang điểm hệ 4
     * @param diemHe10 Điểm ở thang 10
     * @return Điểm ở thang 4
     */
    private static double chuyenDiemHe4(double diemHe10) {
        if (diemHe10 >= 8.5) return 4.0; // A
        if (diemHe10 >= 7.0) return 3.0; // B
        if (diemHe10 >= 5.5) return 2.0; // C
        if (diemHe10 >= 4.0) return 1.0; // D
        return 0.0; // F
    }

    /**
     * Tính điểm tích lũy (hệ 4) cho một sinh viên
     * @param maSV Mã sinh viên
     * @return Điểm tích lũy hệ 4
     */
    public static double getDiemTichLuy(String maSV) {
        double tongDiemNhanTinChi = 0;
        int tongTinChi = 0;

        String sql = "SELECT b.diemTB, m.soTinChi " +
                     "FROM bangdiem b " +
                     "JOIN monhoc m ON b.maMon = m.maMon " +
                     "WHERE b.maSV = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maSV);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                double diemTBHe10 = rs.getDouble("diemTB");
                int soTinChi = rs.getInt("soTinChi");

                // Chỉ tính các môn qua (điểm F không được tính vào điểm tích lũy)
                if (diemTBHe10 >= 4.0) {
                    tongDiemNhanTinChi += chuyenDiemHe4(diemTBHe10) * soTinChi;
                    tongTinChi += soTinChi;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tongTinChi == 0) {
            return 0.0;
        }
        
        // Làm tròn điểm tích lũy đến 2 chữ số thập phân
        double diemTichLuy = tongDiemNhanTinChi / tongTinChi;
        return Math.round(diemTichLuy * 100.0) / 100.0;
    }
}

