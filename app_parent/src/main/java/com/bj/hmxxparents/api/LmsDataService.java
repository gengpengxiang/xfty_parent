package com.bj.hmxxparents.api;

import android.util.Log;

import com.bj.hmxxparents.entity.AppVersionInfo;
import com.bj.hmxxparents.entity.ArticleInfo;
import com.bj.hmxxparents.entity.BadgeType;
import com.bj.hmxxparents.entity.BadgeTypeInfo;
import com.bj.hmxxparents.entity.BaseDataInfo;
import com.bj.hmxxparents.entity.ClassItemInfo;
import com.bj.hmxxparents.entity.ClassNewsInfo;
import com.bj.hmxxparents.entity.CommentInfo;
import com.bj.hmxxparents.entity.KidClassInfo;
import com.bj.hmxxparents.entity.KidDataInfo;
import com.bj.hmxxparents.entity.OrderInfo;
import com.bj.hmxxparents.entity.ReportInfo;
import com.bj.hmxxparents.entity.StudHobbyCategory;
import com.bj.hmxxparents.entity.StudentHobbyInfo;
import com.bj.hmxxparents.entity.SubjectInfo;
import com.bj.hmxxparents.entity.TradeInfo;
import com.bj.hmxxparents.utils.LL;
import com.bj.hmxxparents.utils.StringUtils;
import com.douhao.game.entity.ChallengeInfo;
import com.douhao.game.entity.LevelInfo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseConversation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by he on 2016/11/24.
 */
public class LmsDataService {

    private static final int PAGE_SIZE = 10;

    public LmsDataService() {
    }

    /**
     * 获取学生的姓名，学校，班级
     *
     * @param kidId
     * @return
     * @throws Exception
     */
    public KidClassInfo getKidClassInfoFromAPI(String kidId) throws Exception {
        KidClassInfo kidClassInfo = new KidClassInfo();
        String parseUrl = "student/index/" + kidId;
        LL.i("获取学生信息：" + parseUrl);
        JSONArray resultArray = new JSONArray(HttpUtilService.getJsonByUrl(parseUrl));
        if (resultArray.length() == 0) {
            kidClassInfo.setErrorCode("0");
            kidClassInfo.setMessage("没有找到您的娃");
            return kidClassInfo;
        }

        JSONObject result = resultArray.getJSONObject(0);
        // 成功的情况下
        kidClassInfo.setKidId(result.optString("pid"));
        kidClassInfo.setKidName(result.optString("name"));
        kidClassInfo.setKidImg(StringUtils.isEmpty(result.optString("img")) ? "" : HttpUtilService.BASE_RESOURCE_URL + result.optString("img"));
        kidClassInfo.setSchoolId(result.optString("school_id"));
        kidClassInfo.setSchoolName(result.optString("school_name"));
        kidClassInfo.setClassId(result.optString("class_id"));
        kidClassInfo.setClassName(result.optString("class_name"));

        kidClassInfo.setErrorCode("1");
        kidClassInfo.setMessage("成功");

        return kidClassInfo;
    }

    /**
     * 新的获取学生信息的接口
     *
     * @param kidId
     * @return
     * @throws Exception
     */
    public KidClassInfo getKidClassInfoFromAPI2(String kidId) throws Exception {
        KidClassInfo kidClassInfo = new KidClassInfo();
        String parseUrl = "jz/getstudentinfo";
        LL.i("获取学生信息：" + parseUrl);
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", kidId);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        Log.e("获取学生信息getstudentinfo",result);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
//            JSONArray dataArray = new JSONArray(data);
//            if (dataArray.length() == 0) {
//                kidClassInfo.setErrorCode("0");
//                kidClassInfo.setMessage("学生数据为空");
//                return kidClassInfo;
//            }

            JSONObject dataObj = new JSONObject(data);

            // 成功的情况下
            kidClassInfo.setKidId(dataObj.optString("student_code"));
            kidClassInfo.setKidName(dataObj.optString("student_name"));
            kidClassInfo.setKidImg(StringUtils.isEmpty(dataObj.optString("student_img")) ? "" : HttpUtilService.BASE_RESOURCE_URL + dataObj.optString("student_img"));
            kidClassInfo.setSchoolId(dataObj.optString("schoolcode"));
            kidClassInfo.setSchoolName(dataObj.optString("schoolname"));
            kidClassInfo.setSchoolImg(StringUtils.isEmpty(dataObj.optString("schoolimg")) ? "" : HttpUtilService.BASE_RESOURCE_URL + dataObj.optString("schoolimg"));
            kidClassInfo.setClassId(dataObj.optString("class_code"));
            kidClassInfo.setClassName(dataObj.optString("class_name"));
            kidClassInfo.setKidGender(dataObj.optString("student_xingbie"));
            kidClassInfo.setKidBirthday(dataObj.optString("student_shengri"));

            kidClassInfo.setTianyuan(dataObj.optString("tianyuan"));

            kidClassInfo.setErrorCode("1");
            kidClassInfo.setMessage("成功");
        } else {
            kidClassInfo.setErrorCode("0");
            kidClassInfo.setMessage(errorMsg);
        }

