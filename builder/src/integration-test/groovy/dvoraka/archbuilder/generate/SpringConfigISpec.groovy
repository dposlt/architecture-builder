package dvoraka.archbuilder.generate


import dvoraka.archbuilder.DirType
import dvoraka.archbuilder.Directory
import dvoraka.archbuilder.sample.SimpleClass
import dvoraka.archbuilder.springconfig.BeanMapping
import dvoraka.archbuilder.springconfig.BeanParameter
import dvoraka.archbuilder.springconfig.SpringConfigGenerator
import org.springframework.beans.factory.annotation.Autowired

import java.util.function.Supplier

class SpringConfigISpec extends BaseISpec {

    @Autowired
    Generator mainGenerator
    @Autowired
    SpringConfigGenerator springConfigGenerator


    def "Spring config"() {
        given:
            Directory abs = new Directory.DirectoryBuilder('test')
                    .type(DirType.ABSTRACT)
                    .parent(srcBase)
                    .typeClass(SimpleClass.class)
                    .build()
            Directory ext = new Directory.DirectoryBuilder('config')
                    .type(DirType.IMPL)
                    .parent(srcBase)
                    .superType(abs)
                    .filename('TestSimpleClass')
                    .build()

            // parameters
            BeanParameter parameter = new BeanParameter()
            parameter.setTypeDir(ext)
            parameter.setName("param1")

            // mappings
            String body = 'return new $T()'
            BeanMapping mapping = new BeanMapping()
            mapping.setTypeDir(abs)
            mapping.setName('getBean')
            mapping.addParameter(parameter)
            mapping.setCode(body)

            List<BeanMapping> beanMappings = new ArrayList<>()
            beanMappings.add(mapping)

            Supplier<String> callback = {
                return springConfigGenerator.genConfiguration(beanMappings)
            }

            Directory configuration = new Directory.DirectoryBuilder('config')
                    .type(DirType.SPRING_CONFIG)
                    .parent(srcBase)
                    .filename('SpringConfig')
                    .textSupplier(callback)
                    .build()
        when:
            mainGenerator.generate(root)
        then:
            notThrown(Exception)
    }
}
