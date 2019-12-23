package com.job.network.entity

/**
 * @Author fox.hu
 * @Date 2019/11/29 9:49
 */
enum class DownloadState(val STATE: Int) {
    STATE_WAIT(0),
    STATE_START(1),
    STATE_STOP(2),
    STATE_RESUME(3),
    STATE_CANCEL(4),
    STATE_COMPLETE(5),
    STATE_FAILED(6),
    STATE_RUNNING(7)
}