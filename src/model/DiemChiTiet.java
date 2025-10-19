package model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Lớp model này dùng để hiển thị thông tin điểm chi tiết trong TableView,
 * bao gồm cả họ tên sinh viên.
 */
public class DiemChiTiet {
    private final StringProperty maSV;
    private final StringProperty hoTen;
    private final FloatProperty diemQT;
    private final FloatProperty diemThi;
    private final FloatProperty diemTB;

    public DiemChiTiet(String maSV, String hoTen, float diemQT, float diemThi, float diemTB) {
        this.maSV = new SimpleStringProperty(maSV);
        this.hoTen = new SimpleStringProperty(hoTen);
        this.diemQT = new SimpleFloatProperty(diemQT);
        this.diemThi = new SimpleFloatProperty(diemThi);
        this.diemTB = new SimpleFloatProperty(diemTB);
    }

    // --- Getters ---
    public String getMaSV() { return maSV.get(); }
    public String getHoTen() { return hoTen.get(); }
    public float getDiemQT() { return diemQT.get(); }
    public float getDiemThi() { return diemThi.get(); }
    public float getDiemTB() { return diemTB.get(); }

    // --- Property Getters ---
    public StringProperty maSVProperty() { return maSV; }
    public StringProperty hoTenProperty() { return hoTen; }
    public FloatProperty diemQTProperty() { return diemQT; }
    public FloatProperty diemThiProperty() { return diemThi; }
    public FloatProperty diemTBProperty() { return diemTB; }
}
