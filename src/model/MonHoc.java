package model;

import javafx.beans.property.*;

public class MonHoc {
    private final StringProperty maMon;
    private final StringProperty tenMon;
    private final IntegerProperty soTinChi;
    private final StringProperty loaiMon;
    private final StringProperty monTienQuyet;

    public MonHoc(String maMon, String tenMon, int soTinChi, String loaiMon, String monTienQuyet) {
        this.maMon = new SimpleStringProperty(maMon);
        this.tenMon = new SimpleStringProperty(tenMon);
        this.soTinChi = new SimpleIntegerProperty(soTinChi);
        this.loaiMon = new SimpleStringProperty(loaiMon);
        this.monTienQuyet = new SimpleStringProperty(monTienQuyet);
    }

    // --- Getters ---
    public String getMaMon() { return maMon.get(); }
    public String getTenMon() { return tenMon.get(); }
    public int getSoTinChi() { return soTinChi.get(); }
    public String getLoaiMon() { return loaiMon.get(); }
    public String getMonTienQuyet() { return monTienQuyet.get(); }

    // --- Setters ---
    public void setTenMon(String text) { this.tenMon.set(text); }
    public void setSoTinChi(int value) { this.soTinChi.set(value); }
    public void setLoaiMon(String value) { this.loaiMon.set(value); }
    public void setMonTienQuyet(String text) { this.monTienQuyet.set(text); }
    
    // --- Property Getters ---
    public StringProperty maMonProperty() { return maMon; }
    public StringProperty tenMonProperty() { return tenMon; }
    public IntegerProperty soTinChiProperty() { return soTinChi; }
    public StringProperty loaiMonProperty() { return loaiMon; }
    public StringProperty monTienQuyetProperty() { return monTienQuyet; }

}

