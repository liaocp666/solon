package org.noear.solon.cloud.service;

import org.noear.solon.cloud.CloudEventHandler;
import org.noear.solon.cloud.annotation.EventLevel;
import org.noear.solon.cloud.exception.CloudEventException;
import org.noear.solon.cloud.model.Event;
import org.noear.solon.cloud.model.EventTran;
import org.noear.solon.data.tran.TranUtils;

/**
 * 云端事件服务（事件总线服务）
 *
 * @author noear
 * @since 1.2
 */
public interface CloudEventService {
    /**
     * 新建事务
     */
    default EventTran newTran(){
        return new EventTran();
    }

    /**
     * 新建事务并加入`@Tran`注解管理
     */
    default EventTran newTranAndJoin() {
        EventTran tran = newTran();
        TranUtils.listen(tran);
        return tran;
    }

    /**
     * 发布事件
     *
     * @param event 事件
     */
    boolean publish(Event event) throws CloudEventException;

    /**
     * 关注事件（相当于订阅）
     *
     * @param level    事件级别
     * @param channel  通道
     * @param group    分组
     * @param topic    主题
     * @param tag      标签
     * @param qos      服务质量
     * @param observer 观察者
     */
    void attention(EventLevel level, String channel, String group, String topic, String tag, int qos, CloudEventHandler observer);
}