package com.foo;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.foo.model.ProjectData;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：项目信息表读取
 *
 * @author yangpan3
 * @since 2022-02-24 14:54
 */
@Slf4j
class ProjectReadListener implements ReadListener<ProjectData> {

    /**
     * 缓存的数据
     */
    private Map<String, ProjectData> data = Maps.newHashMap();

    @Override
    public void invoke(ProjectData projectData, AnalysisContext analysisContext) {
        data.put(projectData.getProjectId(), projectData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("doAfterAllAnalysed [analysisContext] {}", data);
    }

    public Map<String, ProjectData> getData() {
        return data;
    }
}
