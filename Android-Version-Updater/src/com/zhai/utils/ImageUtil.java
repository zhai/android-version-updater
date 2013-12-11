package com.zhai.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.widget.ImageView;

public class ImageUtil {
	public final static int MAX_WIDTH = 512;
	public final static int MAX_HEIGHT = 384;

	public static Bitmap getBitmapFromImageView(ImageView imageview) {
		imageview.setDrawingCacheEnabled(true);
		Bitmap bmp = Bitmap.createBitmap(imageview.getDrawingCache());
		imageview.setDrawingCacheEnabled(false);
		return bmp;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		try {
			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);

			Canvas canvas = new Canvas(bitmap);
			// canvas.setBitmap(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);

			return bitmap;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	public static Bitmap zoomBitmap(Bitmap bitmap, float zoom) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		// float scaleWidht = ((float) w / width);
		// float scaleHeight = ((float) h / height);
		matrix.postScale(zoom, zoom);
		// matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// 获得圆角图片的方法
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// 获得带倒影的图片方法
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * get reflection bitmap of the original bitmap.
	 * 
	 * @param srcBitmap
	 * @return
	 */
	public static Bitmap makeReflectionBitmap(Bitmap srcBitmap) {
		int bmpWidth = srcBitmap.getWidth();
		int bmpHeight = srcBitmap.getHeight();
		int[] pixels = new int[bmpWidth * bmpHeight * 4];
		srcBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);

		// get reversed bitmap
		Bitmap reverseBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
				Bitmap.Config.ARGB_8888);
		for (int y = 0; y < bmpHeight; y++) {
			reverseBitmap.setPixels(pixels, y * bmpWidth, bmpWidth, 0,
					bmpHeight - y - 1, bmpWidth, 1);
		}

		// get reflection bitmap based on the reversed one
		reverseBitmap.getPixels(pixels, 0, bmpWidth, 0, 0, bmpWidth, bmpHeight);
		Bitmap reflectionBitmap = Bitmap.createBitmap(bmpWidth, bmpHeight,
				Bitmap.Config.ARGB_8888);
		int alpha = 0x00000000;
		for (int y = 0; y < bmpHeight; y++) {
			for (int x = 0; x < bmpWidth; x++) {
				int index = y * bmpWidth + x;
				int r = (pixels[index] >> 16) & 0xff;
				int g = (pixels[index] >> 8) & 0xff;
				int b = pixels[index] & 0xff;

				pixels[index] = alpha | (r << 16) | (g << 8) | b;

				reflectionBitmap.setPixel(x, y, pixels[index]);
			}
			alpha = alpha + 0x01000000;
		}

		return reflectionBitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap Bytes2Bimap(byte[] b) {
		return Bytes2Bimap(b, false);
	}

