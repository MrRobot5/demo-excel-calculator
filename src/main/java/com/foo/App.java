package com.foo;

import com.alibaba.excel.EasyExcel;
import com.foo.model.Math;
import com.foo.model.ProjectData;
import com.singularsys.jep.Jep;
import com.singularsys.jep.JepException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Scanner;

/**
 * demo: 根据需求，输入excel 数据和公式，得出对应的计算结果
 *
 * @since 2022-2-24 16:09:32
 */
@Slf4j
public class App {

    public static void main(String[] args) throws JepException, IllegalAccessException {
        // 1. load project.xlsx
        ProjectReadListener excelListener = new ProjectReadListener();
        EasyExcel.read(new File("project.xlsx"), ProjectData.class, excelListener).sheet().doRead();
        // 2. load setting.xlsx
        MathReadListener mathReadListener = new MathReadListener();
        EasyExcel.read(new File("setting.xlsx"), Math.class, mathReadListener).sheet().doRead();
        // 3. load args
        Scanner in = new Scanner(System.in);
        System.out.println("请输入项目ID:");
        String name = in.nextLine();
        // 4. calculate
        calculate(excelListener, mathReadListener, name);
    }

    /**
     * 根据加载到的数据，进行计算并得出结果
     *
     * @param name 项目ID
     * @throws JepException
     * @throws IllegalAccessException
     */
    private static void calculate(ProjectReadListener excelListener, MathReadListener mathReadListener, String name) throws JepException, IllegalAccessException {
        Map<String, ProjectData> projects = excelListener.getData();
        Map<String, String> maths = mathReadListener.getData();
        Jep jep = new Jep();
        if (projects.containsKey(name)) {
            ProjectData project = projects.get(name);
            if (maths.containsKey(project.getBu())) {
                addVariable(jep, project);
                String math = maths.get(project.getBu());
                log.info("main [math] {}", math);
                jep.parse(math);
                Object result = jep.evaluate();
                log.info("main [result] {}", result);
            } else {
                log.info("main 部门不存在");
            }
        } else {
            log.info("main 输入的项目Id 不存在");
        }
    }

    /**
     * 给公式添加变量
     *
     * @param jep 公式
     * @param project input
     * @throws JepException
     * @throws IllegalAccessException
     */
    private static void addVariable(Jep jep, ProjectData project) throws JepException, IllegalAccessException {
        for (Field field : project.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            jep.addVariable(field.getName().toUpperCase(), field.get(project));
        }
    }

}
