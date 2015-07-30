package com.sicco.task.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sicco.erp.model.Dispatch;
import com.sicco.erp.util.AccentRemover;
import com.sicco.erp.util.Utils;

public class Task implements Serializable {
	private String id;
	private String ten_cong_viec;
	private String nguoi_thuc_hien;
	private String du_an;
	private String tien_do;
	private String ngay_bat_dau;
	private String ngay_ket_thuc;
	private String nguoi_giao;
	private String dinh_kem;
	private String phong_ban;
	private String mo_ta;
	private String code;
	private String id_du_an;
	private String id_phong_ban;
	private Context context;

	private ArrayList<Task> data;

	public Task(Context context) {
		this.context = context;
	}

	public Task(String id, String ten_cong_viec, String nguoi_thuc_hien,
			String du_an, String tien_do, String ngay_bat_dau,
			String ngay_ket_thuc, String nguoi_giao, String dinh_kem,
			String phong_ban, String mo_ta, String code, String id_du_an,
			String id_phong_ban) {
		super();
		this.id = id;
		this.ten_cong_viec = ten_cong_viec;
		this.nguoi_thuc_hien = nguoi_thuc_hien;
		this.du_an = du_an;
		this.tien_do = tien_do;
		this.ngay_bat_dau = ngay_bat_dau;
		this.ngay_ket_thuc = ngay_ket_thuc;
		this.nguoi_giao = nguoi_giao;
		this.dinh_kem = dinh_kem;
		this.phong_ban = phong_ban;
		this.mo_ta = mo_ta;
		this.code = code;
		this.id_du_an = id_du_an;
		this.id_phong_ban = id_phong_ban;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTen_cong_viec() {
		return ten_cong_viec;
	}

	public void setTen_cong_viec(String ten_cong_viec) {
		this.ten_cong_viec = ten_cong_viec;
	}

	public String getNguoi_thuc_hien() {
		return nguoi_thuc_hien;
	}

	public void setNguoi_thuc_hien(String nguoi_thuc_hien) {
		this.nguoi_thuc_hien = nguoi_thuc_hien;
	}

	public String getDu_an() {
		return du_an;
	}

	public void setDu_an(String du_an) {
		this.du_an = du_an;
	}

	public String getTien_do() {
		return tien_do;
	}

	public void setTien_do(String tien_do) {
		this.tien_do = tien_do;
	}

	public String getNgay_bat_dau() {
		return ngay_bat_dau;
	}

	public void setNgay_bat_dau(String ngay_bat_dau) {
		this.ngay_bat_dau = ngay_bat_dau;
	}

	public String getNgay_ket_thuc() {
		return ngay_ket_thuc;
	}

	public void setNgay_ket_thuc(String ngay_ket_thuc) {
		this.ngay_ket_thuc = ngay_ket_thuc;
	}

	public String getNguoi_giao() {
		return nguoi_giao;
	}

	public void setNguoi_giao(String nguoi_giao) {
		this.nguoi_giao = nguoi_giao;
	}

	public String getDinh_kem() {
		return dinh_kem;
	}

	public void setDinh_kem(String dinh_kem) {
		this.dinh_kem = dinh_kem;
	}

	public String getPhong_ban() {
		return phong_ban;
	}

	public void setPhong_ban(String phong_ban) {
		this.phong_ban = phong_ban;
	}

	public String getMo_ta() {
		return mo_ta;
	}

	public void setMo_ta(String mo_ta) {
		this.mo_ta = mo_ta;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ArrayList<Task> getData() {
		return data;
	}

	public void setData(ArrayList<Task> data) {
		this.data = data;
	}

	public String getId_du_an() {
		return id_du_an;
	}

	public void setId_du_an(String id_du_an) {
		this.id_du_an = id_du_an;
	}

	public String getId_phong_ban() {
		return id_phong_ban;
	}

	public void setId_phong_ban(String id_phong_ban) {
		this.id_phong_ban = id_phong_ban;
	}

	// get data
	public ArrayList<Task> getData(final Context context, String url,
			OnLoadListener OnLoadListener) {

		this.onLoadListener = OnLoadListener;
		onLoadListener.onStart();
		data = new ArrayList<Task>();
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("user_id", Utils.getString(context, "user_id"));

		client.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				String jsonRead = response.toString();

				Log.d("LuanDT", "json: " + jsonRead);
				if (!jsonRead.isEmpty()) {
					try {
						JSONObject object = new JSONObject(jsonRead);
						JSONArray rows = object.getJSONArray("row");
						for (int i = 0; i < rows.length(); i++) {
							JSONObject row = rows.getJSONObject(i);
							String id = row.getString("id");
							String ten_cong_viec = row
									.getString("ten_cong_viec");
							String nguoi_thuc_hien = row
									.getString("nguoi_thuc_hien");
							String du_an = row.getString("du_an");
							String tien_do = row.getString("tien_do");
							String ngay_bat_dau = row.getString("ngay_bat_dau");
							String ngay_ket_thuc = row
									.getString("ngay_ket_thuc");
							String nguoi_giao = row.getString("nguoi_giao");
							String dinh_kem = row.getString("dinh_kem");
							String phong_ban = row.getString("phong_ban");
							String mo_ta = row.getString("mo_ta");
							String code = row.getString("code");
							String id_du_an = row.getString("id_du_an");
							String id_phong_ban = row.getString("id_phong_ban");

							dinh_kem = dinh_kem.replace(" ", "%20");

							data.add(new Task(id, ten_cong_viec,
									nguoi_thuc_hien, du_an, tien_do,
									ngay_bat_dau, ngay_ket_thuc, nguoi_giao,
									dinh_kem, phong_ban, mo_ta, code, id_du_an,
									id_phong_ban));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				onLoadListener.onSuccess();
				super.onSuccess(statusCode, headers, response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				super.onFailure(statusCode, headers, throwable, errorResponse);
				onLoadListener.onFalse();
			}
		});
		return data;
	}
	
	//search
	public ArrayList<Task> search(String k, ArrayList<Task> data) {
		ArrayList<Task> result = new ArrayList<Task>();
		String vReplace = AccentRemover.getInstance(context).removeAccent(k);
		if (!data.isEmpty()) {
			if (k.length() <= 0) {
				return data;
			} else {
				for (Task task : data) {

					String iReplace = AccentRemover.getInstance(context)
							.removeAccent(task.getTen_cong_viec());
					
					if (iReplace.toLowerCase().contains(vReplace.toLowerCase())) {
						result.add(task);
					}
				}
			}
		}
		return result;
	}

	public interface OnLoadListener {
		void onStart();

		void onSuccess();

		void onFalse();
	}

	private OnLoadListener onLoadListener;
}
