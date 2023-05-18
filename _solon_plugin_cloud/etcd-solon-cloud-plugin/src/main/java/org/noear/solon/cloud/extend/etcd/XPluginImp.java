package org.noear.solon.cloud.extend.etcd;

import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.CloudManager;
import org.noear.solon.cloud.CloudProps;
import org.noear.solon.cloud.extend.etcd.service.CloudConfigServiceEtcdImp;
import org.noear.solon.cloud.extend.etcd.service.CloudDiscoveryServiceEtcdImp;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

/**
 * @author luke
 * @since 2.2
 */
public class XPluginImp implements Plugin {

    CloudConfigServiceEtcdImp configServiceEtcdImp;
    CloudDiscoveryServiceEtcdImp discoveryServiceEtcdImp;

    @Override
    public void start(AopContext context) throws Throwable {
        CloudProps cloudProps = new CloudProps(context,"etcd");

        if (Utils.isEmpty(cloudProps.getServer())) {
            return;
        }

        //1.登记配置服务
        if (cloudProps.getConfigEnable()) {
            configServiceEtcdImp = new CloudConfigServiceEtcdImp(cloudProps);
            CloudManager.register(configServiceEtcdImp);

            //1.1.加载配置
            CloudClient.configLoad(cloudProps.getConfigLoad());
        }

        //2.登记发现服务
        if (cloudProps.getDiscoveryEnable()) {
            discoveryServiceEtcdImp = new CloudDiscoveryServiceEtcdImp(cloudProps);
            CloudManager.register(discoveryServiceEtcdImp);
        }
    }

    @Override
    public void stop() throws Throwable {
        if (configServiceEtcdImp != null) {
            configServiceEtcdImp.close();
        }

        if (discoveryServiceEtcdImp != null) {
            discoveryServiceEtcdImp.close();
        }
    }
}
