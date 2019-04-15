package com.boonya.gis.algorithm;

public class GPS {
	private String strLat; // 标准的纬度
	private String strLon; // 标准的经度

	private double dDFLat; // 纬度。单位：度分
	private double dDFLon; // 经度。单位：度分

	private double dFLat; // 纬度。单位：分
	private double dFLon; // 经度。单位：分

	private double dDLat; // 纬度。单位：度
	private double dDLon; // 经度。单位：度

	private int iNetLat; // 纬度的网格坐标
	private int iNetLon; // 经度的网格坐标

	private boolean isValid; // GPS是否有效
	private boolean isNetValid; // 网格坐标是否有效

	public boolean isValid() {
		return isValid;
	}

	public boolean isNetValid() {
		return isNetValid;
	}

	public String getGpsCode() {
		return strLat + "," + strLon;
	}

	public GPS() {
		this.strLat = "";
		this.strLon = "";

		this.dDFLat = 0.0;
		this.dDFLon = 0.0;

		this.dFLat = 0.0;
		this.dFLon = 0.0;

		this.dDLat = 0.0;
		this.dDLon = 0.0;

		this.iNetLat = 0;
		this.iNetLon = 0;

		this.isValid = false;
		this.isNetValid = false;
	}

	public boolean setByGpsCode(String gpsCode) {
		if (null == gpsCode) {
			return false;
		}

		String[] temp = gpsCode.split(",");
		if (temp.length < 2) {
			return false;
		}
		if (this.setByStandardGps(temp[0], temp[1])) {
			isValid = true;
			return true;
		}

		return false;
	}

	public boolean setByStandardGps(String strLat, String strLon) {
		this.strLat = strLat;
		this.strLon = strLon;

		if (this.setStandardToDFCoordinate()
				&& this.setFCoordinateWithDFCoordinate()
				&& this.calcDCoordinate()) {
			this.isValid = true;
			return true;
		}

		return false;
	}

	public String getStrLat() {
		return strLat;
	}

	public String getStrLon() {
		return strLon;
	}

	public double getDFLat() {
		return dDFLat;
	}

	public double getDFLon() {
		return dDFLon;
	}

	public double getFLat() {
		return dFLat;
	}

	public double getFLon() {
		return dFLon;
	}

	/**
	 * 
	 * @return 纬度。单位：度
	 */
	public double getDLat() {
		return dDLat;
	}

	/**
	 * 
	 * @return 经度。单位：度
	 */
	public double getDLon() {
		return dDLon;
	}

	public int getNetLat() {
		return iNetLat;
	}

	public int getNetLon() {
		return iNetLon;
	}

	/**
	 * 标准 转 度分
	 * 
	 */
	private boolean setStandardToDFCoordinate() {
		if (strLat.length() != 10 || strLon.length() != 11) {
			return false;
		}

		try {
			String temp = strLat.substring(1);
			if (strLat.startsWith("N")) {
				this.dDFLat = Double.parseDouble(temp);
			} else if (strLat.startsWith("S")) {
				this.dDFLat = -Double.parseDouble(temp);
			} else {
				return false;
			}

			temp = strLon.substring(1);
			if (strLon.startsWith("E")) {
				this.dDFLon = Double.parseDouble(temp);
			} else if (strLon.startsWith("W")) {
				this.dDFLon = -Double.parseDouble(temp);
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}

		return true;
	}

	/**
	 * 度分 转 分
	 * 
	 */
	private boolean setFCoordinateWithDFCoordinate() {
		int iDLat = (int) (dDFLat / 100);
		int iDLon = (int) (dDFLon / 100);

		this.dFLat = iDLat * 60 + (dDFLat - iDLat * 100);
		this.dFLon = iDLon * 60 + (dDFLon - iDLon * 100);

		if (this.dFLat > 5400.0 || this.dFLat < -5400.0 || this.dFLon > 10800.0
				|| this.dFLon < -10800.0) {
			return false;
		}

		return true;
	}

	/**
	 * 计算以度为单位的GPS格式
	 */
	public boolean calcDCoordinate() {
		int iDLat = (int) (dDFLat / 100);
		int iDLon = (int) (dDFLon / 100);

		this.dDLat = (dDFLat - iDLat * 100) / 60 + iDLat;
		this.dDLon = (dDFLon - iDLon * 100) / 60 + iDLon;

		return true;
	}

	/**
	 * 计算网格坐标
	 * 
	 * @param referGPS
	 *            参照站点的GPS
	 */
	public boolean calcNetCoordinate(GPS referGPS) {
		if (!this.isValid) {
			return false;
		}

		double minLat = referGPS.getFLat() - 30;
		double minLon = referGPS.getFLon() - 30;
		this.iNetLat = (int) ((dFLat - minLat) / 0.0058);
		this.iNetLon = (int) ((dFLon - minLon) / 0.0053);
		isNetValid = true;

		return true;
	}

	/**
	 * 判断车辆是否在定点范围内
	 * 
	 * @param busLat
	 *            车辆纬度（例：N2312.1230）
	 * @param busLon
	 *            车辆经度（例：E11223.1230）
	 * @param siteLat
	 *            定点纬度（例：N2312.1230）
	 * @param siteLon
	 *            定点经度（例：E11223.1230）
	 * @param radius
	 *            定点半径
	 * @return true-是，false-否 例子isInSiteRange("N2312.1230", "E11223.1230",
	 *         "N2312.1230", "E11223.1230", 10);
	 */
	public static boolean isInSiteRange(String busLat, String busLon,
			String siteLat, String siteLon, int radius) {
		GPS busGps = new GPS();
		busGps.setByStandardGps(busLat, busLon);
		GPS siteGps = new GPS();
		siteGps.setByStandardGps(siteLat, siteLon);

		busGps.calcNetCoordinate(siteGps);
		siteGps.calcNetCoordinate(siteGps);
		// 网格坐标计算失败
		if (!busGps.isNetValid() || !busGps.isNetValid()) {
			return false;
		}

		int x1 = busGps.getNetLon();
		int y1 = busGps.getNetLat();
		int x2 = siteGps.getNetLon();
		int y2 = siteGps.getNetLat();
		int distance = (int) (Math.sqrt(Math.abs((x1 - x2) * (x1 - x2)
				+ (y1 - y2) * (y1 - y2))) * 10);

		return distance <= radius ? true : false;
	}

}
