package com.zhaisoft.lib.updater.util;

public class Constant {
	public static final int MAX_VALUE = 0x000fffff;
	
	public static final int JsonTest = 1;	
	public final static String KeyAccount = "account";
	public final static String KeyUserID = "UserId";
	public final static String KeyDeviceID = "UserDeviceId";
	
	public final static String KeyIsLogined = "UserIsLogin";
	public final static String KeyPassword = "UserPassword";
	public final static String KeyLocationCityID = "LocationCityId";
	public final static String KeySelectedCityID = "SelectedCityId";
	public final static String KeyCityRv = "CityRv"; 
	public final static String KeySelectedCityName = "SelectedCityName";
	public final static String KeyRegionProvince = "regionProvince";
	public final static String KeyShareMessage = "sharemessage";
	public static final String KEY_NOTIFICATION = "PrefNotification";
	
	public final static String DatabaseName = "exiuge.db";
	public static final int DB_VERSION = 1;
	
	
	public static interface DataType {
		public static final int BASE = 1000;
		public static final int InitData = BASE+1;		
		public static final int RegetToken = BASE +2;
		public static final int RegetUser = BASE + 3;
		public static final int OUpgrade = BASE + 4;

		public static final int GetUserSMSCode = BASE + 10;
		
		public static final int RegistWithUUID = BASE + 30;
		
		
		public static final int CheckVersion = BASE+20; 
		public static final int UserMessageCenter = BASE+21; 
		
		public static final int getAllRegino = BASE + 161;
		public static final int GetRegionProvince = BASE + 165;
	}
		
	/**
	 * //2000-2099
	 * @author Eagle
	 *
	 */
	public static interface ScaleType {
		public static final int BASE = 2000;
		public static final int Normal = BASE+1; 
		public static final  int ThumNail = BASE+2; 
		public static final  int SmallGallery = BASE+3;
		public static final  int MiddleGallery = BASE+4;
		public static final  int LargeGallery = BASE+5;
		public static final  int QuarterGallery = BASE+6;
		public static final  int Card = BASE+7;
		public static final  int ThumCard = BASE+8;
		public static final  int Forth =BASE+9;
		public static final  int Fifth = BASE+10;
		public static final  int OrderGallery = BASE+11;
		public static final  int NormalNoSample = BASE+12;
		public static final  int Advertisement640_180 = BASE+13; 
		public static final  int Advertisement640_120 = BASE+14; 
	}
	
	public static interface SMSType {
		public static final  int Register = 0;
		public static final  int Change = 1;
	}
}
