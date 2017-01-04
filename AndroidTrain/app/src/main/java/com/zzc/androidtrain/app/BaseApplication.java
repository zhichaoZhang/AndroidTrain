package com.zzc.androidtrain.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * 真正的Application
 * <p>
 * 其实现交由BaseApplicationDelegate处理
 * <p>
 * Created by zczhang on 16/12/18.
 */

public class BaseApplication extends TinkerApplication {

    public BaseApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.zzc.androidtrain.app.BaseApplicationDelegate", "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
