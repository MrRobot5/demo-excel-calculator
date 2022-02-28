package com.foo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.foo.model.Math;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 描述：读取权重计算公式表
 *
 * @author yangpan3
 * @since 2022-02-24 15:31
 */
@Slf4j
public class MathReadListener implements ReadListener<Math> {

    /**
     * 缓存的数据
     */
    private Map<String, String> data = Maps.newHashMap();

    @Override
    public void invoke(Math math, AnalysisContext analysisContext) {
        validate();
        data.put(math.getBu(), math.getMath().replace("（", "(").replace("）", ")"));
    }

    private void validate() {
        log.info("validate 公式支持用户自己配置(仅限+-*/及括号输入,参数范围(A,B,C,D))");
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("doAfterAllAnalysed [analysisContext] {}", data);
    }

    public Map<String, String> getData() {
        return data;
    }
}