        return kidClassInfo;
    }

    /**
     * 获取验证码
     *
     * @return
     * @throws Exception
     */
    public String[] getCodeFromAPI(String phoneNumber) throws Exception {
        String[] result = new String[2];
        String parseUrl = HttpUtilService.BASE_URL + "dayumg/sendMsg.php?phone=" + phoneNumber;
        LL.i("获取验证码：" + parseUrl);
        JSONObject resultObject = new JSONObject(HttpUtilService.getJsonBycompletelyUrl(parseUrl));
        if (resultObject.has("result")) {
            String errorCode = resultObject.optString("result");
            String messaage = resultObject.optString("error");
            result[0] = errorCode;
            result[1] = messaage;
        } else {
            result[0] = "0";
            result[1] = "数据异常，请重试";
        }
        return result;
    }

    public String[] getCodeFromAPI2(String phoneNumber) throws Exception {
        String[] result = new String[2];
        String parseUrl = HttpUtilService.BASE_URL + "dayumg/jzsendmsg.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phoneNumber);

        LL.i("获取验证码：" + parseUrl);
        JSONObject resultObject = new JSONObject(HttpUtilService.getJsonByPostCompleteUrl(parseUrl, params));

        result[0] = resultObject.optString("ret");
        result[1] = resultObject.optString("msg");

        return result;
    }

    /**
     * 登录
     *
     * @return
     * @throws Exception
     */
    public String[] loginFromAPI(String phoneNumber, String code) throws Exception {
        String[] result = new String[2];
        String parseUrl = "jiazhang/login/" + phoneNumber + "/" + code;
        LL.i("登录：" + parseUrl);
        JSONObject resultObject = new JSONObject(HttpUtilService.getJsonByUrl(parseUrl));

        if (resultObject.has("result")) {
            String errorCode = resultObject.optString("result");
            String messaage = resultObject.optString("error");
            result[0] = errorCode;
            result[1] = messaage;
        } else {
            result[0] = "0";
            result[1] = "数据异常，请重试";
        }
        return result;
    }

    public String[] loginFromAPI2(String phoneNumber, String code) throws Exception {
        String[] result = new String[2];
        String parseUrl = "jz/login";
        HashMap<String, String> params = new HashMap<>();
        params.put("jzphone", phoneNumber);
        params.put("yzm", code);

        LL.i("登录：" + parseUrl);
        JSONObject resultObject = new JSONObject(HttpUtilService.getJsonByPostUrl(parseUrl, params));

        result[0] = resultObject.optString("ret");
        result[1] = resultObject.optString("msg");

        return result;
    }

    /**
     * 检查是否关联学生
     *
     * @return
     * @throws Exception
     */
    public String[] checkRelationKidFromAPI(String phoneNumber) throws Exception {
        String[] result = new String[3];
        String parseUrl = "jiazhang/getpslink/" + phoneNumber;
        LL.i("检查是否关联学生：" + parseUrl);
        String resultStr = HttpUtilService.getJsonByUrl(parseUrl);

        Log.e("账号是否关联学生",resultStr);

        JSONArray resultArray = new JSONArray(resultStr);
        if (resultArray.length() == 0) {
            result[0] = "2";
            result[1] = "没有关联学生";
            return result;
        }
        JSONObject resultObject = resultArray.getJSONObject(0);
        if (resultObject.has("studentid")) {
            result[0] = "1";
            result[1] = resultObject.optString("studentid");
            result[2] = resultObject.optString("juese");
        } else {
            result[0] = "0";
            result[1] = "数据异常，请重试";
            result[2] = "";
        }
        return result;
    }


    /**
     * 关联学生
     *
     * @return
     * @throws Exception
     */
    public String[] relationKidFromAPI(String phoneNumber, String kidId) throws Exception {
        String[] result = new String[2];
        String parseUrl = "jiazhang/linkstudent/" + phoneNumber + "/" + kidId;
        LL.i("关联学生：" + parseUrl);
        JSONObject resultObject = new JSONObject(HttpUtilService.getJsonByUrl(parseUrl));

        String abc = HttpUtilService.getJsonByUrl(parseUrl);
        Log.e("abc",abc);

        if (resultObject.has("result")) {
            String errorCode = resultObject.optString("result");
            String messaage = resultObject.optString("error");
            result[0] = errorCode;
            result[1] = messaage;
        } else {
            result[0] = "0";
            result[1] = "数据异常，请重试";
        }
        return result;
    }

    /**
     * 上传学生图片
     *
     * @param kidId
     * @param filePath
     * @return
     * @throws Exception
     */
    public String[] uploadKidPhoto(String kidId, String filePath) throws Exception {
        String[] result = new String[3];
        String parseUrl = "files/index/" + kidId;
        LL.i("上传学生图片：" + parseUrl);
        String resultStr = HttpUtilService.postPictureByUrl(parseUrl, filePath);

        Log.e("上传图片",resultStr);
        JSONObject resultObj = new JSONObject(resultStr);

        if (resultObj.has("affected_rows")) {
            String errorCode = resultObj.optString("affected_rows");
            result[0] = errorCode;
            if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
                result[1] = StringUtils.isEmpty(resultObj.optString("img")) ? "" : HttpUtilService.BASE_RESOURCE_URL + resultObj.optString("img");
            } else {
                result[1] = "上传失败";
            }
        } else {
            result[0] = "0";
            result[1] = "上传失败";
        }
        result[2] = filePath;
        return result;
    }

    /**
     * 上传语音评测文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public String[] uploadISEVoiceFile(String filePath) throws Exception {
        String[] result = new String[3];
        String parseUrl = "files/tzsfileupload";
        LL.i("上传语音文件：" + parseUrl);
        String resultStr = HttpUtilService.postFileByUrl(parseUrl, filePath);
        JSONObject resultObj = new JSONObject(resultStr);

        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;

        if (!StringUtils.isEmpty(errorCode) && "1".equals(errorCode)) {
            JSONObject dataObj = new JSONObject(data);
            result[2] = dataObj.optString("imgpath", "wu");
        } else {
            result[2] = "wu";
        }
        return result;
    }

    public String getQRCodeFormWeb(String qrCode) throws Exception {
        String resultStr = HttpUtilService.getJsonBycompletelyUrl(qrCode);
        String code = resultStr.substring(resultStr.indexOf("var jump_url=") + 14,
                resultStr.indexOf("window.location.href") - 4);
        LL.i("解析结果：" + code);
        String result = HttpUtilService.getJsonBycompletelyUrl(code);
        String resultCode = result.substring(result.indexOf("cliinserthtml") + 15,
                result.indexOf("cliinserthtml") + 39);
        if (!StringUtils.checkQRCode(resultCode)) {
            resultCode = result.substring(result.indexOf("data-original-title=\"\">") + 23,
                    result.indexOf("data-original-title=\"\">") + 47);
        }
        LL.i("解析结果：" + resultCode);

        return resultCode;
    }

    /**
     * 计分
     *
     * @param kidId
     * @param code
     * @return
     * @throws Exception
     */
    public String[] addKidScore(String kidId, String code) throws Exception {
        String[] result = new String[4];
        String parseUrl = "scoring/index/" + kidId + "/" + code;
        LL.i("计分：" + parseUrl);
        String resultStr = HttpUtilService.getJsonByUrl(parseUrl);
        JSONObject resultObj = new JSONObject(resultStr);
        if (resultObj.has("status")) {
            result[0] = "1";
            result[1] = resultObj.optString("status");
            result[2] = resultObj.optString("type");
            result[3] = resultObj.optString("value");
        } else {
            result[0] = "0";
            result[1] = "卡片数据有误";
        }
        return result;
    }

    /**
     * 新版本扫描徽章的接口
     *
     * @param kidId
     * @param code
     * @return
     * @throws Exception
     */
    public String[] addKidBadge(String kidId, String code) throws Exception {
        String[] result = new String[4];

        String parseUrl = "jz/scoring";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", kidId);
        params.put("qrcode", code);
        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        LL.i("计分：" + parseUrl);

        Log.e("扫描徽章二维码",resultStr);

        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONObject resultDataObj = new JSONObject(data);
            result[2] = resultDataObj.optString("type");
            result[3] = resultDataObj.optString("value");
        }

        return result;
    }

    /**
     * 新版本的获取学生数据的接口
     *
     * @param studentid
     * @return
     * @throws Exception
     */
    public KidDataInfo getStudentDataFromAPI(String studentid) throws Exception {
        String parseUrlClassScore = "jz/getdata";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentid);

        String result = HttpUtilService.getJsonByPostUrl(parseUrlClassScore, params);

        Log.e("学生数据",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        KidDataInfo student = new KidDataInfo();
        student.setErrorCode(errorCode);
        student.setMessage(errorMsg);

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONObject resultDataObj = new JSONObject(data);

            student.setScore(resultDataObj.optString("dianzan"));
            student.setBadge(resultDataObj.optString("huizhang"));
            student.setGrade(resultDataObj.optString("dengji"));
            student.setPingyu(resultDataObj.optString("pingyu"));
            student.setBadgePro(resultDataObj.optString("zhuanxiang"));
            student.setUpdateTime(resultDataObj.optString("updatetime"));
        }
        return student;
    }

    /**
     * 新版本获取学生所有动态的接口
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getStudentAllNewsFromAPI(String studentId, String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/zxdt";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e(" 新版本获取学生所有动态的接口",result);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType(itemObj.optString("dongtai_type"));
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    item.setNewsDesc(itemObj.optString("huizhang_shuoming", ""));
                    item.setTeacherPhone(itemObj.optString("teacher_phone"));

                    item.setXueke(itemObj.optString("xueke"));
                    item.setHuode_num(itemObj.optInt("huode_num"));

                    item.setXueke_img(itemObj.optString("xueke_img"));

                    dataList.add(item);

                    item = null;
                }
            }
        }

        return dataList;
    }

    public BadgeTypeInfo getStudentBadgesNumberFromAPI(String studentId, String pageIndex) throws Exception {
        String parseUrl = "jz/huizhang";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        JSONObject data = resultObj.optJSONObject("data").optJSONObject("num");

        BadgeTypeInfo badgeTypeInfo = new BadgeTypeInfo();
        badgeTypeInfo.setBadgeArtNum(Integer.valueOf(data.optString("yishu")));
        badgeTypeInfo.setBadgeBehaviorNum(Integer.valueOf(data.optString("pinde")));
        badgeTypeInfo.setBadgelanguageNum(Integer.valueOf(data.optString("yuyan")));
        badgeTypeInfo.setBadgescienceNum(Integer.valueOf(data.optString("kexue")));
        badgeTypeInfo.setBadgesportNum(Integer.valueOf(data.optString("yundong")));

        return badgeTypeInfo;
    }

    /**
     * 新版本获取学生徽章下的所有动态
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getStudentBadgeNewsFromAPI(String studentId, String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/huizhang";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optJSONObject("data").optString("content");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {

                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType("3");
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    item.setNewsDesc(itemObj.optString("huizhang_shuoming", ""));
                    dataList.add(item);

                    item = null;
                }
            }
        }

        return dataList;
    }

    public String getStudentCommendNumberFromAPI(String studentId, String pageIndex) throws Exception {
        String parseUrl = "jz/dianzan";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optJSONObject("data").optJSONObject("num").optString("dianzan");

        return data;
    }

    /**
     * 新版本获取学生点赞下的所有动态
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getStudentCommendNewsFromAPI(String studentId, String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/dianzan";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optJSONObject("data").optString("content");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType("z1");
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    dataList.add(item);

                    item = null;
                }
            }
        }

        return dataList;
    }

    /**
     * 感谢接口
     *
     * @param dongtaiid
     * @return
     * @throws Exception
     */
    public String[] getThanksTeacherResultFromAPI(String dongtaiid, String newsPosition) throws Exception {
        String[] result = new String[4];

        String parseUrl = "jz/ganxie";
        HashMap<String, String> params = new HashMap<>();
        params.put("dongtaiid", dongtaiid);
        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        LL.i("感谢：" + parseUrl);

        Log.e("感谢结果",resultStr);

        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;
        result[2] = data;
        result[3] = newsPosition;

        return result;
    }

    /**
     * 获取点赞下的各种理由有的情况
     *
     * @param studentId
     * @return
     * @throws Exception
     */
    public List<SubjectInfo> getClassCommendTypeFromAPI(String studentId,String dianzan_type) throws Exception {
        List<SubjectInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/sdznumber";
        HashMap<String, String> params = new HashMap<>();
        params.put("dianzan_type",dianzan_type);
        params.put("studentid", studentId);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("点赞或待改进类型",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optJSONObject("data").optString("z1");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray liyouArray = new JSONArray(data);
            SubjectInfo info;
            for (int i = 0; i < liyouArray.length(); i++) {
                JSONObject itemObj = liyouArray.optJSONObject(i);
                info = new SubjectInfo();
                info.setSubID(itemObj.optString("liyou_id"));
                info.setSubName(itemObj.optString("liyou_name"));
                info.setSubBadgeCount(Integer.valueOf(itemObj.optString("liyou_num")));
                dataList.add(info);
                info = null;
            }
        }
        return dataList;
    }


    /**
     * 获取点赞下的消息列表
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getClassCommendNewsFromAPI(String studentId, String reasonTypeID, String dianzan_type,String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/sdianzans";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("liyou", reasonTypeID);
        params.put("dianzan_type",dianzan_type);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("改进动态",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType(itemObj.optString("dongtai_type"));
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    item.setTeacherPhone(itemObj.optString("teacher_phone"));
                    dataList.add(item);

                    item = null;
                }
            }
        }
        return dataList;
    }

    /**
     * 获取专项下的各分类有的情况
     *
     * @param studentId
     * @return
     * @throws Exception
     */
    public List<SubjectInfo> getClassBadgeProTypeFromAPI(String studentId) throws Exception {
        List<SubjectInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/szxnumber";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optJSONObject("data").optString("8");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray liyouArray = new JSONArray(data);
            SubjectInfo info;
            for (int i = 0; i < liyouArray.length(); i++) {
                JSONObject itemObj = liyouArray.optJSONObject(i);
                info = new SubjectInfo();
                info.setSubID(itemObj.optString("xueke_id"));
                info.setSubName(itemObj.optString("xueke_name"));
                info.setSubBadgeCount(Integer.valueOf(itemObj.optString("xueke_num")));
                dataList.add(info);
                info = null;
            }
        }
        return dataList;
    }

    /**
     * 获取专项徽章下的消息列表
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getClassBadgeProNewsFromAPI(String studentId, String badgeProTypeID, String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/szhuanxiangs";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("hzxueke", badgeProTypeID);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType(itemObj.optString("dongtai_type"));
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    item.setNewsDesc(itemObj.optString("huizhang_shuoming", ""));
                    item.setTeacherPhone(itemObj.optString("teacher_phone"));
                    dataList.add(item);

                    item = null;
                }
            }
        }

        return dataList;
    }

    /**
     * 徽章类型列表
     *
     * @return
     * @throws Exception
     */
    public List<BadgeType> getClassBadgeTypeListFromAPI2(String studentId) throws Exception {
        List<BadgeType> badgeTypeList = new ArrayList<>();
        String parseUrl = "jz/shznumber";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("徽章类型列表",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray badgeTypeArray = new JSONArray(data);
            BadgeType badgeType;
            for (int i = 0; i < badgeTypeArray.length(); i++) {
                JSONObject badgeTypeItem = badgeTypeArray.optJSONObject(i);
                badgeType = new BadgeType();
                badgeType.setType_id(badgeTypeItem.optInt("type_id"));
                badgeType.setType_name(badgeTypeItem.optString("type_name"));

                badgeTypeList.add(badgeType);
                badgeType = null;
            }
        }

        return badgeTypeList;
    }

    public List<BadgeType> getClassBadgeTypeListFromAPI(String studentId) throws Exception {
        List<BadgeType> badgeTypeList = new ArrayList<>();
        String parseUrl = "jz/shznumber";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("徽章类型列表",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray badgeTypeArray = new JSONArray(data);
            BadgeType badgeType;
            SubjectInfo xuekeInfo;
            for (int i = 0; i < badgeTypeArray.length(); i++) {
                JSONObject badgeTypeItem = badgeTypeArray.optJSONObject(i);
                badgeType = new BadgeType();
                badgeType.setBadgeTypeID(badgeTypeItem.optString("type_id"));
                badgeType.setName(badgeTypeItem.optString("type_name"));
                JSONArray xuekeArray = badgeTypeItem.optJSONArray("type_xueke");
                List<SubjectInfo> xuekeList = new ArrayList<>();
                for (int j = 0; j < xuekeArray.length(); j++) {
                    JSONObject xuekeObj = xuekeArray.optJSONObject(j);
                    xuekeInfo = new SubjectInfo();
                    xuekeInfo.setSubID(xuekeObj.optString("xueke_id"));
                    xuekeInfo.setSubName(xuekeObj.optString("xueke_name"));
                    xuekeInfo.setSubBadgeCount(Integer.valueOf(xuekeObj.optString("xueke_num")));
                    xuekeList.add(xuekeInfo);
                    xuekeInfo = null;
                    if (j == 0) {
                        badgeType.setNumber(Integer.valueOf(xuekeObj.optString("xueke_num")));
                    }
                }
                badgeType.setXuekeList(xuekeList);
                badgeTypeList.add(badgeType);
                badgeType = null;
            }
        }

        return badgeTypeList;
    }

    /**
     * 获取徽章下的消息列表
     *
     * @param studentId
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ClassNewsInfo> getClassBadgeNewsFromAPI(String studentId, String badgeTypeId, String xuekeId, String pageIndex) throws Exception {
        List<ClassNewsInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/shuizhangs";
        Log.e("班级ID -- 徽章类型 -- 学科类型 : " ,studentId + " -- " + badgeTypeId + " -- " + xuekeId);
        HashMap<String, String> params = new HashMap<>();
        params.put("studentid", studentId);
        params.put("hztype", badgeTypeId);
        params.put("hzxueke", xuekeId);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e(" 获取徽章下的消息列表",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassNewsInfo item;
            if (pageIndex.equals("1") && newsArray.length() == 0) {
                item = ClassNewsInfo.newInstanceForEmptyView();
                dataList.add(item);
            } else {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject itemObj = newsArray.optJSONObject(i);
                    item = new ClassNewsInfo();

                    item.setNewsId(itemObj.optString("dongtai_id"));
                    item.setNewsPicture(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("dongtai_pic"));
                    item.setNewsType(itemObj.optString("dongtai_type"));
                    item.setNewsTitle(itemObj.optString("dongtai_title"));
                    item.setNewsTime(itemObj.optString("dongtai_time"));
                    item.setNewsThanksStatus(itemObj.optString("dongtai_ganxiestatus"));
                    item.setTeacherPic(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("teacher_pic"));
                    item.setTeacherName(itemObj.optString("teacher_name"));
                    item.setNewsDesc(itemObj.optString("huizhang_shuoming", ""));
                    item.setTeacherPhone(itemObj.optString("teacher_phone"));

                    item.setXueke(itemObj.optString("xueke"));
                    item.setHuode_num(itemObj.optInt("huode_num"));
                    item.setXueke_img(itemObj.optString("xueke_img"));
                    dataList.add(item);

                    item = null;
                }
            }
        }

        return dataList;
    }

    /**
     * 获取版本信息
     *
     * @param appVersion
     * @param appQudao
     * @return
     * @throws Exception
     */
    public AppVersionInfo checkNewVersion(String appVersion, String appQudao) throws Exception {
        AppVersionInfo versionInfo = new AppVersionInfo();
        LL.i("版本号：" + appVersion + " 渠道：" + appQudao);
        String parseUrl = "jz/version";
        HashMap<String, String> params = new HashMap<>();
        params.put("version", appVersion);
        params.put("qudao", appQudao);
        params.put("bao", MLConfig.KEY_APP_PKGNAME);
        params.put("type", MLConfig.KEY_APP_TYPE);
        params.put("os", MLConfig.KEY_APP_OS);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("版本结果",result);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        versionInfo.setErrorCode(errorCode);
        versionInfo.setMessage(errorMsg);

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")
                && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            versionInfo.setTitle(dataObj.optString("title"));
            versionInfo.setContent(dataObj.optString("content"));
            versionInfo.setDownloadUrl(dataObj.optString("url"));
            versionInfo.setQiangzhigengxin(dataObj.optString("qiangzhigengxin"));
        } else {

        }

        return versionInfo;
    }

    /**
     * 徽章类型列表
     *
     * @return
     * @throws Exception
     */
    public String[] getSchoolRankListStatusFromAPI(String schoolID) throws Exception {
        String[] resultArray = new String[3];
        String parseUrl = "jzhuoli/tixing";
        HashMap<String, String> params = new HashMap<>();
        params.put("schoolcode", schoolID);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        resultArray[0] = errorCode;
        resultArray[1] = errorMsg;

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")
                && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            resultArray[2] = dataObj.optString("tixing");
        } else {
            resultArray[2] = "0";
        }

        return resultArray;
    }

    /**
     * 获取全校活力榜的不同种类
     *
     * @return
     * @throws Exception
     */
    public List<BadgeType> getSchoolRankListTypesFromAPI(String schoolID) throws Exception {
        List<BadgeType> badgeTypeList = new ArrayList<>();
        String parseUrl = "jzhuoli/shuoming";
        HashMap<String, String> params = new HashMap<>();
        params.put("schoolcode", schoolID);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray badgeTypeArray = new JSONObject(data).optJSONArray("type");
            BadgeType badgeType;
            for (int i = 0; i < badgeTypeArray.length(); i++) {
                JSONObject badgeTypeItem = badgeTypeArray.optJSONObject(i);
                badgeType = new BadgeType();
                badgeType.setBadgeTypeID(badgeTypeItem.optString("typeid"));
                badgeType.setName(badgeTypeItem.optString("typename"));
                badgeTypeList.add(badgeType);
                badgeType = null;
            }
        }
        return badgeTypeList;
    }

    /**
     * 获取不同type下的活力榜学生
     *
     * @return
     * @throws Exception
     */
    public List<ClassItemInfo> getSchoolRankListByTypeFromAPI(String badgeTypeId, String schoolID) throws Exception {
        List<ClassItemInfo> dataList = new ArrayList<>();
        String parseUrl = "jzhuoli/huolibang";
        HashMap<String, String> params = new HashMap<>();
        params.put("type", badgeTypeId);
        params.put("schoolcode", schoolID);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray newsArray = new JSONArray(data);
            ClassItemInfo item;

            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject itemObj = newsArray.optJSONObject(i);
                item = new ClassItemInfo();

                item.setStudId(itemObj.optString("studentid"));
                item.setStudName(itemObj.optString("studentname"));
                item.setStudImg(HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("studentpic"));
                item.setStudPingyu(itemObj.optString("banji"));
                item.setStudBadge(itemObj.optString("huizhang"));
                item.setStudBadgePro(itemObj.optString("zhuanxiang"));
                item.setStudScore(itemObj.optString("dianzan"));
                item.setStudGrade(itemObj.optString("grade"));

                dataList.add(item);

                item = null;
            }
        }
        return dataList;
    }

    /**
     * 获取逗课列表
     *
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<ArticleInfo> getDouKeListFromAPI(int pageIndex) throws Exception {
        List<ArticleInfo> dataList = new ArrayList<>();
        String parseUrl = "jz/doukelist";
        HashMap<String, String> params = new HashMap<>();
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((pageIndex - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("逗课列表",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1")) {
            JSONArray dataArray = new JSONArray(data);
            ArticleInfo article;
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.optJSONObject(i);
                article = new ArticleInfo();
                article.setArticleID(item.optString("id"));
                article.setTitle(item.optString("title"));
                article.setContent(item.optString("content"));
                article.setAuthor(item.optString("author"));
                article.setPostTime(item.optString("time"));
                article.setArticlePicture(HttpUtilService.BASE_RESOURCE_URL + item.optString("img"));
                article.setAuthImg(HttpUtilService.BASE_RESOURCE_URL + item.optString("authorimg"));
                article.setArticlePath(item.optString("url"));
                article.setAuthDesc(item.optString("authorjianjie"));
                article.setReadNumber(item.getString("pageview"));
                article.setAgreeNumber(item.getString("dianzan"));
                article.setCommentNumber(item.optString("comment_num", "0"));

                article.setJianjie(item.optString("jianjie"));

                dataList.add(article);
                article = null;
            }
        }
        return dataList;
    }

    /**
     * 文章单次浏览记录 版本：2.1
     *
     * @param newsID
     * @param userPhoneNumber
     * @return
     * @throws Exception
     */
    public BaseDataInfo getArticleReadNumber(String newsID, String userPhoneNumber) throws Exception {
        BaseDataInfo dataInfo = new BaseDataInfo();
        String parseUrl = "jz/newspageview";
        HashMap<String, String> params = new HashMap<>();
        params.put("newsid", newsID);
        params.put("userphone", userPhoneNumber);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        dataInfo.setRet(errorCode);
        dataInfo.setMsg(errorMsg);
        dataInfo.setData(data);

        return dataInfo;
    }

    /**
     * 获取逗课详情
     *
     * @param newsID
     * @return
     * @throws Exception
     */
    public ArticleInfo getArticleInfoByID(String newsID) throws Exception {
        ArticleInfo info = new ArticleInfo();

        String parseUrl = "douke/getnewsbyid";
        HashMap<String, String> params = new HashMap<>();
        params.put("newsid", newsID);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("逗课详情",result);

        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (errorCode.equals("1") && !StringUtils.isEmpty(data)) {
            JSONObject item = new JSONObject(data);
            info.setArticleID(item.optString("id"));
            info.setTitle(item.optString("title"));
            info.setContent(item.optString("content"));
            info.setAuthor(item.optString("author"));
            info.setPostTime(item.optString("time"));
            info.setArticlePicture(HttpUtilService.BASE_RESOURCE_URL + item.optString("img"));
            info.setAuthImg(HttpUtilService.BASE_RESOURCE_URL + item.optString("authorimg"));
            info.setArticlePath(item.optString("url"));
            info.setAuthDesc(item.optString("authorjianjie"));
            info.setReadNumber(item.getString("pageview"));
            info.setAgreeNumber(item.getString("dianzan"));
            info.setCommentNumber(item.optString("comment_num", "0"));

            info.setJianjie(item.optString("jianjie"));
            info.setImg(item.optString("img"));
        }
        return info;
    }

    /**
     * 为逗课内容点赞或取消点赞 版本：2.1
     *
     * @param newsID
     * @param userPhoneNumber
     * @param type            点赞是1，取消点赞是2，查询是否已点过赞是3
     * @return
     * @throws Exception
     */
    public BaseDataInfo getArticleAgreeNumber(String newsID, String userPhoneNumber, String type) throws Exception {
        BaseDataInfo dataInfo = new BaseDataInfo();
        LL.i("逗课ID：" + newsID + " -- 手机号：" + userPhoneNumber + " -- type：" + type);
        String parseUrl = "jz/newsdianzan";
        HashMap<String, String> params = new HashMap<>();
        params.put("newsid", newsID);
        params.put("userphone", userPhoneNumber);
        params.put("dianzanadd", type);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        dataInfo.setRet(errorCode);
        dataInfo.setMsg(errorMsg);
        dataInfo.setData(data);

        return dataInfo;
    }

    /**
     * 创建虚拟学生
     *
     * @param userPhoneNumber
     * @return
     * @throws Exception
     */
    public KidClassInfo createVirtualStudent(String userPhoneNumber) throws Exception {
        KidClassInfo kidClassInfo = new KidClassInfo();
        String parseUrl = "jz/shiyongadd";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", userPhoneNumber);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (errorCode.equals("1") && !StringUtils.isEmpty(data)) {
            JSONObject infoObj = new JSONObject(data);
//            kidClassInfo.setKidName(infoObj.optString("name"));
//            kidClassInfo.setKidImg(StringUtils.isEmpty(infoObj.optString("img")) ?
//                    "" : HttpUtilService.BASE_RESOURCE_URL + infoObj.optString("img"));
            kidClassInfo.setKidId(infoObj.optString("pid"));
//            kidClassInfo.setClassId(infoObj.optString("classcode"));
        }
        return kidClassInfo;
    }

    /**
     * 删除虚拟学生的关联关系
     *
     * @param userPhoneNumber
     * @param kidID
     * @return
     * @throws Exception
     */
    public String[] deleteVirtualStudentRelation(String userPhoneNumber, String kidID) throws Exception {
        String[] resultArray = new String[3];

        String parseUrl = "jz/shiyongdel";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", userPhoneNumber);
        params.put("studentcode", kidID);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        resultArray[0] = errorCode;
        resultArray[1] = errorMsg;
        resultArray[2] = data;

        return resultArray;
    }

    /**
     * 获取学生的基本信息是否完整
     *
     * @return
     * @throws Exception
     */
    public KidClassInfo getStudentBaseInfoFromAPI(String phoneNumber) throws Exception {
        KidClassInfo info = new KidClassInfo();
        String parseUrl = "jzinfo/wanshanxinxi";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "select");

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (errorCode.equals("4") && !StringUtils.isEmpty(data)) {
            JSONArray resultArray = new JSONArray(data);
            info.setKidBirthday(resultArray.optJSONObject(0).optString("value"));
            info.setKidGender(resultArray.optJSONObject(1).optString("value"));
            info.setKidRelation(resultArray.optJSONObject(2).optString("value"));
        }
        info.setErrorCode(errorCode);
        info.setMessage(errorMsg);

        return info;
    }

    /**
     * @param phoneNumber
     * @param gender      性别，1是男，0是女
     * @param birthday
     * @param relation    家长角色，jiazhang，baba，mama三类
     * @return {"ret":"1","msg":"设置成功",
     * @throws Exception
     */
    public String[] setStudentBaseInfoFromAPI(String phoneNumber, String gender, String birthday,
                                              String relation) throws Exception {
        String[] resultArray = new String[2];
        String parseUrl = "jzinfo/wanshanxinxi";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "insert");
        params.put("xingbie", gender);
        params.put("shengri", birthday);
        params.put("juese", relation);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        resultArray[0] = errorCode;
        resultArray[1] = errorMsg;

        return resultArray;
    }

    /**
     * 修改学生兴趣列表
     *
     * @param phoneNumber
     * @param hobbies
     * @return
     * @throws Exception
     */
    public String[] setStudentHobbyFormAPI(String phoneNumber, String hobbies) throws Exception {
        String[] resultArray = new String[2];
        String parseUrl = "jzinfo/xingqu";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "insert");
        params.put("xingqu", hobbies);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");

        resultArray[0] = errorCode;
        resultArray[1] = errorMsg;

        return resultArray;
    }

    /**
     * 获取学生兴趣列表
     *
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    public List<StudHobbyCategory> getStudentHobbyFromAPI(String phoneNumber) throws Exception {
        List<StudHobbyCategory> dataList = new ArrayList<>();
        String parseUrl = "jzinfo/xingqu";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "select");

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (errorCode.equals("4") && !StringUtils.isEmpty(data)) {
            StudHobbyCategory category;
            StudentHobbyInfo hobbyInfo;
            JSONArray resultArray = new JSONArray(data);
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject categoryObj = resultArray.optJSONObject(i);
                category = new StudHobbyCategory();
                category.setTypeName(categoryObj.optString("typename"));

                List<StudentHobbyInfo> hobbyInfoList = new ArrayList<>();
                JSONArray hobbyArray = categoryObj.optJSONArray("xingqu_array");
                for (int j = 0; j < hobbyArray.length(); j++) {
                    hobbyInfo = new StudentHobbyInfo();
                    JSONObject hobbyObj = hobbyArray.optJSONObject(j);
                    hobbyInfo.setHobbyID(hobbyObj.optString("code"));
                    hobbyInfo.setHobbyName(hobbyObj.optString("name"));
                    hobbyInfo.setHobbyPic(StringUtils.isEmpty(hobbyObj.optString("img")) ?
                            "" : HttpUtilService.BASE_RESOURCE_URL + hobbyObj.optString("img"));
                    hobbyInfo.setHobbyShowType(StudentHobbyInfo.SHOW_TYPE_ITEM);
                    String isSelected = hobbyObj.optString("status");
                    hobbyInfo.setHobbyIsChecked(Boolean.valueOf(isSelected));
                    hobbyInfo.setHobbyCategory(hobbyObj.optString("typename"));
                    hobbyInfoList.add(hobbyInfo);
                    hobbyInfo = null;
                }
                category.setHobbyInfoList(hobbyInfoList);

                dataList.add(category);
                category = null;
            }
        }
        return dataList;
    }

    /**
     * 修改学生培训列表
     *
     * @param phoneNumber
     * @param trainings
     * @return
     * @throws Exception
     */
    public String[] setStudentTrainingFormAPI(String phoneNumber, String trainings) throws Exception {
        String[] resultArray = new String[2];
        String parseUrl = "jzinfo/peixun";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "insert");
        params.put("peixun", trainings);

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");

        resultArray[0] = errorCode;
        resultArray[1] = errorMsg;

        return resultArray;
    }

    /**
     * 查询培训记录
     *
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    public List<StudentHobbyInfo> getStudentTrainingFromAPI(String phoneNumber) throws Exception {
        List<StudentHobbyInfo> dataList = new ArrayList<>();

        String parseUrl = "jzinfo/peixun";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiazhangphone", phoneNumber);
        params.put("caozuo", "select");

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (errorCode.equals("4") && !StringUtils.isEmpty(data)) {
            StudentHobbyInfo hobbyInfo;
            JSONArray dataArray = new JSONArray(data);
            for (int i = 0; i < dataArray.length(); i++) {
                hobbyInfo = new StudentHobbyInfo();
                JSONObject hobbyObj = dataArray.optJSONObject(i);
                hobbyInfo.setHobbyID(hobbyObj.optString("code"));
                hobbyInfo.setHobbyName(hobbyObj.optString("name"));
                hobbyInfo.setHobbyPic(StringUtils.isEmpty(hobbyObj.optString("img")) ?
                        "" : HttpUtilService.BASE_RESOURCE_URL + hobbyObj.optString("img"));
                hobbyInfo.setHobbyShowType(StudentHobbyInfo.SHOW_TYPE_ITEM);
                String isSelected = hobbyObj.optString("status");
                hobbyInfo.setHobbyIsChecked(Boolean.valueOf(isSelected));
                dataList.add(hobbyInfo);
                hobbyInfo = null;
            }
        }
        return dataList;
    }

    /**
     * 保存会话记录
     *
     * @param teacherPhone
     * @param userPhone
     * @return
     * @throws Exception
     */
    public String[] saveConversationFromAPI(String teacherPhone, String userPhone) throws Exception {
        String[] result = new String[2];
        String parseUrl = "chat/chatloginsert";
        HashMap<String, String> params = new HashMap<>();
        params.put("jiaoshi", teacherPhone);
        params.put("jiazhang", userPhone);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");
        return result;
    }

    public List<EaseConversation> getConversationListFromAPI(String userPhone, int pageIndex) throws Exception {
        List<EaseConversation> dataList = new ArrayList<>();
        String parseUrl = "chat/userchatlogs";
        HashMap<String, String> params = new HashMap<>();
        params.put("userphone", userPhone);
        params.put("usertype", MLConfig.KEY_CONVERSATION_TYPE);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((pageIndex - 1) * PAGE_SIZE));

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);

        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && "1".equals(errorCode)) {
            JSONArray dataArray = new JSONArray(data);
            EaseConversation myConversation;
            EMConversation conversation;
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject itemObj = dataArray.optJSONObject(i);
                String teacherPhone = itemObj.optString("jiaoshi");
                String teacherName = itemObj.optString("jiaoshi_name");
                String teacherPic = HttpUtilService.BASE_RESOURCE_URL + itemObj.optString("jiaoshi_img");
                String relation = StringUtils.isEmpty(itemObj.optString("jiazhang_juese")) ? "" : "的" + itemObj.optString("jiazhang_juese");

                myConversation = new EaseConversation();
                String teacherEaseID = "jiaoshi" + teacherPhone;
                conversation = EMClient.getInstance().chatManager().getConversation(teacherEaseID, EMConversation.EMConversationType.Chat, true);
                myConversation.setEmConversation(conversation);
                myConversation.setUserEaseID(teacherEaseID);
                myConversation.setUserPhoto(teacherPic);
                myConversation.setUserNick(teacherName);
                myConversation.setRelation(relation);

                dataList.add(myConversation);
                conversation = null;
                myConversation = null;
            }
        }
        return dataList;
    }

    /**
     * 获取挑战赛相关信息
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ChallengeInfo getStudentChallengeInfoFromAPI(String studentcode) throws Exception {
        ChallengeInfo info = new ChallengeInfo();

        String parseUrl = "tiaozhansai/getuserscore";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);

        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && "1".equals(errorCode)) {
            JSONObject dataObj = new JSONObject(data);
            info.setChallengeNumber(Integer.valueOf(dataObj.optString("chapter", "0")));
            info.setChallengeScore(Integer.valueOf(dataObj.optString("fenshu", "0")));
            info.setChallengeRank(Integer.valueOf(dataObj.optString("paiming", "0")));
        } else if (!StringUtils.isEmpty(errorCode) && "2".equals(errorCode)) {
            // 没有数据
            info.setChallengeNumber(0);
            info.setChallengeScore(0);
            info.setChallengeRank(0);
        }

        return info;
    }

    /**
     * 根据学号查询分页关卡试题 版本：2.4 类型：jiazhang
     *
     * @param studentcode
     * @param chapter
     * @param limit
     * @return
     * @throws Exception
     */
    public List<LevelInfo> getNextLevelsFromAPI(String studentcode, int chapter, int limit) throws Exception {
        List<LevelInfo> dataList = new ArrayList<>();

        String parseUrl = "tiaozhansai/getshiti";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);
        params.put("chapter", String.valueOf(chapter));
        params.put("limit", String.valueOf(limit));
        params.put("offset", "0");

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);

        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && "1".equals(errorCode)) {
            JSONArray dataArray = new JSONArray(data);
            LevelInfo info;
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.optJSONObject(i);
                info = new LevelInfo();
                info.setLvlNumber(Integer.parseInt(item.optString("chapter")));
                info.setLvlContent(item.optString("word", ""));
                info.setLvlTrueReading(item.optString("pinyin", ""));
                info.setShitiCode(item.optString("shiticode"));
                dataList.add(info);
                info = null;
            }
        }
        return dataList;
    }

    /**
     * 挑战赛计分接口 版本：2.4 类型：jiazhang
     *
     * @param studentcode
     * @param chapter
     * @param shiticode
     * @param score
     * @param voicePath
     * @return
     * @throws Exception
     */
    public String[] saveLevelScoreFromAPI(String studentcode, int chapter, String shiticode,
                                          int score, String voicePath) throws Exception {
        String[] result = new String[3];

        String parseUrl = "tiaozhansai/scoring";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);
        params.put("chapter", String.valueOf(chapter));
        params.put("shiticode", shiticode);
        params.put("score", String.valueOf(score));
        params.put("uservoice", voicePath);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);

        String errorCode = resultObj.optString("ret", "");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;
        result[2] = data;

        return result;
    }

    /**
     * 是否显示捐助的入口
     *
     * @return
     * @throws Exception
     */
    public String[] getDonationStatusFromAPI(String type) throws Exception {
        String[] result = new String[3];

        String parseUrl = "pay";
        HashMap<String, String> params = new HashMap<>();
        params.put("appversion", type);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;
        result[2] = data;

        return result;
    }

    /**
     * 生成订单
     *
     * @param price
     * @return
     * @throws Exception
     */
    public OrderInfo getTheOrderInfoFromAPI(String price, String phoneNumber, String type) throws Exception {
        OrderInfo info = new OrderInfo();

        String parseUrl = "Wxpay/example/Testpay.php?price=" + price + "&phone=" + phoneNumber + "&paytype=" + type;
        String resultStr = HttpUtilService.getWxPayJsonByUrl(parseUrl);

        JSONObject resultObj = new JSONObject(resultStr);
        String resultCode = resultObj.optString("result_code", "");
        if ("SUCCESS".equals(resultCode)) {
            info.setAppid(resultObj.optString("appid"));
            info.setMch_id(resultObj.optString("mch_id"));
            info.setNonce_str(resultObj.optString("nonce_str"));
            info.setPrepay_id(resultObj.optString("prepay_id"));
            info.setResult_code(resultCode);
            info.setReturn_code(resultObj.optString("return_code"));
            info.setReturn_msg(resultObj.optString("return_msg"));
            info.setSign(resultObj.optString("sign"));
            info.setTrade_type(resultObj.optString("trade_type"));
            info.setTimeStamp(resultObj.optString("timeStamp"));
            info.setOut_trade_no(resultObj.optString("out_trade_no"));
        }
        return info;
    }

    /**
     * 查询订单交易状态
     *
     * @param tradeID
     * @return
     * @throws Exception
     */
    public TradeInfo getTheTradeInfoFromAPI(String tradeID) throws Exception {
        TradeInfo info = new TradeInfo();

        String parseUrl = "Wxpay/example/orderquery.php?out_trade_no=" + tradeID;
        String resultStr = HttpUtilService.getWxPayJsonByUrl(parseUrl);

        JSONObject resultObj = new JSONObject(resultStr);
        String resultCode = resultObj.optString("result_code", "");
        if ("SUCCESS".equals(resultCode)) {
            info.setResult_code(resultCode);
            info.setTrade_state(resultObj.optString("trade_state", ""));
        } else {
            info.setResult_code(resultCode);
        }

        return info;
    }

    /**
     * 获取逗课评论
     *
     * @param newsID
     * @param pageIndex
     * @return
     * @throws Exception
     */
    public List<CommentInfo> getDoukeAllCommentFromAPI(String newsID, String pageIndex) throws Exception {
        List<CommentInfo> dataList = new ArrayList<>();
        String parseUrl = "douke";
        HashMap<String, String> params = new HashMap<>();
        params.put("newsid", newsID);
        params.put("limit", String.valueOf(PAGE_SIZE));
        params.put("offset", String.valueOf((Integer.parseInt(pageIndex) - 1) * PAGE_SIZE));

        String result = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(result);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if (!StringUtils.isEmpty(errorCode) && errorCode.equals("1") &&
                !StringUtils.isEmpty(data)) {
            JSONArray newsArray = new JSONArray(data);
            CommentInfo item;
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject itemObj = newsArray.optJSONObject(i);
                item = new CommentInfo();

                item.setCommID(itemObj.optString("comment_id", ""));
                item.setCommCreaterName(itemObj.optString("user_title", ""));
                item.setCommCreaterPhoto(HttpUtilService.BASE_RESOURCE_URL +
                        itemObj.optString("user_img", ""));
                item.setCommCreateTime(itemObj.optString("createtime", ""));
                item.setCommContent(itemObj.optString("content", ""));
                item.setCommCreatePhone(itemObj.optString("userphone", ""));
                dataList.add(item);

                item = null;
            }
        }

        return dataList;
    }

    /**
     * 发送评论内容
     *
     * @param newsID
     * @param userPhoneNumber
     * @param userType
     * @param content
     * @return
     * @throws Exception
     */
    public String[] postDoukeCommentFromAPI(String newsID, String userPhoneNumber,
                                            String userType, String content) throws Exception {
        String[] result = new String[3];

        String parseUrl = "douke/setcomment";
        HashMap<String, String> params = new HashMap<>();
        params.put("newsid", newsID);
        params.put("userphone", userPhoneNumber);
        params.put("usertype", userType);
        params.put("content", content);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        result[0] = errorCode;
        result[1] = errorMsg;
        result[2] = data;

        return result;
    }

    /**
     * 获取查看学生报告的入口是否打开
     *
     * @param phoneNumber
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportBaseInfoFromAPI(String phoneNumber, String studentcode) throws Exception {
        ReportInfo baseInfo = new ReportInfo();

        String parseUrl = "jzbaogao/getpay";
        HashMap<String, String> params = new HashMap<>();
        params.put("jzphone", phoneNumber);
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            JSONObject schoolInfo = dataObj.optJSONObject("school_huiyuan");
            if (schoolInfo != null) {
                baseInfo.schoolReportState = schoolInfo.optString("baogaoopen", "0");
                String realPay = StringUtils.isEmpty(schoolInfo.optString("baogaopay", "0")) ? "0" : schoolInfo.optString("baogaopay", "0");
                baseInfo.reportRealPay = Integer.parseInt(realPay);
                String price = StringUtils.isEmpty(schoolInfo.optString("baogaoprice", "0")) ? "0" : schoolInfo.optString("baogaoprice", "0");
                baseInfo.reportPrice = Integer.parseInt(price);
            } else {
                baseInfo.schoolReportState = "0";
            }

            String userInfo = dataObj.optString("paydata");
            if (StringUtils.isEmpty(userInfo)) {
                baseInfo.isUserPaySuccess = false;
            } else {
                baseInfo.isUserPaySuccess = true;
            }
        } else {
            baseInfo.schoolReportState = "0";
        }

        return baseInfo;
    }

    /**
     * 获取第一页的数据
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportIndex1FromAPI(String studentcode) throws Exception {
        ReportInfo reportInfo = new ReportInfo();

        String parseUrl = "jzbaogao/index";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);

        Log.e("第一页数据",resultStr);

        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            JSONObject studentinfo = dataObj.optJSONObject("student_hz_dz");
            if (studentinfo != null) {
                reportInfo.userBadgeRank = studentinfo.optString("bpm", "");
                reportInfo.userBadgeCount = studentinfo.optString("badge", "0");
                reportInfo.userCommendRank = studentinfo.optString("dpm", "");
                reportInfo.userCommendCount = studentinfo.optString("value", "0");
            } else {
                throw new JSONException("studentinfo-返回结果为空");
            }

            JSONObject classInfo = dataObj.optJSONObject("class_avg");
            if (classInfo != null) {
                reportInfo.classBadgeAvg = classInfo.optString("bavg", "0");
                reportInfo.classCommendAvg = classInfo.optString("davg", "0");
            } else {
                throw new JSONException("classInfo-返回结果为空");
            }
        } else {
            throw new JSONException("返回结果为空");
        }

        return reportInfo;
    }

    /**
     * 获取第二页徽章分布图
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportIndex2FromAPI(String studentcode) throws Exception {
        ReportInfo reportInfo = new ReportInfo();

        String parseUrl = "jzbaogao/getbg2";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            JSONArray hzArray = dataObj.optJSONArray("hznum");
            LinkedHashMap<String, Integer> badgePieMap = new LinkedHashMap<>();
            if (hzArray != null) {
                for (int i = 0; i < hzArray.length(); i++) {
                    JSONObject itemObj = hzArray.getJSONObject(i);
                    String name = itemObj.optString("name");
                    String hznum = StringUtils.isEmpty(itemObj.optString("hznum", "0")) ? "0" : itemObj.optString("hznum", "0");
                    int number = Integer.parseInt(hznum);
                    badgePieMap.put(name, number);
                }
            } else {
                // throw new JSONException("hzArray-返回结果为空");
            }
            reportInfo.userBadgePieMap = badgePieMap;
        } else {
            throw new JSONException("返回结果为空");
        }
        return reportInfo;
    }

    /**
     * 获取第三页点赞分布图
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportIndex3FromAPI(String studentcode) throws Exception {
        ReportInfo reportInfo = new ReportInfo();

        String parseUrl = "jzbaogao/getbg3";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            JSONArray dzArray = dataObj.optJSONArray("dznum");
            LinkedHashMap<String, Integer> commendPieMap = new LinkedHashMap<>();
            if (dzArray != null) {
                for (int i = 0; i < dzArray.length(); i++) {
                    JSONObject itemObj = dzArray.optJSONObject(i);
                    String name = itemObj.optString("liyou");
                    String dznum = StringUtils.isEmpty(itemObj.optString("dznum", "0")) ? "0" : itemObj.optString("dznum", "0");
                    int number = Integer.parseInt(dznum);
                    commendPieMap.put(name, number);
                }
            } else {
                // throw new JSONException("hzArray-返回结果为空");
            }
            reportInfo.userCommendPieMap = commendPieMap;
        } else {
            throw new JSONException("返回结果为空");
        }
        return reportInfo;
    }

    /**
     * 获取第四页的统计数据
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportIndex4FromAPI(String studentcode) throws Exception {
        ReportInfo reportInfo = new ReportInfo();

        String parseUrl = "jzbaogao/getbg4";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONArray dataArray = new JSONArray(data);
            LinkedHashMap<Integer, Float> userBadgeLineMap = new LinkedHashMap<>();
            LinkedHashMap<Integer, Float> classBadgeLineMap = new LinkedHashMap<>();
            LinkedHashMap<Integer, Float> userCommendLineMap = new LinkedHashMap<>();
            LinkedHashMap<Integer, Float> classCommendLineMap = new LinkedHashMap<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.optJSONObject(i);
                int key = dataArray.length() - i;
                String datahz = StringUtils.isEmpty(item.optString("weekdata_hz", "0")) ? "0" : item.optString("weekdata_hz", "0");
                String avghz = StringUtils.isEmpty(item.optString("class_avg_hz", "0")) ? "0" : item.optString("class_avg_hz", "0");
                String datadz = StringUtils.isEmpty(item.optString("weekdata_dz", "0")) ? "0" : item.optString("weekdata_dz", "0");
                String avgdz = StringUtils.isEmpty(item.optString("class_avg_dz", "0")) ? "0" : item.optString("class_avg_dz", "0");

                Float userBadge = new BigDecimal(datahz).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                Float classBadge = new BigDecimal(avghz).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                Float userCommend = new BigDecimal(datadz).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
                Float classCommend = new BigDecimal(avgdz).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

                userBadgeLineMap.put(key, userBadge);
                classBadgeLineMap.put(key, classBadge);
                userCommendLineMap.put(key, userCommend);
                classCommendLineMap.put(key, classCommend);
            }
            reportInfo.userBadgeLineMap = userBadgeLineMap;
            reportInfo.classBadgeLineMap = classBadgeLineMap;
            reportInfo.userCommendLineMap = userCommendLineMap;
            reportInfo.classCommendLineMap = classCommendLineMap;
        } else {
            throw new JSONException("返回结果为空");
        }
        return reportInfo;
    }

    /**
     * 获取第五页的统计数据
     *
     * @param studentcode
     * @return
     * @throws Exception
     */
    public ReportInfo getReportIndex5FromAPI(String studentcode) throws Exception {
        ReportInfo reportInfo = new ReportInfo();

        String parseUrl = "jzbaogao/getbg5";
        HashMap<String, String> params = new HashMap<>();
        params.put("studentcode", studentcode);

        String resultStr = HttpUtilService.getJsonByPostUrl(parseUrl, params);
        JSONObject resultObj = new JSONObject(resultStr);
        String errorCode = resultObj.optString("ret");
        String errorMsg = resultObj.optString("msg");
        String data = resultObj.optString("data");

        if ("1".equals(errorCode) && !StringUtils.isEmpty(data)) {
            JSONObject dataObj = new JSONObject(data);
            JSONArray badgeArray = dataObj.optJSONArray("huizhang");
            List<ReportInfo.User> badgeList = new ArrayList<>();
            if (badgeArray != null) {
                ReportInfo.User user;
                for (int i = 0; i < badgeArray.length(); i++) {
                    JSONObject item = badgeArray.optJSONObject(i);
                    user = new ReportInfo.User();
                    user.username = item.optString("name");
                    user.photoPath = HttpUtilService.BASE_RESOURCE_URL + item.optString("img");
                    user.count = item.optString("badge");
                    user.pm = item.optString("pm");
                    badgeList.add(user);
                    user = null;
                }
            }
            reportInfo.badgeRankList = badgeList;

            JSONArray commendArray = dataObj.optJSONArray("dianzan");
            List<ReportInfo.User> commendList = new ArrayList<>();
            if (commendArray != null) {
                ReportInfo.User user;
                for (int i = 0; i < commendArray.length(); i++) {
                    JSONObject item = commendArray.optJSONObject(i);
                    user = new ReportInfo.User();
                    user.username = item.optString("name");
                    user.photoPath = HttpUtilService.BASE_RESOURCE_URL + item.optString("img");
                    user.count = item.optString("value");
                    user.pm = item.optString("pm");
                    commendList.add(user);
                    user = null;
                }
            }
            reportInfo.commendRankList = commendList;

        } else {
            throw new JSONException("返回结果为空");
        }

        return reportInfo;
    }


}