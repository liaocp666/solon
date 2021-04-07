package org.noear.solon.cloud.extend.aws.s3;

import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudManager;
import org.noear.solon.cloud.extend.aws.s3.service.CloudFileServiceImp;
import org.noear.solon.core.Plugin;

/**
 * @author noear 2021/4/7 created
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(SolonApp app) {
        if (Utils.isEmpty(S3Props.instance.getFileBucket())) {
            return;
        }

        if (S3Props.instance.getFileEnable()) {
            CloudManager.register(new CloudFileServiceImp());
        }
    }
}
