package com.geok.langfang.shuju;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.geok.langfang.tools.Tools;
import com.geok.langfang.util.ZipUtil;

import java.io.IOException;

/**
 * Created by ydb on 2018/4/4.
 */

public class SetMessage {
    // 压缩时用
    public static void setMessage(int arg, String result, int flaglogout, Handler handler) {
        Tools.isRun = false;
        //
        Message message = new Message();
        message.arg1 = arg;
        String str = "连不上服务器，请联系管理员";
        try {
            // String a = new String(result.getBytes("utf-8"), "utf-8");
            str = ZipUtil.uncompress(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putString("result", str);
        bundle.putInt("flag", flaglogout);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
