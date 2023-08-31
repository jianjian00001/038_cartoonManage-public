package cn.saberking.jvav.apm.common.exception.job;

/**
 * 计划策略异常
 *
 * @author apm
 */
public class TaskException extends Exception {
    private static final long serialVersionUID = 1L;

    private final Code code;

    public TaskException(String msg, Code code) {
        this(msg, code, null);
    }

    public TaskException(String msg, Code code, Exception nestedEx) {
        super(msg, nestedEx);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }

    public enum Code {
        /**
         * 任务存在
         */
        TASK_EXISTS,
        /**
         * 任务不存在
         */
        NO_TASK_EXISTS,
        /**
         * 任务已启动
         */
        TASK_ALREADY_STARTED,
        /**
         * 未知任务
         */
        UNKNOWN,
        /**
         * 配置错误
         */
        CONFIG_ERROR,
        /**
         * 任务节点不可用
         */
        TASK_NODE_NOT_AVAILABLE
    }
}