package cn.saberking.jvav.apm.framework.aspectj;

import cn.saberking.jvav.apm.common.annotation.DataSource;
import cn.saberking.jvav.apm.common.utils.StringUtils;
import cn.saberking.jvav.apm.framework.datasource.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 多数据源处理
 *
 * @author apm
 */
@Aspect
@Order(1)
@Component
public class DataSourceAspect {
//    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(cn.saberking.jvav.apm.common.annotation.DataSource)"
            + "|| @within(cn.saberking.jvav.apm.common.annotation.DataSource)")
    public void dsPointCut() {

    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (StringUtils.isNotNull(dataSource)) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}
