# No qualifying bean of type 'org.springframework.core.convert.ConversionService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Qualifier(value=webFluxConversionService)}

我最近基于 Gradle 依赖管理构建了一个新 SpringCloud +Spring WebFlux应用程序，从start.spring下载项目模板之后。io时，我添加了一些第三方依赖并尝试启动应用程序。出现如下异常：

```java
Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.core.convert.ConversionService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {@org.springframework.beans.factory.annotation.Qualifier(value=webFluxConversionService)}
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.raiseNoMatchingBeanFound(DefaultListableBeanFactory.java:1662) ~[spring-beans-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1221) ~[spring-beans-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1175) ~[spring-beans-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:857) ~[spring-beans-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:760) ~[spring-beans-5.1.13.RELEASE.jar:5.1.13.RELEASE]
	... 110 common frames omitted
```

这个问题网上 命中率 很高，也很常见。解决很简单 ，排除项目中存在的 `org.springframework.boot:spring-boot-starter-web`依赖组件