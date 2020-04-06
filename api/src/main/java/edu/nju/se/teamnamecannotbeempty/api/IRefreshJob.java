package edu.nju.se.teamnamecannotbeempty.api;

/**
 * 数据刷新接口
 */
public interface IRefreshJob {
    /**
     * 异步地启动数据刷新工作
     *
     * @前置条件 服务的提供方已经启动
     * @后置条件 服务提供方继续刷新数据至完成
     */
    void trigger();
}
