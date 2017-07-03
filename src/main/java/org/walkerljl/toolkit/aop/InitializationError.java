package org.walkerljl.toolkit.aop;

/**
 * 初始化错误
 *
 * @author lijunlin
 */
public class InitializationError extends Error {

    private static final long serialVersionUID = 1L;

    public InitializationError() {
        super();
    }

    public InitializationError(String message) {
        super(message);
    }

    public InitializationError(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationError(Throwable cause) {
        super(cause);
    }
}