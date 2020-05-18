package com.silver.fox.base.component.viewmodel

import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.silver.fox.ext.logw
import com.silver.fox.viewext.isMainThread
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 * 用于只会被处理一次的事件
 * 值得注意的是，如果添加多个observer，只会有一个随机的通知内容的改变
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending =
        AtomicBoolean(false)


    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            "Multiple observers registered but only one will be notified of changes.".logw("SingleLiveEvent")
        }
        // Observe the internal MutableLiveData
        super.observe(owner, Observer { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    override fun observeForever(observer: Observer<in T>) {
        if (hasActiveObservers()) {
            "Multiple observers registered but only one will be notified of changes.".logw("SingleLiveEvent")
        }
        // Observe the internal MutableLiveData
        super.observeForever { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        if (isMainThread) {
            super.setValue(t)
        } else {
            super.postValue(t)
        }
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}