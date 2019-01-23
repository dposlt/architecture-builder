package dvoraka.archbuilder.template

import com.fasterxml.jackson.databind.ObjectMapper
import dvoraka.archbuilder.BuildTool
import dvoraka.archbuilder.DirType
import dvoraka.archbuilder.Directory
import dvoraka.archbuilder.GradleBuildTool
import dvoraka.archbuilder.generate.Generator
import dvoraka.archbuilder.generate.JavaHelper
import dvoraka.archbuilder.generate.JavaTestingHelper
import dvoraka.archbuilder.generate.Utils
import dvoraka.archbuilder.sample.microservice.data.message.RequestMessage
import dvoraka.archbuilder.sample.microservice.server.AbstractServer
import dvoraka.archbuilder.sample.microservice.service.BaseService
import dvoraka.archbuilder.service.DirService
import dvoraka.archbuilder.template.arch.MicroserviceTemplate
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Ignore
import spock.lang.Specification

@Slf4j
@SpringBootTest
class MicroserviceTemplateISpec extends Specification implements JavaHelper, JavaTestingHelper {

    @Autowired
    Generator mainGenerator
    @Autowired
    DirService dirService
    @Autowired
    ObjectMapper objectMapper


    def setup() {
    }

    def "test"() {
        expect:
            true
    }

    def "create micro-service"() {
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

        when:
            BuildTool buildTool = new GradleBuildTool(new File(rootDirName))
            buildTool.prepareEnv()

        then:
            notThrown(Exception)

        when:
            buildTool.build()

        then:
            notThrown(Exception)

        cleanup:
            Utils.removeFiles(rootDirName)
            true
    }

    def "micro-service directory serialization"() {
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
            String json = objectMapper.writeValueAsString(rootDir)
        then:
            notThrown(Exception)
    }

    @Ignore('WIP')
    def "micro-service directory deserialization"() {
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
            String json = objectMapper.writeValueAsString(rootDir)
        then:
            notThrown(Exception)
        when:
            Directory loadedDir = objectMapper.readValue(json, Directory)
        then:
            notThrown(Exception)
    }
}