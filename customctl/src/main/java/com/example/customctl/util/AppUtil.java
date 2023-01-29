package com.example.customctl.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.example.customctl.bean.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppUtil {
    //获取已安装的应用消息队列
    public static ArrayList<AppInfo> getAppInfo(Context ctx,int type){
        ArrayList<AppInfo> appList = new ArrayList<>();
        SparseIntArray siArray = new SparseIntArray();
        //获得应用包管理器
        PackageManager pm = ctx.getPackageManager();
        //获取系统中已经安装的应用列表
        List<ApplicationInfo> installList = pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (int i = 0; i < installList.size(); i++) {
            ApplicationInfo item = installList.get(i);
            //去除重复的应用信息
            if (siArray.indexOfKey(item.uid)>=0){
                continue;
            }
            //将应用编号添加到siArray中方便去重校验
            siArray.put(item.uid,1);
            //查看应用的权限列表，过滤出可以上网的应用
            try {
                String[] permissions = pm.getPackageInfo(item.packageName,
                        PackageManager.GET_PERMISSIONS).requestedPermissions;
                if (permissions == null) {
                    continue;
                }
                boolean isQueryNetwork = false;
                for (String permission : permissions) {
                    if (permission.equals("android.permission.INTERNET")){
                        isQueryNetwork = true;
                        break;
                    }
                }
                //类型为0表示所有应用，为1表示只要联网应用
                if(type == 0 || (type == 1 && isQueryNetwork)){
                    AppInfo app = new AppInfo();
                    app.uid = item.uid;
                    app.label = item.loadLabel(pm).toString();
                    app.package_name = item.packageName;
                    app.icon = item.loadIcon(pm);
                    appList.add(app);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }
        }
        return appList;
    }
    //填充应用的完整信息。主要有：1.补充应用的图标字段，2.将列表按照流量排序
    public static ArrayList<AppInfo> fillAppInfo(Context ctx,ArrayList<AppInfo> originArray){
        ArrayList<AppInfo> fullArray = (ArrayList<AppInfo>) originArray.clone();
        PackageManager pm = ctx.getPackageManager();
        //获取系统中已经安装的应用列表
        List<ApplicationInfo> installList = pm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        for (int i = 0; i < fullArray.size(); i++) {
            AppInfo app = fullArray.get(i);
            for (ApplicationInfo item : installList) {
                if(app.uid == item.uid){
                    //填充应用的图标信息。因为数据库没保存图标的位图，所以取出数据库记录之后还要补上图标数据
                    app.icon = item.loadIcon(pm);
                    break;
                }
            }
            fullArray.set(i,app);
        }
        //按流量大小降序排列
        Collections.sort(fullArray, new Comparator<AppInfo>() {
            @Override
            public int compare(AppInfo o1, AppInfo o2) {
                return (o1.traffic<o2.traffic)?1:-1;
            }
        });
        return fullArray;
    }
}
