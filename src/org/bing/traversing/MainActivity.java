package org.bing.traversing;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMapLongClickListener, LocationListener {
	
	private String mMockProviderName = LocationManager.GPS_PROVIDER;
	private Thread thread;
	private LocationManager locationManager;
	private Boolean is_run = true;
	
	// ��Խλ��
	private double latitude = 31.3029742;
	private double longitude = 120.6097126;

	// �����ͼ�õ���λ��
	private double click_latitude = 31.3029742;
	private double click_longitude = 120.6097126;

	// ��ͼ���
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	BitmapDescriptor bdA = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_marka);

	// �ؼ����
	TextView textLatitudeValue;
	TextView textLongitudeValue;
	Button btnSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setOnMapLongClickListener(this);

		textLatitudeValue = (TextView) findViewById(R.id.textLatitudeValue);
		textLongitudeValue = (TextView) findViewById(R.id.textLongitudeValue);
		btnSet = (Button) findViewById(R.id.btnSet);
		
		btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				latitude = click_latitude;
				longitude = click_longitude;
				
				Toast.makeText(MainActivity.this, "������:" + latitude + "," + longitude, Toast.LENGTH_SHORT).show();;
			}
		});
		
		init_location();
		// �����̣߳�һֱ�޸�GPS����
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (is_run) {
					try {
						Thread.sleep(500);
						setLocation(longitude, latitude);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			}
		});
		thread.start();
		
		
	}
	
	@SuppressLint("NewApi")
	private void setLocation(double longitude, double latitude) {
		Location location = new Location(mMockProviderName);
		location.setTime(System.currentTimeMillis());
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(2.0f);
		location.setAccuracy(3.0f);
		location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
		locationManager.setTestProviderLocation(mMockProviderName, location);
	}
	
	/**
	 * inilocation ��ʼ�� λ��ģ��
	 * 
	 */
	private void init_location() {
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		locationManager.addTestProvider(mMockProviderName, false, true, false, false, true, true, true, 0, 5);
		locationManager.setTestProviderEnabled(mMockProviderName, true);
		locationManager.requestLocationUpdates(mMockProviderName, 0, 0, this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
		mBaiduMap = null;
		bdA.recycle();
		is_run = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// ����ϴε�mark���
		mBaiduMap.clear();
		mBaiduMap.hideInfoWindow();

		// ���õ�ǰ�������
		click_latitude = point.latitude;
		click_longitude = point.longitude;

		textLatitudeValue.setText(String.valueOf(click_latitude));
		textLongitudeValue.setText(String.valueOf(click_longitude));

		// �ڵ�ͼ����ʾmark��Ϣ
		MarkerOptions option = new MarkerOptions().icon(bdA).position(point);
		mBaiduMap.addOverlay(option);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}
