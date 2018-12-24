package com.bj.hmxxparents.api;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bj.hmxxparents.utils.AppUtils;
import com.bj.hmxxparents.utils.BitmapUtils;
import com.bj.hmxxparents.utils.LL;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/2.
 */
public class HttpUtilService {

    //正式版
    public static final String BASE_URL = "http://xixin.gamepku.com/";
    public static final String BASE_RESOURCE_URL = "http://xixin.gamepku.com/files/";
    public static final String BASE_FILES_UPLOAD_URL = "http://xixin.gamepku.com/";

    //测试版
//    public static final String BASE_URL = "http://testxixin.gamepku.com/";
//    public static final String BASE_RESOURCE_URL = "http://testxixin.gamepku.com/files/";
//    public static final String BASE_FILES_UPLOAD_URL = "http://testxixin.gamepku.com/";

    public static final String BASE_API_URL = BASE_URL + "index.php/";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/EduParents/";

    public static String getBaseURL() {
        return BASE_API_URL + "MobileSpecialRelateAppList.aspx";
    }

    public static String getJsonByUrl(String parseUrl) throws Exception {
        String url = BASE_API_URL + parseUrl;
        String result = getJsonBycompletelyUrl(url);
        return result;
    }

    public static String getWxPayJsonByUrl(String parseUrl) throws Exception {
        String url = BASE_URL + parseUrl;
        String result = getJsonBycompletelyUrl(url);
        return result;
    }

    public static String getJsonByPostUrl(String parseUrl, HashMap<String, String> params) throws Exception {
        String url = BASE_API_URL + parseUrl;
        String result = postParamsByUrl(url, params);

        return result;
    }

    public static String getJsonByPostCompleteUrl(String parseUrl, HashMap<String, String> params) throws Exception {
        String url = parseUrl;
        String result = postParamsByUrl(url, params);
        return result;
    }

    public static String getJsonBycompletelyUrl(String parseUrl) throws Exception {
        String url = parseUrl;
        LL.i("API接口地址: " + url);
//        Response response = OkHttpUtils.get().url(url).build().execute();

        Response response = OkHttpUtils.post().url(url)
                .addParams("appkey", MLConfig.HTTP_APP_KEY)
                .build()
                .execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
        String result = response.body().string();
        LL.i("API_Result: " + result);
        return result;
    }

    public static String postParamsByUrl(String parseUrl, HashMap<String, String> params) throws Exception {
        String url = parseUrl;
        LL.i("API接口地址: " + url);

        PostFormBuilder builder = OkHttpUtils.post().url(url);
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addParams(key, value);
        }
        builder.addParams("appkey", MLConfig.HTTP_APP_KEY);

        Response response = builder
                .build()
                .execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
        String result = response.body().string();
        LL.i("API_Result: " + result);
        return result;
    }

    public static String postContentByUrl(String parseUrl, String postContent) throws Exception {
        String url = BASE_API_URL + parseUrl;
        LL.i("API接口地址: " + url);
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream")
                , getData(postContent));
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return "1";
        } else {
            return "0";
        }
    }

    /**
     * 上传音频文件
     * 切换到正式版之后 要切换到 douhao.gamepku.com 下
     *
     * @param parseUrl
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String postFileByUrl(String parseUrl, String filePath) throws Exception {
        String url = BASE_FILES_UPLOAD_URL + parseUrl;
        File file = new File(filePath);
        LL.i("newFileSize:" + file.length() / 1024 + "KB");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        LL.i("API接口地址: " + url);
        Response response = OkHttpUtils.post()
                .addFile("userfile", fileName, file)
                .url(url)
                .addParams("appkey", "123456")
                .build()
                .execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
        // 上传成功后删除缓存文件
        file.delete();
        String result = response.body().string();
        LL.i("API_Result: " + result);
        return result;
    }

    /**
     * 上传图片，并返回图片的ID
     *
     * @param parseUrl
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String postPictureByUrl(String parseUrl, String filePath) throws Exception {
        String url = BASE_FILES_UPLOAD_URL + parseUrl;
        File file = new File(filePath);
        LL.i("newFileSize:" + file.length() / 1024 + "KB");
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        LL.i("API接口地址: " + url);
        Response response = OkHttpUtils.post()
                .addFile("userfile", fileName, file)
                .url(url)
                .addParams("appkey", MLConfig.HTTP_APP_KEY)
                .build()
                .execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code" + response);
        }
        // 上传成功后删除缓存文件
        BitmapUtils.deleteCacheFile();
        String result = response.body().string();
        LL.i("API_Result: " + result);
        return result;
    }

    /**
     * 获取字节流
     **/
    private static byte[] getData(String baseContent) {
        byte[] array = null;
        try {
            array = baseContent.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return array;
    }

    public static void downloadFile(final Context context, String downloadUrl, final String fileName) throws Exception {
        OkHttpUtils.get().url(downloadUrl).tag(fileName).build()
                .execute(new FileCallBack(DOWNLOAD_PATH, fileName) {
                    @Override
                    public void inProgress(float progress, long total) {
//                        LL.i("Download", "HttpUtilService -- DownloadFile() -- 下载中：" + progress);
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        LL.i("Download", "HttpUtilService -- DownloadFile() -- 下载失败");
                        File file = new File(DOWNLOAD_PATH, fileName);
                        file.delete();
                    }

                    @Override
                    public void onResponse(File response) {
                        LL.i("Download", "HttpUtilService -- DownloadFile() -- 下载完成：" + response.getPath());
                        Toast.makeText(context, "安装包下载完成", Toast.LENGTH_SHORT).show();
                        AppUtils.installAPK(context, response.getPath());
                    }
                });
    }

}
