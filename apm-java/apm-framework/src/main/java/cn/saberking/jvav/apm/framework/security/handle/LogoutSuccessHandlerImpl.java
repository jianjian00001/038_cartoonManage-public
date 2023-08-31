package cn.saberking.jvav.apm.framework.security.handle;

import cn.saberking.jvav.apm.common.constant.Constants;
import cn.saberking.jvav.apm.common.constant.HttpStatus;
import cn.saberking.jvav.apm.common.core.domain.AjaxResult;
import cn.saberking.jvav.apm.common.core.domain.model.LoginUser;
import cn.saberking.jvav.apm.common.utils.ServletUtils;
import cn.saberking.jvav.apm.common.utils.StringUtils;
import cn.saberking.jvav.apm.framework.manager.AsyncManager;
import cn.saberking.jvav.apm.framework.manager.factory.AsyncFactory;
import cn.saberking.jvav.apm.framework.web.service.TokenService;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author apm
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Resource
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @param request        请求
     * @param response       响应
     * @param authentication token
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.SUCCESS, "退出成功")));
    }
}
