package com.zhai.utils;

import java.lang.reflect.Field;

import android.content.DialogInterface;

public class DialogUtil {
	public static void setDialogClose(DialogInterface dialog, boolean close) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, close); // flas表示不关闭
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
