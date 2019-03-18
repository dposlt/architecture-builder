package dvoraka.archbuilder.module;

import dvoraka.archbuilder.data.Directory;
import dvoraka.archbuilder.sample.microservice.data.BaseException;
import dvoraka.archbuilder.sample.microservice.data.ResultData;
import dvoraka.archbuilder.sample.microservice.data.message.RequestMessage;
import dvoraka.archbuilder.sample.microservice.data.message.ResponseMessage;
import dvoraka.archbuilder.sample.microservice.net.BaseNetComponent;
import dvoraka.archbuilder.sample.microservice.net.ServiceNetComponent;
import dvoraka.archbuilder.sample.microservice.net.receive.NetReceiver;
import dvoraka.archbuilder.sample.microservice.server.AbstractServer;
import dvoraka.archbuilder.sample.microservice.service.BaseService;
import dvoraka.archbuilder.springconfig.SpringConfigGenerator;
import dvoraka.archbuilder.template.NetTemplateConfig;

import java.util.Collections;

public class MicroserviceType1Template implements Module {

    private Directory root;


    public MicroserviceType1Template(
            String rootDirName,
            String packageName,
            String serviceName,
            SpringConfigGenerator configGenerator
    ) {
        NetTemplateConfig netTemplateConfig = new NetTemplateConfig(
                ResultData.class,
                BaseException.class,
                RequestMessage.class,
                ResponseMessage.class,
                ServiceNetComponent.class,
                NetReceiver.class,
                BaseNetComponent.class,
                AbstractServer.class
        );

        DefaultMicroservice template = new DefaultMicroservice(
                rootDirName,
                packageName,
                BaseService.class,
                Collections.emptyList(),
                serviceName,
                netTemplateConfig,
                configGenerator
        );

        root = template.getRootDirectory();
    }

    @Override
    public Directory getRootDirectory() {
        return root;
    }
}