package controller;

import dao.MonHocDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.MonHoc;

public class MonHocController {

    @FXML private TableView<MonHoc> tableMonHoc;
    @FXML private TableColumn<MonHoc, String> colMaMon, colTenMon, colLoaiMon, colMonTienQuyet;
    @FXML private TableColumn<MonHoc, Integer> colSoTinChi;
    @FXML private TextField tfMaMon, tfTenMon, tfSoTinChi, tfMonTienQuyet, tfTimKiem;
    @FXML private ComboBox<String> cbLoaiMon;

    private ObservableList<MonHoc> list;

    @FXML
    public void initialize() {
        cbLoaiMon.setItems(FXCollections.observableArrayList("Bắt buộc", "Tự chọn"));
        colMaMon.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMaMon()));
        colTenMon.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTenMon()));
        colSoTinChi.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSoTinChi()).asObject());
        colLoaiMon.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLoaiMon()));
        colMonTienQuyet.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMonTienQuyet()));

        loadData();
    }

    private void loadData() {
        list = FXCollections.observableArrayList(MonHocDAO.getAllMonHoc());
        tableMonHoc.setItems(list);
    }

    @FXML
    private void themMonHoc() {
        MonHoc m = new MonHoc(
                tfMaMon.getText(),
                tfTenMon.getText(),
                Integer.parseInt(tfSoTinChi.getText()),
                cbLoaiMon.getValue(),
                tfMonTienQuyet.getText()
        );
        MonHocDAO.insert(m);
        loadData();
        clearFields();
    }

    @FXML
    private void suaMonHoc() {
        MonHoc selected = tableMonHoc.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        selected.setTenMon(tfTenMon.getText());
        selected.setSoTinChi(Integer.parseInt(tfSoTinChi.getText()));
        selected.setLoaiMon(cbLoaiMon.getValue());
        selected.setMonTienQuyet(tfMonTienQuyet.getText());

        MonHocDAO.update(selected);
        loadData();
        clearFields();
    }

    @FXML
    private void xoaMonHoc() {
        MonHoc selected = tableMonHoc.getSelectionModel().getSelectedItem();
        if (selected != null) {
            MonHocDAO.delete(selected.getMaMon());
            loadData();
        }
    }

    @FXML
    private void timKiemMonHoc() {
        String keyword = tfTimKiem.getText();
        list = FXCollections.observableArrayList(MonHocDAO.search(keyword));
        tableMonHoc.setItems(list);
    }

    private void clearFields() {
        tfMaMon.clear();
        tfTenMon.clear();
        tfSoTinChi.clear();
        cbLoaiMon.setValue(null);
        tfMonTienQuyet.clear();
        tfTimKiem.clear();
    }
}