	public static Bitmap Bytes2Bimap(byte[] b, boolean bUseNative) {
		if (b.length == 0) {
			return null;
		}
		if (bUseNative) {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			createNativeAllocOptions(newOpts);
			return BitmapFactory.decodeByteArray(b, 0, b.length, newOpts);
		} else
			return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	public static Bitmap decodeFile_new(String imageFile, int maxNumOfPixels) {
		return decodeFile_new(imageFile, maxNumOfPixels, false);
	}

	public static Bitmap decodeFile_new(String imageFile, int maxNumOfPixels,
			boolean bUseNative) {
		if (StringUtils.isNull(imageFile))
			return null;
		Bitmap bitmap = null;
		try {
			// FileDescriptor fd = new FileInputStream(imageFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			options.inDensity = options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;

			// BitmapFactory.decodeFileDescriptor(fd, null, options);

			BitmapFactory.decodeFile(imageFile, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}
			final int temp = options.outWidth * options.outHeight;
			if (temp < maxNumOfPixels)
				maxNumOfPixels = temp;

			options.inSampleSize = computeSampleSize(options, -1,
					maxNumOfPixels);
			options.inJustDecodeBounds = false;

			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			if (bUseNative)
				createNativeAllocOptions(options);

			// bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);

			bitmap = BitmapFactory.decodeFile(imageFile, options);

		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		} catch (Exception err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels) {
		return decodeFile(imageFile, maxNumOfPixels, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels,
			boolean bUseNative) {
		return decodeFile(imageFile, maxNumOfPixels, bUseNative, false, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels,
			boolean bUseNative, boolean bShare) {
		return decodeFile(imageFile, maxNumOfPixels, bUseNative, bShare, false);
	}

	public static Bitmap decodeFile(String imageFile, int maxNumOfPixels,
			boolean bUseNative, boolean bShare, boolean bDefaultDensity) {
		if (StringUtils.isNull(imageFile))
			return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inNativeAlloc = true;
		opts.inJustDecodeBounds = true;
		// DisplayUtil.getDisplayMetrics(act)
		if (bDefaultDensity)
			opts.inDensity = opts.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		BitmapFactory.decodeFile(imageFile, opts);
		final int temp = opts.outWidth * opts.outHeight;
		if (temp > 0 && temp < maxNumOfPixels || maxNumOfPixels == 0)
			maxNumOfPixels = temp;
		// opts.inSampleSize = computeSampleSize(opts, -1, 128 * 128);
		opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
		opts.inJustDecodeBounds = false;

		/**
		 * todo
		 */
		if (bUseNative) {
			createNativeAllocOptions(opts);
			if (bShare) {
				opts.inPurgeable = true;
				opts.inInputShareable = true;
			}
		} else {
			opts.inPurgeable = true;
			opts.inInputShareable = true;
		}

		try {
			return BitmapFactory.decodeFile(imageFile, opts);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	public static Bitmap readBitmap(Context context, int resId) {
		return readBitmap(context, resId, false);
	}

	public static Bitmap readBitmap(Context context, int resId,
			boolean bUseNative) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		if (bUseNative)
			ImageUtil.createNativeAllocOptions(opt);
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static int[] computeWH(String imageFile) {
		int[] wh = { 0, 0 };
		if (StringUtils.isNull(imageFile))
			return wh;
		try {
			FileDescriptor fd = new FileInputStream(imageFile).getFD();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(fd, null, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return wh;
			}
			wh[0] = options.outWidth;
			wh[1] = options.outHeight;
		} catch (Exception e) {
		}
		return wh;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap extractMiniThumb(Bitmap source, int width, int height,
			boolean recycle) throws Exception {
		if (source == null) {
			return null;
		}
		float scale;
		if (source.getWidth() > width && source.getHeight() > height) {
			if (source.getWidth() < source.getHeight()) {
				scale = width / (float) source.getWidth();
			} else {
				scale = height / (float) source.getHeight();
			}
		} else {
			scale = 1;
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap miniThumbnail = transform(matrix, source, width, height, true,
				recycle);
		return miniThumbnail;
	}

	public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp, boolean recycle)
			throws Exception {
		if (source == null || source.isRecycled())
			return null;
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			// c.drawColor(Color.WHITE);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();
		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;
		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}
		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}
		if (recycle && b1 != source) {
			source.recycle();
		}
		int dx1 = scaleUp ? 0 : Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = scaleUp ? 0 : Math.max(0, b1.getHeight() - targetHeight);
		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);
		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}
		return b2;
	}

	private static HashMap<String, SoftReference<Bitmap>> imageCache;

	public static Bitmap getBitmap4Cach(int id, Context context) {
		if (imageCache != null && imageCache.containsKey(id + "")) {
			SoftReference<Bitmap> softReference = imageCache.get(id + "");
			if (softReference != null && softReference.get() != null
					&& !softReference.get().isRecycled()) {
				return softReference.get();
			}
		}
		if (context != null) {
			if (imageCache == null)
				imageCache = new HashMap();
			Drawable d = context.getResources().getDrawable(id);
			Bitmap bt = ImageUtil.drawableToBitmap(d);
			SoftReference s = new SoftReference(bt);
			imageCache.put(id + "", s);
			return bt;
		}
		return null;
	}

	public final static int BITMAP_MAX_WIDTH = 512;
	public final static int BITMAP_MAX_HEIGHT = 512;

	public static Bitmap createBitmap(int w, int h, Bitmap.Config config) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(w, h, config);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 通过文件获取Bitmap
	 * 
	 * @param imageFile
	 * @return
	 */
	public static BitmapFactory.Options decodeFile(String imageFile) {
		if (StringUtils.isNull(imageFile))
			return null;
		BitmapFactory.Options opts = new BitmapFactory.Options();

		opts.inJustDecodeBounds = true;
		try {
			BitmapFactory.decodeFile(imageFile, opts);
			return opts;
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return null;
	}

	/**
	 * 缩放图书
	 * 
	 * @param drawable
	 * @return
	 */

	public static Bitmap ShrinkBitmap(String file, int width, int height) {
		return ShrinkBitmap(file, width, height, false, false);
	}

	public static Bitmap ShrinkBitmap(String file, int width, int height,
			boolean bUseNative) {
		return ShrinkBitmap(file, width, height, bUseNative, false);
	}

	public static Bitmap ShrinkBitmap(String file, int width, int height,
			boolean bUseNative, boolean bShare) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		bmpFactoryOptions.inDensity = bmpFactoryOptions.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
		Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		int heightRatio = width > 0 ? (int) FloatMath
				.ceil(bmpFactoryOptions.outHeight / (float) height) : 1;
		int widthRatio = height > 0 ? (int) FloatMath
				.ceil(bmpFactoryOptions.outWidth / (float) width) : 1;

		if (heightRatio > 1 || widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		if (bUseNative) {
			createNativeAllocOptions(bmpFactoryOptions);
		}
		if (bShare) {
			bmpFactoryOptions.inPurgeable = true;
			bmpFactoryOptions.inInputShareable = true;
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		// bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

		try {
			return bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
		}
		return bitmap;
	}

	public static void createNativeAllocOptions(BitmapFactory.Options options) {
		if (options == null)
			return;
		// options.inNativeAlloc = true;
		try {
			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(
					options, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 通过网络得到Bitmap

	public static Bitmap GetNetBitmap(String url) {

		return GetNetBitmap(url, 4 * 1024);
	}

	public static Bitmap GetNetBitmap(String url, int IO_BUFFER_SIZE) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out, IO_BUFFER_SIZE);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void copy(InputStream in, OutputStream out,
			int IO_BUFFER_SIZE) throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

}
