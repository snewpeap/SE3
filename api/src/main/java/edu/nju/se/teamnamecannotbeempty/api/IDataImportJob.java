package edu.nju.se.teamnamecannotbeempty.api;

/**
 * 数据导入服务接口
 */
public interface IDataImportJob {
    /**
     * 启动数据导入，返回的是将要导入的Paper条目数
     * 该方法返回时，数据导入并未完成
     *
     * @return 将要导入的Paper条目数
     * @前置条件 服务的提供方已经启动
     * @后置条件 服务提供方继续导入数据至完成
     */
    long trigger();
}
