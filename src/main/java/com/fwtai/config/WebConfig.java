package com.fwtai.config;

import com.fwtai.tool.ToolString;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 配置拦截器|Tomcat服务器配置|路由配置|请求的后缀名
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018年1月9日 15:27:18
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@SpringBootConfiguration
public class WebConfig extends WebMvcConfigurerAdapter{

    /**把拦截器添加进去*/
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new AuthInterceptor());
    }

    /**设置请求路径及jsp页面路径*/
    @Override
    public void addViewControllers(final ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("login");//解释:请求路径:/login;返回响应的jsp页面路径:/WEB-INF/view/login.jsp,注意必须在拦截器或配置让其可以访问或放行!
        registry.addViewController("/").setViewName("index");//默认显示的jsp页面是/WEB-INF/view/index.jsp,如果配置了resources/index.html或static/index.html都会走这个html页面,配置的优先权大于默认值,默认是resources/index.html或static/index.html
        super.addViewControllers(registry);
    }

    /**错误页设置、配置*/
    @Bean
    public EmbeddedServletContainerFactory servletContainer(){
        final TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setUriEncoding(Charset.forName("UTF-8"));
        final ErrorPage code404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error/404.html");
        final ErrorPage code500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error/500.html");
        final ErrorPage agrs = new ErrorPage(IllegalArgumentException.class,"/error/agrs.html");
        final Set<ErrorPage> set = new HashSet<ErrorPage>();
        set.add(code404);
        set.add(code500);
        set.add(agrs);
        tomcat.setErrorPages(set);
        return tomcat;
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter(){
        final StringHttpMessageConverter converter = new StringHttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(new MediaType("text","plain",Charset.forName("UTF-8"))));
        return converter;
    }

    /**
     * 文件上传临时存储路径?处理文件上传的路径问题
    */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        final String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        String fileDir = "";
        //使用jar运行项目
        if(path.contains("jar!/")){
            final int index = path.lastIndexOf(".jar!/");
            final String dir_upload = path.substring(0,index).substring(6);
            fileDir = dir_upload.substring(0,dir_upload.lastIndexOf('/'));
        }
        final MultipartConfigFactory factory = new MultipartConfigFactory();
        if(ToolString.isLinuxOS()){
            factory.setLocation("/"+fileDir);
        }else{
            factory.setLocation(fileDir);
        }
        return factory.createMultipartConfig();
    }

    /**默认的请求后缀名,启用此bean则无法上传文件!*/
    //@Bean
    public ServletRegistrationBean dispatcherRegistration(final DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean reg = new ServletRegistrationBean(dispatcherServlet);
        reg.getUrlMappings().clear();
        reg.addUrlMappings("/");
        reg.addUrlMappings("*.fwtai");
        reg.addUrlMappings("*.do");
        return reg;
    }
}