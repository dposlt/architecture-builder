package dvoraka.archbuilder.template

import dvoraka.archbuilder.DirType
import dvoraka.archbuilder.Directory
import dvoraka.archbuilder.generate.Generator
import dvoraka.archbuilder.generate.JavaHelper
import dvoraka.archbuilder.generate.JavaTestingHelper
import dvoraka.archbuilder.generate.Utils
import dvoraka.archbuilder.service.DirService
import dvoraka.archbuilder.template.dir.MicroserviceTemplate
import dvoraka.archbuilder.test.microservice.data.message.RequestMessage
import dvoraka.archbuilder.test.microservice.server.AbstractServer
import dvoraka.archbuilder.test.microservice.service.BaseService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@Slf4j
@SpringBootTest
class MicroserviceTemplateISpec extends Specification implements JavaHelper, JavaTestingHelper {

    @Autowired
    Generator mainGenerator
    @Autowired
    DirService dirService


    def setup() {
    }

    def "test"() {
        expect:
            true
    }

    def "create microservice"() {
        given:
            String rootDirName = 'testing-service'
            String packageName = 'test.microservice'
            String serviceName = 'TestingService'

            MicroserviceTemplate template = new MicroserviceTemplate(
                    rootDirName,
                    packageName,
                    BaseService.class,
                    Collections.emptyList(),
                    serviceName,
                    AbstractServer.class,
                    RequestMessage.class,
            )
            Directory rootDir = template.getRootDirectory()

        when:
            mainGenerator.generate(template.getRootDirectory())

        then:
            exists(DirType.SERVICE, rootDir, dirService)
            exists(DirType.SERVICE_ABSTRACT, rootDir, dirService)
            exists(DirType.SERVICE_IMPL, rootDir, dirService)

            exists(DirType.BUILD_CONFIG, rootDir, dirService)
            exists(DirType.SRC_PROPERTIES, rootDir, dirService)

        when:
            Directory serviceImplDir = dirService.findByType(DirType.SERVICE_IMPL, rootDir)
                    .get()
            Directory serviceDir = dirService.findByType(DirType.SERVICE, rootDir)
                    .get()
            Class<?> serviceImplClass = loadClass(defaultServiceImplName(serviceDir))

        then:
            serviceImplClass.getSimpleName() == 'Default' + serviceName
            serviceImplClass.getName() == defaultServiceImplName(serviceDir)

        cleanup:
            Utils.removeFiles(rootDirName)
            true
    }
}
