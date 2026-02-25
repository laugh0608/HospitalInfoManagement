package com.graduation.hospital.common.log;

import ch.qos.logback.core.pattern.Converter;
import org.slf4j.MDC;

/**
 * 自定义 Logback 转换器
 * 用于在日志格式中输出 MDC 中的 requestId
 */
public class MdcRequestIdConverter extends Converter<Object> {

    private static final String DEFAULT_VALUE = "-";
    private static final String MDC_KEY = "requestId";

    @Override
    public String convert(Object eventObject) {
        try {
            String requestId = MDC.get(MDC_KEY);
            if (requestId == null || requestId.isEmpty()) {
                return DEFAULT_VALUE;
            }
            return requestId;
        } catch (Exception e) {
            return DEFAULT_VALUE;
        }
    }
}