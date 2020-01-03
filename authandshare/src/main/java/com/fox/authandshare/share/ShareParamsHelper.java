package com.fox.authandshare.share;

import android.os.Bundle;

/**
 * @author fox.hu
 * @date 2018/9/4
 */

public interface ShareParamsHelper<T> {

    Bundle createParams(ShareType type, T t);
}
