package com.sicco.erp.model;

import java.util.ArrayList;

public class TimKiem {
	String id, tencongviec, ngaybatdau, tinhtrang, tiendo, nguoithuchien,
			phongban, loaicongviec, hancuoi, duan, mucuutien, nguoiduocxem,
			nguoigiao, mota, tonghopbaocao, tepdinhkem, url;
	ArrayList<ThaoLuan> thaoluan;

	public TimKiem(String id, String tencongviec, String hancuoi) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.hancuoi = hancuoi;
	}

	public TimKiem(String id, String tencongviec, String tinhtrang,
			String tiendo, String nguoithuchien, String phongban,
			String loaicongviec, String hancuoi, String duan, String mucuutien,
			String nguoiduocxem, String nguoigiao, String mota,
			String tonghopbaocao, String tepdinhkem, String url) {
		this.id = id;
		this.tencongviec = tencongviec;
		this.tinhtrang = tinhtrang;
		this.tiendo = tiendo;
		this.nguoithuchien = nguoithuchien;
		this.phongban = phongban;
		this.loaicongviec = loaicongviec;
		this.hancuoi = hancuoi;
		this.duan = duan;
		this.mucuutien = mucuutien;
		this.nguoiduocxem = nguoiduocxem;
		this.nguoigiao = nguoigiao;
		this.mota = mota;
		this.tonghopbaocao = tonghopbaocao;
		this.tepdinhkem = tepdinhkem;
		this.url = url;
//		this.thaoluan = thaoluan;
	}

	public void setID(String id) {
		this.id = id;
	}

	public String getID() {
		return id;
	}

	public void setTenCongViec(String tencongviec) {
		this.tencongviec = tencongviec;
	}

	public String getTenCongViec() {
		return tencongviec;
	}

	public void setNgayBatDau(String ngaybatdau) {
		this.ngaybatdau = ngaybatdau;
	}

	public String getNgayBatDau() {
		return ngaybatdau;
	}

	public void setTinhTrang(String tinhtrang) {
		this.tinhtrang = tinhtrang;
	}

	public String getTinhTrang() {
		return tinhtrang;
	}

	public void setTienDo(String tiendo) {
		this.tiendo = tiendo;
	}

	public String getTienDo() {
		return tiendo;
	}

	public void setNguoiThucHien(String nguoithuchien) {
		this.nguoithuchien = nguoithuchien;
	}

	public String getNguoiThucHien() {
		return nguoithuchien;
	}

	public void setPhongBan(String phongban) {
		this.phongban = phongban;
	}

	public String getPhongBan() {
		return phongban;
	}

	public void setLoaiCongViec(String loaicongviec) {
		this.loaicongviec = loaicongviec;
	}

	public String getLoaiCongViec() {
		return loaicongviec;
	}

	public void setHanCuoi(String han_cuoi) {
		this.hancuoi = hancuoi;
	}

	public String getHanCuoi() {
		return hancuoi;
	}

	public void setDuAn(String duan) {
		this.duan = duan;
	}

	public String getDuAn() {
		return duan;
	}

	public void setMucUuTien(String mucuutien) {
		this.mucuutien = mucuutien;
	}

	public String getMucUuTien() {
		return mucuutien;
	}

	public void setNguoiDuocXem(String nguoiduocxem) {
		this.nguoiduocxem = nguoiduocxem;
	}

	public String getNguoiDuocXem() {
		return nguoiduocxem;
	}

	public void setNguoiGiao(String nguoigiao) {
		this.nguoigiao = nguoigiao;
	}

	public String getNguoiGiao() {
		return nguoigiao;
	}

	public void setMoTa(String mota) {
		this.mota = mota;
	}

	public String getMoTa() {
		return mota;
	}

	public void setTongHopBaoCao(String tonghopbaocao) {
		this.tonghopbaocao = tonghopbaocao;
	}

	public String getTongHopBaoCao() {
		return tonghopbaocao;
	}

	public void setTepDinhKem(String tepdinhkem) {
		this.tepdinhkem = tepdinhkem;
	}

	public String getTepDinhKem() {
		return tepdinhkem;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setThaoLuan(ArrayList<ThaoLuan> thaoluan) {
		this.thaoluan = thaoluan;
	}

	public ArrayList<ThaoLuan> getThaoLuan() {
		return thaoluan;
	}
}
